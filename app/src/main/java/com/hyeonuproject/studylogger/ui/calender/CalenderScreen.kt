package com.hyeonuproject.studylogger.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.data.StudyRecord
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel()
) {
    val dailyRecords by viewModel.dailyRecords.collectAsState()
    val currentMonth by viewModel.currentMonth.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    // 달력 상태 설정 (현재 월 기준 앞뒤로 100개월)
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val daysOfWeek = remember { daysOfWeek() }
    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // 달력 UI
        HorizontalCalendar(
            state = state,
            dayContent = { day ->
                Day(day = day, isSelected = selectedDate == day.date) {
                    viewModel.loadRecordsForDate(it.date)
                }
            },
            monthHeader = { month ->
                val monthTitle = "${month.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.yearMonth.year}"
                Column {
                    Text(
                        text = monthTitle,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth()) {
                        daysOfWeek.forEach { dayOfWeek ->
                            Text(
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 일일 기록 리스트
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(dailyRecords) { record ->
                StudyRecordItem(record = record)
            }
        }
    }
}

// 각 날짜 셀 UI
@Composable
fun Day(day: CalendarDay, isSelected: Boolean, onClick: (CalendarDay) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f) // 정사각형 모양
            .padding(4.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = { onClick(day) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                day.position != DayPosition.MonthDate -> Color.Gray
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}


// 개별 기록 아이템 UI (변경 없음)
@Composable
fun StudyRecordItem(record: StudyRecord) {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val startTime = sdf.format(Date(record.startTime))
    val endTime = sdf.format(Date(record.endTime))
    val durationInMinutes = record.duration / 60
    val durationText = if (durationInMinutes > 0) "${durationInMinutes}분" else "${record.duration}초"


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
            Text(text = "시간: $startTime ~ $endTime ($durationText)")
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
        CalendarScreen()
    }
}