// MainActivity.kt 전체를 아래 코드로 교체하세요.

package com.hyeonuproject.studylogger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hyeonuproject.studylogger.data.AppDatabase
import com.hyeonuproject.studylogger.ui.analysis.AnalysisScreen
import com.hyeonuproject.studylogger.ui.analysis.AnalysisViewModel
import com.hyeonuproject.studylogger.ui.calendar.CalendarScreen
import com.hyeonuproject.studylogger.ui.calendar.CalendarViewModel
import com.hyeonuproject.studylogger.ui.home.HomeScreen
import com.hyeonuproject.studylogger.ui.home.HomeViewModel
import com.hyeonuproject.studylogger.ui.settings.CategorySettingsScreen
import com.hyeonuproject.studylogger.ui.settings.SettingsScreen
import com.hyeonuproject.studylogger.ui.settings.SettingsViewModel
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val studyDao = database.studyDao()

        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(studyDao) as T
                    modelClass.isAssignableFrom(CalendarViewModel::class.java) -> CalendarViewModel(studyDao) as T
                    modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(studyDao) as T
                    modelClass.isAssignableFrom(AnalysisViewModel::class.java) -> AnalysisViewModel(studyDao) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            StudyLoggerTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { AppBottomNavigationBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(viewModel = viewModel(factory = viewModelFactory)) }
                        composable("calendar") { CalendarScreen(viewModel = viewModel(factory = viewModelFactory)) }
                        composable("analysis") { AnalysisScreen(viewModel = viewModel(factory = viewModelFactory)) }
                        composable("settings") { SettingsScreen(navController = navController) }
                        composable("categorySettings") { CategorySettingsScreen(viewModel = viewModel(factory = viewModelFactory)) }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("홈") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.DateRange, contentDescription = "Calendar") },
            label = { Text("캘린더") },
            selected = currentRoute == "calendar",
            onClick = { navController.navigate("calendar") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Analytics, contentDescription = "Analysis") },
            label = { Text("분석") },
            selected = currentRoute == "analysis",
            onClick = { navController.navigate("analysis") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("더보기") },
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") }
        )
    }
}