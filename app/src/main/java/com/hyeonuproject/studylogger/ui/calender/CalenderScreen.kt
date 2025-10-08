package com.hyeonuproject.studylogger.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.data.StudyRecord
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(viewModel: CalendarViewModel = viewModel()) { // viewModel 주입
    // ViewModel의 dailyRecords를 구독하여 데이터 변경을 실시간으로 감지
    val dailyRecords by viewModel.dailyRecords.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "2025년 10월", // TODO: 실제 달력으로 교체 예정
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ViewModel에서 가져온 실제 데이터 리스트를 표시
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dailyRecords) { record ->
                StudyRecordItem(record = record)
            }
        }
    }
}

// 개별 기록 아이템 UI (이 부분은 변경 없음)
@Composable
fun StudyRecordItem(record: StudyRecord) {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val startTime = sdf.format(Date(record.startTime))
    val endTime = sdf.format(Date(record.endTime))
    val duration = "${record.duration / 60}분"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = record.category,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = "시간: $startTime ~ $endTime ($duration)")
            if (record.memo.isNotBlank()) {
                Text(text = "메모: ${record.memo}")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    StudyLoggerTheme {
        CalendarScreen() // Preview는 ViewModel 없이 UI만 보여줌
    }
}