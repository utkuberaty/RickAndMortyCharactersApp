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

@Composable
fun SwipeCard(
    modifier: Modifier = Modifier,
    onSwipe: (direction: SwipeDirection) -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .detectSwipeGestures(onSwipeComplete = onSwipe)
            .fillMaxSize(),
    ) {
        content()
    }
}

@Composable
fun Modifier.detectSwipeGestures(
    swipeThreshold: Float = 150f, // Define a threshold for successful swipe
    onSwipeComplete: (SwipeDirection) -> Unit = {} // Lambda to be called on swipe completion
): Modifier {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isSwiped by remember { mutableStateOf(false) }

    return this
        .graphicsLayer { // Use graphicsLayer for smooth animations and transformations
            translationX = offsetX
            // Optionally, you can add a rotation based on the offsetX to simulate a tilt during swipes.
            rotationZ = (offsetX / 20).coerceIn(-15f, 15f)
        }
        .pointerInput(Unit) {
            detectHorizontalDragGestures(onHorizontalDrag = { change, dragAmount ->
                change.consume()
                offsetX += dragAmount
            }, onDragEnd = {
                if (offsetX.absoluteValue > swipeThreshold && !isSwiped) {
                    isSwiped = true
                    onSwipeComplete(if (offsetX > 0) SwipeDirection.RIGHT else SwipeDirection.LEFT) // True if swiped right, false if left
                }
                offsetX = 0f // Reset position after swipe
                isSwiped = false // Reset swipe status
            })
        }
}

enum class SwipeDirection {
    LEFT,
    RIGHT
}
