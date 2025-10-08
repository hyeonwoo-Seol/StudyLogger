package com.hyeonuproject.studylogger.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonuproject.studylogger.data.StudyDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AnalysisViewModel(studyDao: StudyDao) : ViewModel() {

    private val allRecords = studyDao.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val todayTotalDuration = allRecords.map { records ->
        // 오늘 자정 시간을 계산
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = calendar.timeInMillis

        val totalSeconds = records
            .filter { it.startTime >= todayStart }
            .sumOf { it.duration }
        formatDuration(totalSeconds)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0분")


    val categoryDurations = allRecords.map { records ->
        records.groupBy { it.category }
            .mapValues { (_, groupedRecords) ->
                groupedRecords.sumOf { it.duration }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    fun formatDuration(totalSeconds: Long): String {
        if (totalSeconds == 0L) return "0분"
        val hours = TimeUnit.SECONDS.toHours(totalSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
        return when {
            hours > 0 -> "${hours}시간 ${minutes}분"
            minutes > 0 -> "${minutes}분"
            else -> "${totalSeconds}초"
        }
    }
}