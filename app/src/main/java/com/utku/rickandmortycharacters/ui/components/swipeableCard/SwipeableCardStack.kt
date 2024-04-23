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

/**
 * Composable function that creates a lazy loading stack of cards which can be swiped away.
 * This generic composable can handle any type of data object.
 *
 * @param T The type of the items being displayed in the card stack.
 * @param modifier A [Modifier] applied to the LazyLayout composable.
 * @param items A collection of paging items that represent the data to be displayed.
 * @param content A composable lambda that defines how each item in the stack should be displayed.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> GenericLazyCardStack(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    content: @Composable BoxScope.(T?) -> Unit
) {
    // Remember the state that manages the index of the top visible card.
    val lazyCardStackState = remember { LazyCardStackState() }
    Timber.i("items count: " + items.itemCount) // Log the number of items in the stack.
    LazyLayout(
        modifier = modifier,
        itemProvider = { GenericCardStackItemProvider(lazyCardStackState, items, content) },
        measurePolicy = rememberCardStackMeasurePolicy(
            itemCount = items.itemCount,
            state = lazyCardStackState,
        ),
    )
}

/**
 * Composable function to remember and provide the measure policy for a card stack layout.
 * This defines how cards are measured and placed in the layout.
 *
 * @param itemCount The total number of items in the stack.
 * @param state The current state of the card stack, used to determine which items to measure.
 * @return A [MeasureResult] specifying how items are laid out.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberCardStackMeasurePolicy(
    itemCount: Int,
    state: LazyCardStackState
): LazyLayoutMeasureScope.(Constraints) -> MeasureResult = remember(itemCount, state) {
    { constraints ->
        // Determine the indices of the current and next items to be displayed.
        val topIndex = state.topIndex
        val nextIndex = if (topIndex + 1 < itemCount) topIndex + 1 else null

        val placeables = listOfNotNull(nextIndex, topIndex).mapNotNull { index ->
            measure(index, constraints).firstOrNull() // Measure the items if they exist.
        }

        // Place measured items on the layout at the specified positions.
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(x = 0, y = 0) // Place items without any offset.
            }
        }
    }
}

/**
 * Class representing the state of a lazy card stack. Manages the index of the top visible card.
 */
class LazyCardStackState {
    var topIndex by mutableIntStateOf(0) // The index of the top visible card.

    /**
     * Function to advance the top index, effectively swiping away the top card and revealing the next one.
     */
    fun swipeAwayTopItem() {
        topIndex += 1 // Increment the top index to move to the next card.
    }
}