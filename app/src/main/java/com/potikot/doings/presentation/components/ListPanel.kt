package com.potikot.doings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.potikot.doings.presentation.util.Clickable

@Composable
fun <T : Clickable> ListPanel(
    list: List<T>,
    modifier: Modifier = Modifier,
    lastElement: T? = null,
    createElement: (@Composable LazyListScope.(T, Modifier) -> Unit)? = null,
) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = modifier.heightIn(max = 600.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val allItems = list + listOfNotNull(lastElement)
            val modifier = Modifier
            items(allItems) {
                if (createElement == null) {
                    DefaultListElementView(it, modifier)
                } else {
                    createElement(this@LazyColumn, it, modifier)
                }
            }
        }
    }
}

@Composable
private fun <T : Clickable> DefaultListElementView(
    e: T,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = e.onClick)
//            .combinedClickable(
//                onClick = e.onClick,
//                onLongClick = e.onLongClick
//            )
    ) {
        Text(
            e.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}