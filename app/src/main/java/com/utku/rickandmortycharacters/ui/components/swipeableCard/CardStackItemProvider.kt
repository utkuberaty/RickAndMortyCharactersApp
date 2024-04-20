package com.utku.rickandmortycharacters.ui.components.swipeableCard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable

@OptIn(ExperimentalFoundationApi::class)
class GenericCardStackItemProvider<T>(
    private val items: List<T>,
    private val content: @Composable (T) -> Unit = {}
) : LazyLayoutItemProvider {
    override val itemCount: Int = items.size

    @Composable
    override fun Item(index: Int, key: Any) {
        SwipeCard(
            content = {
                content(items[index])
            }
        )
    }
}


