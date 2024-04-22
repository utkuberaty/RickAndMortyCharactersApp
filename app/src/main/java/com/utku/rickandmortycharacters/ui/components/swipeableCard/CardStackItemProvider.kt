package com.utku.rickandmortycharacters.ui.components.swipeableCard

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalFoundationApi::class)
class GenericCardStackItemProvider<T : Any>(
    private val state: LazyCardStackState,
    private val items: LazyPagingItems<T>,
    private val content: @Composable BoxScope.(T?) -> Unit = {}
) : LazyLayoutItemProvider {

    override val itemCount: Int = items.itemCount

    @Composable
    override fun Item(index: Int, key: Any) {
        SwipeCard(
            onSwipe = {
                state.swipeAwayTopItem()
                Log.i("onSwipe", "onSwipe completed")
            },
            content = {
                content(items[index])
            }
        )
    }
}


