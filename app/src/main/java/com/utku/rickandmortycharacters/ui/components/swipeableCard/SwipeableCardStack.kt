// SwipeableCardStack.kt
package com.utku.rickandmortycharacters.ui.components.swipeableCard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.lazy.layout.LazyLayoutMeasureScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> GenericLazyCardStack(
    modifier: Modifier = Modifier,
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    val lazyItemProvider = remember(items) { GenericCardStackItemProvider(items, content) }
    val lazyCardStackState = remember(items) { LazyCardStackState() }
    LazyLayout(
        modifier = modifier,
        itemProvider = { lazyItemProvider },
        measurePolicy = rememberCardStackMeasurePolicy(
            itemCount = lazyItemProvider.itemCount,
            state = lazyCardStackState,
        ),
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun rememberCardStackMeasurePolicy(
    itemCount: Int,
    state: LazyCardStackState
): LazyLayoutMeasureScope.(Constraints) -> MeasureResult =
    {
        val placeables = mutableListOf<Placeable>()
        val xOffset = state.offset.floatValue
        for (index in 0 until itemCount) {
            val placeable = measure(index, it)
            placeables.add(placeable.first())
        }
        layout(it.maxWidth, it.maxHeight) {
            // Reverse the order of placing, ensuring the first item is on top
            placeables.asReversed().forEachIndexed { index, placeable ->
                if (index == itemCount - 1 - state.topIndex) {
                    placeable.placeRelative(x = xOffset.roundToInt(), y = 0)
                } else {
                    placeable.placeRelative(x = 0, y = 0)
                }
            }
        }
    }

class LazyCardStackState {
    var topIndex = 0
    var offset = mutableFloatStateOf(0f)
    // Add more state handling as needed for animations and interactions
}

