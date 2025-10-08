package com.hyeonuproject.studylogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.data.AppDatabase
import com.hyeonuproject.studylogger.ui.home.HomeScreen
import com.hyeonuproject.studylogger.ui.home.HomeViewModel
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터베이스 인스턴스 생성
        val database = AppDatabase.getInstance(applicationContext)
        val studyDao = database.studyDao()

        // HomeViewModel을 생성할 Factory 준비
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(studyDao) as T
            }
        }

        enableEdgeToEdge()
        setContent {
            StudyLoggerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Factory를 사용하여 ViewModel 생성 후 HomeScreen에 전달
                    HomeScreen(viewModel = viewModel(factory = viewModelFactory))
                }
            }
        }
    }
}