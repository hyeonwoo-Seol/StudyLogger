package com.hyeonuproject.studylogger.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hyeonuproject.studylogger.ui.theme.StudyLoggerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    val timerText by viewModel.timerText.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. 카테고리 선택 드롭다운 메뉴
        ExposedDropdownMenuBox(
            expanded = isDropdownExpanded,
            onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
        ) {
            OutlinedTextField(
                value = selectedCategory?.name ?: "카테고리를 선택하세요",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            viewModel.onCategorySelected(category)
                            isDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. 중앙 타이머
        Text(
            text = timerText,
            fontSize = 72.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. 메모 입력란
        OutlinedTextField(
            value = "",
            onValueChange = { /*TODO*/ },
            label = { Text("메모 (Markdown)") }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 4. 시작/정지 버튼
        Button(
            onClick = { viewModel.toggleTimer() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .height(60.dp)
        ) {
            Text(text = if (isTimerRunning) "정지" else "시작", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    StudyLoggerTheme {
        HomeScreen()
    }
}