package com.potikot.doings.presentation.project.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potikot.doings.domain.model.Board
import com.potikot.doings.domain.model.BoardId
import com.potikot.doings.domain.model.ColumnId
import com.potikot.doings.domain.model.ID
import com.potikot.doings.domain.model.Project
import com.potikot.doings.domain.model.TaskId
import com.potikot.doings.presentation.util.generateMockProject
import com.potikot.doings.ui.theme.DoingsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProjectCard(
    project: Project,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    onSelectBoard: (BoardId) -> Unit,
    onCreateBoard: (String) -> Unit,
    onOpenOptions: (ID) -> Unit,
    onCreateColumn_Board: (String) -> Unit,
    onSelectColumn_Board: (ColumnId) -> Unit,
    onCreateTask_Column: (ColumnId, String, String?) -> Unit,
    onOpenOptions_Column: (ColumnId) -> Unit,
    onDelete_Column: (ColumnId) -> Unit,
    onToggleDone_Task: (TaskId, Boolean) -> Unit,
    onOpenOptions_Task: (TaskId) -> Unit,
) {
    val pageCount = project.boards.size
    val currentPage = project.selectedBoardId?.let { project.boards.indexOfFirst { it.id == project.selectedBoardId } } ?: 0

    val chipRowState = rememberLazyListState()

    val scrollOffset = LocalWindowInfo.current.containerSize.width / -3
    LaunchedEffect(currentPage) {
        chipRowState.animateScrollToItem(
            index = currentPage.coerceAtLeast(0),
            scrollOffset = scrollOffset
        )
    }

    Column(
        modifier = modifier
    ) {
        // top bar
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { onNavigateBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "Return to projects list",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        onOpenOptions(project.id)
//                        val board = project.boards.first {
//                            it.id == vm.state.value.boardId
//                        }
//                        val column = board.columns.first {
//                            it.id == vm.state.value.columnId
//                        }
//                         Log.d("Project Screen",
//                         "current project: ${project.name}(${project.id.value})\n" +
//                                 "current board: ${board.name}(${vm.state.value.boardId?.value})/(${project.selectedBoardId?.value})\n" +
//                                 "current column: ${column.name}(${vm.state.value.columnId?.value})/(${board.selectedColumnId?.value})")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = "Project options",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // pager
        LazyRow(
            state = chipRowState,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            items(pageCount) { index ->
                val board = project.boards[index]
                val isSelected = currentPage == index

                BoardListItem(
                    board = board,
                    isSelected = isSelected,
                    modifier = Modifier
//                        .padding(horizontal = 4.dp)
                        .combinedClickable(
                            enabled = true,
                            onClick = { onSelectBoard(board.id) },
                            onLongClick = { onOpenOptions(board.id) }
                        )
                )
            }
            item {
                IconButton(
                    onClick = { onCreateBoard("New Board") },
                    modifier = Modifier.size(24.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        }

        if (project.boards.isEmpty()) {
            return@Column
        }

        // board
        Box(
            modifier = Modifier
                .weight(1f)
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f))
            ) {
                BoardCard(
                    board = project.boards[currentPage],
                    modifier = Modifier.padding(top = 8.dp),
                    onCreateColumn = onCreateColumn_Board,
                    onSelectColumn = onSelectColumn_Board,
                    onCreateTask_Column = onCreateTask_Column,
                    onOpenOptions_Column = onOpenOptions_Column,
                    onDelete_Column = onDelete_Column,
                    onToggleDone_Task = onToggleDone_Task,
                    onOpenOptions_Task = onOpenOptions_Task
                )
            }
        }
    }
}

@Composable
private fun BoardListItem(
    board: Board,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier.padding(horizontal = 8.dp).padding(top = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            val strokeModifier = if (!isSelected) Modifier else Modifier
                .drawBehind {
                    val strokeWidth = 2.dp.toPx()
                    val y = size.height - strokeWidth / 2
                    drawLine(
                        color = contentColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = strokeWidth
                    )
                }

            Text(
                text = board.name,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                modifier = strokeModifier.padding(bottom = 8.dp)
            )
//            if (isSelected) {
//                Box(
//                    modifier = Modifier
//                        .height(2.dp)
//                        .fillMaxWidth()
//                        .background(contentColor)
//                )
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProjectCardPreview() {
    DoingsTheme {
        ProjectCard(
            project = generateMockProject(
                name = "Some project",
                boardCount = 3,
                columnsPerBoard = 5,
                tasksPerColumn = 7
            ),
            onNavigateBack = { },
            onSelectBoard = { },
            onCreateBoard = { },
            onOpenOptions = { },
            onCreateColumn_Board = { },
            onSelectColumn_Board = { },
            onCreateTask_Column = { _, _, _ -> },
            onOpenOptions_Column = { },
            onDelete_Column = { },
            onToggleDone_Task = { _, _ -> },
            onOpenOptions_Task = { },
        )
    }
}