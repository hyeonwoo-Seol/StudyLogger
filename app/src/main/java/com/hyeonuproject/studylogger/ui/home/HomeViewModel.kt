package com.hyeonuproject.studylogger.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel : ViewModel() {

    private val _timerText = MutableStateFlow("00:00:00")
    val timerText: StateFlow<String> = _timerText.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null
    private var elapsedSeconds = 0L

    // UI에서 호출할 타이머 토글 함수
    fun toggleTimer() {
        if (_isTimerRunning.value) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _isTimerRunning.value = true
        // viewModelScope를 사용하여 ViewModel 생명주기에 맞춰 코루틴을 실행합니다.
        timerJob = viewModelScope.launch(Dispatchers.Default) {
            while (true) {
                delay(1000) // 1초 대기
                elapsedSeconds++
                _timerText.value = formatTime(elapsedSeconds)
            }
        }
    }

    private fun stopTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel() // 진행 중인 코루틴을 취소합니다.
    }

    // 초를 HH:mm:ss 형식의 문자열로 변환하는 함수
    private fun formatTime(seconds: Long): String {
        val hours = TimeUnit.SECONDS.toHours(seconds)
        val minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    // ViewModel이 소멸될 때 타이머를 정지시켜 메모리 누수를 방지합니다.
    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}