package com.hyeonuproject.studylogger.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    // ViewModel의 StateFlow를 구독하여 데이터 변경을 감지합니다.
    val timerText by viewModel.timerText.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "선택된 카테고리: 알고리즘",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ViewModel의 timerText를 화면에 표시합니다.
        Text(
            text = timerText,
            fontSize = 72.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = "",
            onValueChange = { /*TODO*/ },
            label = { Text("메모 (Markdown)") }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 버튼 클릭 시 ViewModel의 toggleTimer 함수를 호출합니다.
        Button(
            onClick = { viewModel.toggleTimer() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .height(60.dp)
        ) {
            // isTimerRunning 상태에 따라 버튼 텍스트를 변경합니다.
            Text(text = if (isTimerRunning) "정지" else "시작", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StudyLoggerTheme {
        HomeScreen()
    }
}