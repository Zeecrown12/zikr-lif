package com.example.ui.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.prayer.PrayerCalculator
import com.example.data.repository.ZikrPreferencesRepository
import com.example.ui.components.ZikrTopBar
import com.example.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun QiblaScreen(
    repository: ZikrPreferencesRepository,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val currentCity by repository.currentCity.collectAsState()

    val qiblaBearing = remember(currentCity) {
        PrayerCalculator.calculateQiblaBearing(currentCity.latitude, currentCity.longitude)
    }

    val distanceToKaaba = remember(currentCity) {
        PrayerCalculator.calculateDistanceToKaaba(currentCity.latitude, currentCity.longitude)
    }

    var deviceAzimuth by remember { mutableStateOf(0f) }
    var hasCompassSensor by remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotationSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        if (rotationSensor == null) {
            hasCompassSensor = false
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    val azimuthRad = orientation[0]
                    var azimuthDeg = Math.toDegrees(azimuthRad.toDouble()).toFloat()
                    if (azimuthDeg < 0) azimuthDeg += 360f
                    deviceAzimuth = azimuthDeg
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (rotationSensor != null) {
            sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager?.unregisterListener(listener)
        }
    }

    // Relative angle to rotate the needle
    val relativeAngle = (qiblaBearing - deviceAzimuth + 360f) % 360f
    val animatedRelativeAngle by animateFloatAsState(
        targetValue = relativeAngle,
        animationSpec = tween(durationMillis = 200),
        label = "qibla_rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
    ) {
        ZikrTopBar(
            title = "Qibla Direction",
            subtitle = "Direction to the Holy Kaaba in Makkah",
            showBack = true,
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // City & Coordinates Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = currentCity.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        )
                        Text(
                            text = "${currentCity.country} • %.2f°, %.2f°".format(currentCity.latitude, currentCity.longitude),
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "%,d km".format(distanceToKaaba),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                        )
                        Text(
                            text = "to Holy Kaaba",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Compass Dial View
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(6.dp, EmeraldDark, CircleShape)
                    .testTag("qibla_compass_dial"),
                contentAlignment = Alignment.Center
            ) {
                // Compass Cardinal points (N, E, S, W)
                Text(
                    text = "N",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    ),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                )
                Text(
                    text = "S",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "E",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                )
                Text(
                    text = "W",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 10.dp)
                )

                // Rotating Needle Pointer
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(if (hasCompassSensor) animatedRelativeAngle else qiblaBearing),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(190.dp)) {
                        val cx = size.width / 2
                        val cy = size.height / 2

                        // Needle pointing to Qibla (Green and Gold Arrow)
                        val needlePath = Path().apply {
                            moveTo(cx, cy - 80f)
                            lineTo(cx + 18f, cy)
                            lineTo(cx - 18f, cy)
                            close()
                        }
                        drawPath(needlePath, color = Color(0xFF047857))

                        // South side of needle
                        val southPath = Path().apply {
                            moveTo(cx, cy + 60f)
                            lineTo(cx + 14f, cy)
                            lineTo(cx - 14f, cy)
                            close()
                        }
                        drawPath(southPath, color = Color(0xFFD1D5DB))

                        // Center Pivot pin
                        drawCircle(color = Color(0xFFD97706), radius = 12f, center = Offset(cx, cy))
                        drawCircle(color = Color.White, radius = 5f, center = Offset(cx, cy))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bearing value
            Text(
                text = "%.1f° from North".format(qiblaBearing),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldDark
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = if (hasCompassSensor) EmeraldPale else GoldContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (hasCompassSensor) Icons.Default.Sensors else Icons.Default.Explore,
                        contentDescription = null,
                        tint = if (hasCompassSensor) EmeraldDark else GoldMuted
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (hasCompassSensor) {
                            "Live compass active. Hold device flat to align with Kaaba."
                        } else {
                            "Device compass sensor unavailable. Needle shows theoretical bearing."
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (hasCompassSensor) EmeraldDark else GoldMuted
                        )
                    )
                }
            }
        }
    }
}
