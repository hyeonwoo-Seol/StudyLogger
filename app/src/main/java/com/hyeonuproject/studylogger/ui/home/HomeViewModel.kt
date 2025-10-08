package com.hyeonuproject.studylogger.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonuproject.studylogger.data.Category
import com.hyeonuproject.studylogger.data.StudyDao
import com.hyeonuproject.studylogger.data.StudyRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(private val studyDao: StudyDao) : ViewModel() {

    private val _timerText = MutableStateFlow("00:00:00")
    val timerText: StateFlow<String> = _timerText.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    // DB에서 모든 카테고리 목록을 가져옵니다.
    val categories = studyDao.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 현재 선택된 카테고리를 저장합니다.
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()


    private var timerJob: Job? = null
    private var startTime = 0L
    private var elapsedSeconds = 0L

    fun toggleTimer() {
        if (_isTimerRunning.value) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    // 사용자가 카테고리를 선택했을 때 호출되는 함수
    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }

    private fun startTimer() {
        // 선택된 카테고리가 없으면 타이머를 시작하지 않습니다.
        if (_selectedCategory.value == null) {
            // TODO: 사용자에게 카테고리를 먼저 선택하라는 메시지 표시 (예: Toast)
            return
        }
        _isTimerRunning.value = true
        startTime = System.currentTimeMillis()
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000)
                elapsedSeconds++
                _timerText.value = formatTime(elapsedSeconds)
            }
        }
    }

    private fun stopTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
        saveRecord()
    }

    private fun saveRecord() {
        // 선택된 카테고리가 있을 때만 저장합니다.
        _selectedCategory.value?.let { category ->
            viewModelScope.launch {
                val endTime = System.currentTimeMillis()
                val record = StudyRecord(
                    category = category.name, // 선택된 카테고리 이름 사용
                    startTime = startTime,
                    endTime = endTime,
                    duration = elapsedSeconds,
                    memo = "" // TODO: 메모 입력 연동
                )
                studyDao.insertStudyRecord(record)
                resetTimer()
            }
        }
    }

    private fun resetTimer() {
        elapsedSeconds = 0L
        _timerText.value = "00:00:00"
    }

    private fun formatTime(seconds: Long): String {
        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel 소멸 시 타이머가 돌고 있다면 저장하지 않고 종료
        timerJob?.cancel()
    }
}