package com.example.habitify.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun PomodoroPage() {
    var taskName by remember { mutableStateOf(TextFieldValue("")) }
    var taskList by remember { mutableStateOf(listOf<String>()) }
    var isPlaying by remember { mutableStateOf(false) }
    var pomodoroTime by remember { mutableIntStateOf(25 * 60) }
    var remainingTime by remember { mutableIntStateOf(pomodoroTime) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying){
        while (isPlaying && remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1
        }
        if (remainingTime <= 1) {
            isPlaying = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (taskList.isEmpty()) {
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Tambah Tugas Baru") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    if (taskName.text.isNotEmpty()) {
                        taskList = taskList + taskName.text
                        taskName = TextFieldValue("")
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Tambah Tugas")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                taskList.forEachIndexed { index, task ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = task,
                                fontSize = (18.sp),
                                fontWeight = FontWeight.Normal,
                                color = Color.Black)
                            Button(onClick = {
                                taskList = taskList.toMutableList().apply {
                                    removeAt(index)
                                }
                            }) {
                                Text("Selesai")
                            }
                        }
                    }
                }
            }
        }
    }
}