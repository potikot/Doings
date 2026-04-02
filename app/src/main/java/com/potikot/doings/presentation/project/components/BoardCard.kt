package com.potikot.doings.presentation.project.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.Tag
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.presentation.util.generateMockBoard
import com.potikot.doings.ui.theme.DoingsTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BoardCard(
    board: Board,
    modifier: Modifier = Modifier,
    onCreateColumn: (String) -> Unit,
    onSelectColumn: (ColumnId) -> Unit,
    onCreateTask_Column: (ColumnId, String, String?) -> Unit,
    onOpenOptions_Column: (ColumnId) -> Unit,
    onDelete_Column: (ColumnId) -> Unit,
    onToggleDone_Task: (TaskId, Boolean) -> Unit,
    onOpenOptions_Task: (TaskId) -> Unit,
    onTagClick_Task: (TaskId, Tag) -> Unit
) {
    val initialPage = board.columns.indexOfFirst { it.id == board.selectedColumnId }.coerceAtLeast(0)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { board.columns.size + 1 }
    )

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(board.selectedColumnId) {
        val pageToScroll = board.columns.indexOfFirst { it.id == board.selectedColumnId }.coerceAtLeast(0)
        if (pageToScroll != pagerState.currentPage) {
            pagerState.animateScrollToPage(pageToScroll)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            if (page < board.columns.size) {
                val selectedColumn = board.columns[page]
                onSelectColumn(selectedColumn.id)
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Column
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            if (page < board.columns.size) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    ColumnCard(
                        column = board.columns[page],
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onCreateTask = onCreateTask_Column,
                        onOpenOptions = onOpenOptions_Column,
                        onDelete = onDelete_Column,
                        onToggleDone_Task = onToggleDone_Task,
                        onOpenOptions_Task = onOpenOptions_Task,
                        onTagClick_Task = onTagClick_Task
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Button(
                        onClick = { onCreateColumn("new column") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        Text("Создать колонку")
                    }
                }
            }
        }

        // Page Selector
        LazyRow(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            items(pagerState.pageCount) { index ->
                val isSelected = pagerState.currentPage == index

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 10.dp else 8.dp)
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), //if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun BoardCardPreview() {
    DoingsTheme {
        BoardCard(
            board = generateMockBoard(
                name = "Board 1",
                columnCount = 3,
                tasksPerColumn = 4
            ),
            modifier = Modifier,
            onCreateColumn = { },
            onSelectColumn = { },
            onCreateTask_Column = { _, _, _ -> },
            onOpenOptions_Column = { },
            onDelete_Column = { },
            onToggleDone_Task = { _, _ -> },
            onOpenOptions_Task = { },
            onTagClick_Task = { _, _ -> }
        )
    }
}