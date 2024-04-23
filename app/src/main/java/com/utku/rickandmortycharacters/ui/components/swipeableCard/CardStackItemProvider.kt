package com.utku.rickandmortycharacters.ui.components.swipeableCard

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.layout.LazyLayoutItemProvider
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import timber.log.Timber

/**
 * A generic card stack item provider for managing a stack of swipeable cards.
 * This class implements the LazyLayoutItemProvider interface to provide the items
 * and the swipe behavior for a LazyCardStack layout.
 *
 * @param T The type of the items in the card stack.
 * @param state The state of the card stack, used to control swipe actions.
 * @param items The paging items to be displayed within the card stack.
 * @param content A composable function that defines how to display each item in the card stack.
 */
@OptIn(ExperimentalFoundationApi::class)
class GenericCardStackItemProvider<T : Any>(
    private val state: LazyCardStackState,
    private val items: LazyPagingItems<T>,
    private val content: @Composable BoxScope.(T?) -> Unit = {}
) : LazyLayoutItemProvider {

    // Property to get the count of items in the stack.
    override val itemCount: Int = items.itemCount

    /**
     * Provides the UI component for each item at the specified index. Implements a swipeable card view.
     *
     * @param index The index of the item within the card stack.
     * @param key An identifier for the item, used to optimize recomposition.
     */
    @Composable
    override fun Item(index: Int, key: Any) {
        SwipeCard(
            onSwipe = {
                // Trigger the swipe away action on the top item of the stack.
                state.swipeAwayTopItem()
                // Log the swipe completion.
                Timber.i("onSwipe completed")
            },
            content = {
                // Render the content for the current item based on the passed index.
                content(items[index])
            }
        )
    }
}