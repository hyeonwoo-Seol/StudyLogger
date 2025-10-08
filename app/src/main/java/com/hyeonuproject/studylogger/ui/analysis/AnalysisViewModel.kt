package com.hyeonuproject.studylogger.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonuproject.studylogger.data.StudyDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

class AnalysisViewModel(studyDao: StudyDao) : ViewModel() {

    // 모든 공부 기록을 가져옵니다.
    private val allRecords = studyDao.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 오늘 총 공부 시간을 계산합니다 (StateFlow<String> 형식).
    val todayTotalDuration = allRecords.map { records ->
        val todayStart = System.currentTimeMillis() // Simplification for today
        val todayRecords = records.filter { it.startTime >= todayStart - TimeUnit.DAYS.toMillis(1) }
        val totalSeconds = todayRecords.sumOf { it.duration }
        formatDuration(totalSeconds)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "00:00:00")


    // 카테고리별 공부 시간 합계를 계산합니다 (StateFlow<Map<String, Long>> 형식).
    val categoryDurations = allRecords.map { records ->
        records.groupBy { it.category }
            .mapValues { (_, groupedRecords) ->
                groupedRecords.sumOf { it.duration }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    // 초를 "HH시간 mm분" 형식으로 변환하는 함수
    fun formatDuration(totalSeconds: Long): String {
        val hours = TimeUnit.SECONDS.toHours(totalSeconds)
        val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
        return if (hours > 0) {
            "${hours}시간 ${minutes}분"
        } else {
            "${minutes}분"
        }
    }
}