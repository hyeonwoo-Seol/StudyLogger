package com.hyeonuproject.studylogger.ui.analysis

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AnalysisScreen(
    modifier: Modifier = Modifier,
    viewModel: AnalysisViewModel = viewModel()
) {
    val todayTotalDuration by viewModel.todayTotalDuration.collectAsState()
    val categoryDurations by viewModel.categoryDurations.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 오늘 총 공부 시간 카드
        item {
            StatCard(title = "오늘 총 공부 시간", value = todayTotalDuration)
        }

        // 카테고리별 분석
        item {
            Text(
                text = "카테고리별 분석",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(categoryDurations.toList()) { (category, duration) ->
            StatCard(title = category, value = viewModel.formatDuration(duration))
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}