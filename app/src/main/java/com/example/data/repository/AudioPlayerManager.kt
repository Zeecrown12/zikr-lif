package com.example.data.repository

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

sealed class AudioPlayerState {
    object Idle : AudioPlayerState()
    object Buffering : AudioPlayerState()
    data class Playing(val currentPositionMs: Int, val durationMs: Int) : AudioPlayerState()
    data class Paused(val currentPositionMs: Int, val durationMs: Int) : AudioPlayerState()
    data class Error(val message: String) : AudioPlayerState()
}

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _playerState = MutableStateFlow<AudioPlayerState>(AudioPlayerState.Idle)
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    private val _currentPlayingId = MutableStateFlow<String?>(null)
    val currentPlayingId: StateFlow<String?> = _currentPlayingId.asStateFlow()

    private val _currentPlayingSurahNumber = MutableStateFlow<Int?>(null)
    val currentPlayingSurahNumber: StateFlow<Int?> = _currentPlayingSurahNumber.asStateFlow()

    private val _currentPlayingAyahNumber = MutableStateFlow<Int?>(null)
    val currentPlayingAyahNumber: StateFlow<Int?> = _currentPlayingAyahNumber.asStateFlow()

    private val _isRepeatEnabled = MutableStateFlow(false)
    val isRepeatEnabled: StateFlow<Boolean> = _isRepeatEnabled.asStateFlow()

    init {
        initTts()
    }

    private fun initTts() {
        try {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale("ar"))
                    isTtsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            scope.launch {
                                _playerState.value = AudioPlayerState.Playing(0, 5000)
                            }
                        }

                        override fun onDone(utteranceId: String?) {
                            scope.launch {
                                if (_isRepeatEnabled.value && _currentPlayingId.value != null) {
                                    // Repeat if enabled
                                    delay(500)
                                    lastSpokenText?.let { speakText(_currentPlayingId.value!!, it) }
                                } else {
                                    stop()
                                }
                            }
                        }

                        override fun onError(utteranceId: String?) {
                            scope.launch {
                                _playerState.value = AudioPlayerState.Error("Audio playback error.")
                            }
                        }
                    })
                }
            }
        } catch (_: Exception) {}
    }

    private var lastSpokenText: String? = null

    fun toggleRepeat() {
        _isRepeatEnabled.value = !_isRepeatEnabled.value
        mediaPlayer?.isLooping = _isRepeatEnabled.value
    }

    fun isPlaying(id: String): Boolean {
        return _currentPlayingId.value == id && _playerState.value is AudioPlayerState.Playing
    }

    fun isBuffering(id: String): Boolean {
        return _currentPlayingId.value == id && _playerState.value is AudioPlayerState.Buffering
    }

    fun playSurah(surahNumber: Int, audioUrl: String) {
        val id = "surah_$surahNumber"
        if (_currentPlayingId.value == id && mediaPlayer != null) {
            when (_playerState.value) {
                is AudioPlayerState.Playing -> {
                    pause()
                    return
                }
                is AudioPlayerState.Paused -> {
                    resume()
                    return
                }
                else -> {}
            }
        }

        stop()
        _currentPlayingId.value = id
        _currentPlayingSurahNumber.value = surahNumber
        _currentPlayingAyahNumber.value = null
        playStream(audioUrl)
    }

    fun playAyah(surahNumber: Int, ayahNumberInSurah: Int, audioUrl: String, arabicText: String = "") {
        val id = "ayah_${surahNumber}_$ayahNumberInSurah"
        if (_currentPlayingId.value == id) {
            if (_playerState.value is AudioPlayerState.Playing) {
                pause()
                return
            } else if (_playerState.value is AudioPlayerState.Paused) {
                resume()
                return
            }
        }

        stop()
        _currentPlayingId.value = id
        _currentPlayingSurahNumber.value = surahNumber
        _currentPlayingAyahNumber.value = ayahNumberInSurah

        if (audioUrl.isNotBlank()) {
            playStream(audioUrl, fallbackText = arabicText)
        } else if (arabicText.isNotBlank()) {
            speakText(id, arabicText)
        }
    }

    fun playDhikr(id: String, audioUrl: String = "", arabicText: String = "") {
        val playId = "dhikr_$id"
        if (_currentPlayingId.value == playId) {
            if (_playerState.value is AudioPlayerState.Playing) {
                pause()
                return
            } else if (_playerState.value is AudioPlayerState.Paused) {
                resume()
                return
            }
        }

        stop()
        _currentPlayingId.value = playId
        _currentPlayingSurahNumber.value = null
        _currentPlayingAyahNumber.value = null

        if (audioUrl.isNotBlank()) {
            playStream(audioUrl, fallbackText = arabicText)
        } else if (arabicText.isNotBlank()) {
            speakText(playId, arabicText)
        } else {
            _playerState.value = AudioPlayerState.Error("Audio recording unavailable.")
        }
    }

    private fun playStream(audioUrl: String, fallbackText: String = "") {
        _playerState.value = AudioPlayerState.Buffering

        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioUrl)
                isLooping = _isRepeatEnabled.value

                setOnPreparedListener { mp ->
                    mp.start()
                    _playerState.value = AudioPlayerState.Playing(0, mp.duration)
                    startProgressTracker()
                }

                setOnCompletionListener {
                    if (!_isRepeatEnabled.value) {
                        stop()
                    }
                }

                setOnErrorListener { _, _, _ ->
                    if (fallbackText.isNotBlank() && isTtsReady) {
                        speakText(_currentPlayingId.value ?: "stream", fallbackText)
                    } else {
                        _playerState.value = AudioPlayerState.Error("Unable to stream audio. Please verify internet connection.")
                    }
                    true
                }

                prepareAsync()
            }
        } catch (e: Exception) {
            if (fallbackText.isNotBlank() && isTtsReady) {
                speakText(_currentPlayingId.value ?: "stream", fallbackText)
            } else {
                _playerState.value = AudioPlayerState.Error("Audio error: ${e.localizedMessage ?: "Unknown"}")
            }
        }
    }

    private fun speakText(id: String, text: String) {
        lastSpokenText = text
        _currentPlayingId.value = id
        _playerState.value = AudioPlayerState.Buffering

        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
        } else {
            _playerState.value = AudioPlayerState.Error("Arabic audio speech synthesizer is initializing.")
        }
    }

    fun pause() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
                _playerState.value = AudioPlayerState.Paused(mp.currentPosition, mp.duration)
                progressJob?.cancel()
            }
        }
        tts?.stop()
        if (_playerState.value is AudioPlayerState.Playing && mediaPlayer == null) {
            _playerState.value = AudioPlayerState.Paused(0, 0)
        }
    }

    fun resume() {
        mediaPlayer?.let { mp ->
            mp.start()
            _playerState.value = AudioPlayerState.Playing(mp.currentPosition, mp.duration)
            startProgressTracker()
        }
        val currentId = _currentPlayingId.value
        val spokenText = lastSpokenText
        if (mediaPlayer == null && spokenText != null && currentId != null) {
            speakText(currentId, spokenText)
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let { mp ->
            try {
                mp.seekTo(positionMs)
                _playerState.value = AudioPlayerState.Playing(positionMs, mp.duration)
            } catch (e: Exception) {}
        }
    }

    fun stop() {
        progressJob?.cancel()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: Exception) {}
        mediaPlayer = null
        tts?.stop()
        _playerState.value = AudioPlayerState.Idle
        _currentPlayingId.value = null
        _currentPlayingSurahNumber.value = null
        _currentPlayingAyahNumber.value = null
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (true) {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        _playerState.value = AudioPlayerState.Playing(mp.currentPosition, mp.duration)
                    }
                }
                delay(500)
            }
        }
    }

    fun release() {
        stop()
        try {
            tts?.shutdown()
        } catch (e: Exception) {}
        tts = null
    }
}
