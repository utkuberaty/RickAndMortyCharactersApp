package com.utku.rickandmortycharacters.ui.components.swipeableCard

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.paging.compose.LazyPagingItems
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> GenericLazyCardStack(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    content: @Composable BoxScope.(T?) -> Unit
) {
    val lazyCardStackState = remember { LazyCardStackState() }
    Timber.i("items count: " + items.itemCount)
    LazyLayout(
        modifier = modifier,
        itemProvider = { GenericCardStackItemProvider(lazyCardStackState, items, content) },
        measurePolicy = rememberCardStackMeasurePolicy(
            itemCount = items.itemCount,
            state = lazyCardStackState,
        ),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberCardStackMeasurePolicy(
    itemCount: Int,
    state: LazyCardStackState
): LazyLayoutMeasureScope.(Constraints) -> MeasureResult = remember(itemCount, state) {
    { constraints ->
        // Define which items to measure based on the current state.
        val topIndex = state.topIndex
        val nextIndex = if (topIndex + 1 < itemCount) {
            topIndex + 1
        } else null  // Check bounds to avoid indexing errors.

        val placeables = listOfNotNull(
            nextIndex,
            topIndex
        ).mapNotNull { index ->
            measure(index, constraints).firstOrNull()  // Safely measure each item if it exists.
        }  // Remove any nulls in case of measurement issues.

        // Layout the measured placeables, applying vertical offsets.
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(x = 0, y = 0)
            }
        }
    }
}

class LazyCardStackState {
    var topIndex by mutableIntStateOf(0)

    fun swipeAwayTopItem() {
        topIndex += 1  // Increment to update the top index, causing the next card to become the top one.
    }
}

