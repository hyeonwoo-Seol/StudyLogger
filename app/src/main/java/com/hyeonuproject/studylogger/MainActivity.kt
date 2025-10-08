package com.hyeonuproject.studylogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.data.AppDatabase
import com.hyeonuproject.studylogger.ui.calendar.CalendarScreen
import com.hyeonuproject.studylogger.ui.calendar.CalendarViewModel
import com.hyeonuproject.studylogger.ui.home.HomeScreen
import com.hyeonuproject.studylogger.ui.home.HomeViewModel
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val studyDao = database.studyDao()

        // 각 ViewModel을 생성할 Factory 준비
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(studyDao) as T
                    modelClass.isAssignableFrom(CalendarViewModel::class.java) -> CalendarViewModel(studyDao) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            StudyLoggerTheme {
                var selectedTab by remember { mutableStateOf("Home") }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                label = { Text("홈") },
                                selected = selectedTab == "Home",
                                onClick = { selectedTab = "Home" }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendar") },
                                label = { Text("캘린더") },
                                selected = selectedTab == "Calendar",
                                onClick = { selectedTab = "Calendar" }
                            )
                        }
                    }
                ) { innerPadding ->
                    // 선택된 탭에 따라 다른 화면을 보여줌
                    when (selectedTab) {
                        "Home" -> HomeScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel(factory = viewModelFactory)
                        )
                        "Calendar" -> CalendarScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = viewModel(factory = viewModelFactory)
                        )
                    }
                }
            }
        }
    }
}