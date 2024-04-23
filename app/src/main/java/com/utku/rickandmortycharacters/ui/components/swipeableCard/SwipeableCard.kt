package com.utku.rickandmortycharacters.ui.components.swipeableCard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/**
 * A composable function that creates a swipeable card. This card can detect swipe gestures
 * and perform actions based on the swipe direction.
 *
 * @param modifier A [Modifier] applied to the card.
 * @param onSwipe A lambda function that receives the swipe direction and executes when the swipe is complete.
 * @param content A composable lambda that defines the content inside the swipeable card.
 */
@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    onSwipe: (direction: SwipeDirection) -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .detectSwipeGestures(onSwipeComplete = onSwipe) // Attach the swipe detection modifier.
            .fillMaxSize(),
    ) {
        content() // Build the content inside the Box composable.
    }
}

/**
 * Adds swipe detection functionality to a [Modifier].
 *
 * @param swipeThreshold The minimum swipe distance (in pixels) to recognize a swipe gesture.
 * @param onSwipeComplete A lambda that is called when a swipe gesture is completed.
 * @return Returns a [Modifier] with swipe detection capability.
 */
@Composable
fun Modifier.detectSwipeGestures(
    swipeThreshold: Float = 150f,
    onSwipeComplete: (SwipeDirection) -> Unit = {}
): Modifier {
    var offsetX by remember { mutableFloatStateOf(0f) } // State to hold the horizontal offset of the drag.
    var isSwiped by remember { mutableStateOf(false) } // State to check if the swipe has been completed.

    return this
        .graphicsLayer {
            translationX = offsetX // Apply horizontal translation to the layer.
            rotationZ = (offsetX / 20).coerceIn(-15f, 15f) // Apply a rotational effect based on the drag distance.
        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->
                change.consume()
                offsetX += dragAmount // Update the offset during the drag.
            }, onDragEnd = {
                if (offsetX.absoluteValue > swipeThreshold && !isSwiped) {
                    isSwiped = true
                    onSwipeComplete(if (offsetX > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT)
                }
                offsetX = 0f // Reset the offset after the swipe.
                isSwiped = false // Reset swipe status.
            })
        }
}

/**
 * Enum representing the direction of a swipe gesture.
 */
enum class SwipeDirection {
    LEFT,
    RIGHT
}

