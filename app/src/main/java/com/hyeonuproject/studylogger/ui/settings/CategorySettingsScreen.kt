package com.hyeonuproject.studylogger.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.data.Category

@Composable
fun CategorySettingsScreen(
    viewModel: SettingsViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    var newCategoryName by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            // 하단에 카테고리 추가 UI 배치
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newCategoryName,
                    onValueChange = { newCategoryName = it },
                    label = { Text("새 카테고리 이름") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    viewModel.addCategory(newCategoryName)
                    newCategoryName = "" // 입력창 초기화
                }) {
                    Text("추가")
                }
            }
        }
    ) { innerPadding ->
        // 카테고리 목록
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            items(categories) { category ->
                Text(text = category.name, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}