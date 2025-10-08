package com.hyeonuproject.studylogger.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyeonuproject.studylogger.data.Category
import com.hyeonuproject.studylogger.data.StudyDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val studyDao: StudyDao) : ViewModel() {

    // 데이터베이스의 모든 카테고리 목록을 실시간으로 관찰합니다.
    val categories = studyDao.getAllCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 새로운 카테고리를 데이터베이스에 추가하는 함수
    fun addCategory(categoryName: String) {
        if (categoryName.isNotBlank()) {
            viewModelScope.launch {
                studyDao.insertCategory(Category(name = categoryName))
            }
        }
    }
}