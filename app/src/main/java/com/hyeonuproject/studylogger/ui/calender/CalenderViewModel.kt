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
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

class CalendarViewModel(private val studyDao: StudyDao) : ViewModel() {

    private val _dailyRecords = MutableStateFlow<List<StudyRecord>>(emptyList())
    val dailyRecords: StateFlow<List<StudyRecord>> = _dailyRecords.asStateFlow()

    // 현재 달력이 보여주는 연도와 월
    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    // 사용자가 선택한 날짜
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()


    init {
        // ViewModel 생성 시 오늘 날짜의 기록을 기본으로 불러옵니다.
        loadRecordsForDate(_selectedDate.value)
    }

    // LocalDate를 Date로 변환하여 기록을 불러오는 함수
    fun loadRecordsForDate(date: LocalDate) {
        _selectedDate.value = date
        val instant = date.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val convertedDate = Date.from(instant)

        val calendar = Calendar.getInstance()
        calendar.time = convertedDate

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val dayStart = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val dayEnd = calendar.timeInMillis

        studyDao.getRecordsByDay(dayStart, dayEnd)
            .onEach { records ->
                _dailyRecords.value = records
            }
            .launchIn(viewModelScope)
    }

    // 달력의 월이 변경될 때 호출되는 함수
    fun onMonthChanged(yearMonth: YearMonth) {
        _currentMonth.value = yearMonth
    }
}