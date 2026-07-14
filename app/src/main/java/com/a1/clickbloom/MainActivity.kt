package com.a1.clickbloom

import android.os.Bundle
import android.view.SoundEffectConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.a1.clickbloom.ui.theme.ClickBloomTheme
import com.a1.clickbloom.ui.theme.DeepNavy
import com.a1.clickbloom.ui.theme.Mint
import com.a1.clickbloom.ui.theme.SoftBlue
import com.a1.clickbloom.ui.theme.Sunset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ClickBloomTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ClickBloomApp()
                }
            }
        }
    }
}

@Composable
private fun ClickBloomApp() {
    var counter by rememberSaveable { mutableIntStateOf(0) }
    var buttonPressed by remember { mutableStateOf(false) }
    var counterPulse by remember { mutableStateOf(false) }
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val buttonScale = androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (buttonPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 520f),
        label = "buttonScale",
    )

    val counterScale = androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (counterPulse) 1.05f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 420f),
        label = "counterScale",
    )

    LaunchedEffect(counterPulse) {
        if (counterPulse) {
            delay(140)
            counterPulse = false
        }
    }

    val subtitle = when {
        counter == 0 -> "Нажми на кнопку и счётчик оживёт."
        counter < 10 -> "Красивое начало. Продолжай."
        counter < 25 -> "Темп отличный, кнопка уже разогрета."
        counter < 50 -> "Счётчик расцвёл. Это уже серия."
        else -> "Это мощный клик-ритм. Впечатляет."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DeepNavy, Color(0xFF11264A), Color(0xFF1A3A5E)),
                ),
            ),
    ) {
        BloomBackdrop()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Click Bloom",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Мини-приложение со счётчиком, анимацией и мягким звуком клика",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.72f),
                        lineHeight = 24.sp,
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CounterCard(
                    counter = counter,
                    subtitle = subtitle,
                    modifier = Modifier.scale(counterScale.value),
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = {
                        view.playSoundEffect(SoundEffectConstants.CLICK)
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        counter += 1
                        counterPulse = true

                        scope.launch {
                            buttonPressed = true
                            delay(90)
                            buttonPressed = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(74.dp)
                        .graphicsLayer {
                            scaleX = buttonScale.value
                            scaleY = buttonScale.value
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Sunset,
                        contentColor = DeepNavy,
                    ),
                    shape = RoundedCornerShape(28.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 4.dp,
                    ),
                ) {
                    Text(
                        text = "Нажать",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OutlinedButton(
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            counter = 0
                            counterPulse = true
                        },
                        enabled = counter > 0,
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Refresh,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Сбросить")
                    }
                }
            }
        }
    }
}

@Composable
private fun CounterCard(
    counter: Int,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.14f),
                shape = RoundedCornerShape(32.dp),
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Твои клики",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold,
            ),
        )

        Spacer(modifier = Modifier.height(18.dp))

        AnimatedContent(
            targetState = counter,
            transitionSpec = {
                (slideInVertically { it / 2 } + fadeIn() + scaleIn(initialScale = 0.8f))
                    .togetherWith(slideOutVertically { -it / 2 } + fadeOut() + scaleOut(targetScale = 1.15f))
            },
            label = "counterText",
        ) { targetCount ->
            Text(
                text = targetCount.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    color = Mint,
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.72f),
                lineHeight = 24.sp,
            ),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(SoftBlue.copy(alpha = 0.22f))
                .padding(horizontal = 18.dp, vertical = 10.dp),
        ) {
            Text(
                text = "Следующий клик: ${counter + 1}",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                ),
            )
        }
    }
}

@Composable
private fun BloomBackdrop() {
    val transition = rememberInfiniteTransition(label = "backdrop")
    val driftA by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "driftA",
    )
    val driftB by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "driftB",
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val minSide = size.minDimension

        drawCircle(
            color = Sunset.copy(alpha = 0.18f),
            radius = minSide * 0.34f,
            center = Offset(
                x = size.width * (0.18f + 0.1f * driftA),
                y = size.height * (0.22f + 0.05f * driftB),
            ),
        )

        drawCircle(
            color = SoftBlue.copy(alpha = 0.16f),
            radius = minSide * 0.3f,
            center = Offset(
                x = size.width * (0.83f - 0.08f * driftB),
                y = size.height * (0.28f + 0.08f * driftA),
            ),
        )

        drawCircle(
            color = Mint.copy(alpha = 0.12f),
            radius = minSide * 0.38f,
            center = Offset(
                x = size.width * (0.52f + 0.06f * driftA),
                y = size.height * (0.86f - 0.04f * driftB),
            ),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ClickBloomPreview() {
    ClickBloomTheme {
        ClickBloomApp()
    }
}
