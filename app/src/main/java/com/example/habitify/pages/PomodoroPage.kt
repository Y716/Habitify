package com.example.habitify.pages
import android.media.MediaPlayer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitify.R
import kotlinx.coroutines.delay
@Composable
fun PomodoroPage() {
    var taskName by remember { mutableStateOf("") }
    var taskList by remember { mutableStateOf(listOf<String>()) }
    var isPlaying by remember { mutableStateOf(false) }
    var pomodoroTime by remember { mutableIntStateOf(25 * 60) }
    var remainingTime by remember { mutableIntStateOf(pomodoroTime) }
    var isMusicPlaying by remember { mutableStateOf(false) }
    var workInterval by remember { mutableStateOf(25 * 60) }
    var breakInterval by remember { mutableStateOf(5 * 60) }
    var isBreakInterval by remember { mutableStateOf(false) }
    var isTaskCompleted by remember { mutableStateOf(false) }
    var mediaPlayer: MediaPlayer? = null
    var currentTrackIndex by remember { mutableStateOf(0) }
    val trackList =
        listOf(R.raw.track1, R.raw.track2, R.raw.track3, R.raw.track4, R.raw.track5, R.raw.track6)

    fun formatTime(timeInSeconds: Int): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    val context = LocalContext.current

    // Fungsi Putar Musik
    fun toggleMusic() {
        if (isMusicPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            isMusicPlaying = false
        } else {
            mediaPlayer = MediaPlayer.create(context, trackList[currentTrackIndex])
            mediaPlayer?.start()
            isMusicPlaying = true
            mediaPlayer?.setOnCompletionListener {
                isMusicPlaying = false
            }
        }
    }

    // Fungsi Next Track
    fun nextTrack() {
        if (currentTrackIndex < trackList.size - 1) {
            currentTrackIndex += 1
        } else {
            currentTrackIndex = 0
        }
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, trackList[currentTrackIndex])
        mediaPlayer?.start()
        isMusicPlaying = true
        mediaPlayer?.setOnCompletionListener {
            isMusicPlaying = false
        }
    }

    // Fungsi Track Sebelumnya
    fun previousTrack() {
        if (currentTrackIndex > 0) {
            currentTrackIndex -= 1
        } else {
            currentTrackIndex = trackList.size - 1
        }
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, trackList[currentTrackIndex])
        mediaPlayer?.start()
        isMusicPlaying = true
        mediaPlayer?.setOnCompletionListener {
            isMusicPlaying = false
        }
    }
    fun toggleTimer() {
        if (isPlaying) {
            isPlaying = false
            if (isMusicPlaying){
                toggleMusic()
            }
        } else {
            isPlaying = true
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying && remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1
        }
        if (remainingTime <= 0) {
            if (isBreakInterval) {
                pomodoroTime = workInterval
                remainingTime = workInterval
                isBreakInterval = false
            } else {
                pomodoroTime = breakInterval
                remainingTime = breakInterval
                isBreakInterval = true
            }
            if (isMusicPlaying) {
                toggleMusic()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(top = 25.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Task
        if (taskList.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = {
                        Text(
                            "Tambah Tugas Baru",
                            fontSize = 20.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        if (taskName.isNotEmpty()) {
                            taskList = taskList + taskName
                            taskName = ""
                            isTaskCompleted = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F9FA))
                    .padding(top = 25.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                taskList.forEachIndexed { index, task ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = task,
                                fontSize = 20.sp
                            )
                            Button(onClick = {
                                taskList = taskList.toMutableList().apply { removeAt(index) }
                                if (taskList.isEmpty()) {
                                    isTaskCompleted = false
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Check,
                                    contentDescription = "Task Completed"
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 50.dp))

        // Timer Pomodoro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(270.dp)) {
                val sweepAngle = (remainingTime / pomodoroTime.toFloat()) * 360f

                // Progress Circle Background
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = this.size,
                    style = Stroke(width = 10.dp.toPx())
                )

                // Progress Circle Dinamis
                if (isPlaying) {
                    drawArc(
                        color = Color(0xFF1976D2),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        size = this.size,
                        style = Stroke(
                            width = 20.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    )
                }
            }

            // Clock dan Detail Dalam Jam
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatTime(remainingTime),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Divider(
                    color = Color.LightGray,
                    thickness = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(14.dp)
                )
                Text(
                    text = if (isPlaying) "Tetap Fokus" else "Istirahat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light,
                    color = Gray
                )
            }
        }


        // Button Musik
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { previousTrack() }) {
                    Icon(
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Previous Track",
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp),
                    )
                }
                Button(
                    onClick = { toggleMusic() },
                    modifier = Modifier.size(170.dp, 40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    Text(
                        text = if (isMusicPlaying) "Hentikan Musik" else "Putar Musik",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
                IconButton(onClick = { nextTrack() }) {
                    Icon(
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Next Track",
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp)

                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 80.dp))

            // Button Timer
            Button(
                onClick = { toggleTimer() },
                modifier = Modifier.size(120.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pause Timer" else "Start Timer",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}




