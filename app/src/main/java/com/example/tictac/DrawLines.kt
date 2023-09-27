package com.example.tictac


import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tictac.ui.theme.Purple40


@Composable
fun BoardBase() {
    Canvas(
        modifier = Modifier
            .size(300.dp)
            .padding(0.dp),
    ) {
        drawLine(
            color = Color.Black,
            strokeWidth = 15f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width / 3, y = 0f),
            end = Offset(x = size.width / 3, y = size.height)
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 15f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 2 / 3, y = 0f),
            end = Offset(x = size.width * 2 / 3, y = size.height)
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 15f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height / 3),
            end = Offset(x = size.width, y = size.height / 3)
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 15f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 2 / 3),
            end = Offset(x = size.width, y = size.height * 2 / 3)
        )
    }
}

@Composable
fun Circle() {
    val context = LocalContext.current
    val mediaPlayer = MediaPlayer.create(context, R.raw.click2)

    // Resetting media player once it's done
    mediaPlayer.setOnCompletionListener {
        it.reset()
        it.release()
    }

    // Play the sound
    mediaPlayer.start()

    Canvas(
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
    ) {
        drawCircle(
            color = Purple40,
            style = Stroke(width = 25f)
        )
    }
}

@Composable
fun Cross() {
    val context = LocalContext.current
    val mediaPlayer = MediaPlayer.create(context, R.raw.click)

    // Resetting media player once it's done
    mediaPlayer.setOnCompletionListener {
        it.reset()
        it.release()
    }

    // Play the sound
    mediaPlayer.start()

    Canvas(
        modifier = Modifier
            .size(50.dp)
            .padding(5.dp)
    ) {
        drawLine(
            color = Color.Blue,
            strokeWidth = 25f,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = size.height)
        )
        drawLine(
            color = Color.Blue,
            strokeWidth = 25f,
            start = Offset(x = 0f, y = size.height),
            end = Offset(x = size.width, y = 0f)
        )
    }
}

@Composable
fun PlaySound(isWinner: Boolean) {
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    // Initialize and play sound if isWinner is true
    LaunchedEffect(isWinner) {
        if (isWinner) {
            mediaPlayer?.release() // Release any previous instance.
            mediaPlayer = MediaPlayer.create(context, R.raw.victory).apply {
                setOnCompletionListener {
                    it.release()
                }
                start()
            }
        }
    }

    // Dispose and clean up on leaving the Composable
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}



