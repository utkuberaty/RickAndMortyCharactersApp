package com.utku.rickandmortycharacters.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.swipeableCard(
    onSwiped: (Direction) -> Unit
): Modifier {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }

    return this
        .graphicsLayer {
            translationX = offsetX
            translationY = offsetY
            rotationZ = rotation
        }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
                rotation = offsetX * 0.1f // Adjust the rotation factor as needed
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { _ ->
                    val finalOffsetX = offsetX
                    when {
                        finalOffsetX > 200 -> onSwiped(Direction.RIGHT)
                        finalOffsetX < -200 -> onSwiped(Direction.LEFT)
                        else -> {
                            // Reset to initial position if not swiped far enough
                            offsetX = 0f
                            offsetY = 0f
                            rotation = 0f
                        }
                    }
                }
            )
        }
}

enum class Direction { LEFT, RIGHT }