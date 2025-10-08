package com.hyeonuproject.studylogger.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonuproject.studylogger.data.StudyDao
import com.hyeonuproject.studylogger.data.StudyRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(private val studyDao: StudyDao) : ViewModel() { // 생성자에 studyDao 추가

    private val _timerText = MutableStateFlow("00:00:00")
    val timerText: StateFlow<String> = _timerText.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

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

    private fun startTimer() {
        _isTimerRunning.value = true
        startTime = System.currentTimeMillis() // 시작 시간 기록
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
        saveRecord() // 타이머가 멈추면 기록 저장 함수 호출
    }

    // 기록을 데이터베이스에 저장하는 함수
    private fun saveRecord() {
        viewModelScope.launch {
            val endTime = System.currentTimeMillis()
            val record = StudyRecord(
                category = "알고리즘", // 지금은 임시 카테고리
                startTime = startTime,
                endTime = endTime,
                duration = elapsedSeconds,
                memo = "" // 지금은 임시 메모
            )
            studyDao.insertStudyRecord(record) // DAO를 통해 DB에 삽입
            resetTimer() // 타이머 초기화
        }
    }

    // 타이머 상태를 초기화하는 함수
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
        stopTimer()
    }
}