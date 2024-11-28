package com.example.habitify.pages
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.button.ButtonConstants
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
        }
    }

    fun formatTime(timeInSeconds: Int): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(top = 25.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                    label = { Text("Tambah Tugas Baru",
                        fontSize = 20.sp) },
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
                            })  {
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
        // Timer dengan lingkaran
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(270.dp)) {
                val sweepAngle = (remainingTime / pomodoroTime.toFloat()) * 360f
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    size = this.size,
                    style = Stroke(width = 10.dp.toPx())
                )
                if (isPlaying) {
                    drawArc(
                        color = Color(0xFF1976D2),
                        startAngle = -90f,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        size = this.size,
                        style = Stroke(width = 20.dp.toPx())
                    )
                }
            }
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

        // Buttons to control Timer and Music (stacked in Column)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            
            // Putar Musik button
            Button(
                onClick = { toggleMusic() },
                modifier = Modifier.size(150.dp, 40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray)
            ) {
                Text(
                    text = if (isMusicPlaying) "Hentikan Musik" else "Putar Musik",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )

            }

            Spacer(modifier = Modifier.padding(top = 80.dp))
            // Start/Pause Timer button
            Button(
                onClick = { isPlaying = !isPlaying },
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

fun toggleMusic() {
    TODO("Not yet implemented")
}

