package com.hyeonuproject.studylogger.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonuproject.studylogger.data.StudyDao
import com.hyeonuproject.studylogger.data.StudyRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar
import java.util.Date

class CalendarViewModel(private val studyDao: StudyDao) : ViewModel() {

    // 화면에 표시될 공부 기록 리스트
    private val _dailyRecords = MutableStateFlow<List<StudyRecord>>(emptyList())
    val dailyRecords: StateFlow<List<StudyRecord>> = _dailyRecords.asStateFlow()

    init {
        // ViewModel이 생성될 때 오늘 날짜의 기록을 기본으로 불러옵니다.
        loadRecordsForDate(Date())
    }

    // 특정 날짜의 기록을 불러오는 함수
    fun loadRecordsForDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // 선택한 날짜의 시작 시간 (00:00:00)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val dayStart = calendar.timeInMillis

        // 선택한 날짜의 종료 시간 (23:59:59)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val dayEnd = calendar.timeInMillis

        // DAO를 통해 해당 날짜의 기록을 가져옵니다.
        studyDao.getRecordsByDay(dayStart, dayEnd)
            .onEach { records ->
                _dailyRecords.value = records
            }
            .launchIn(viewModelScope)
    }
}