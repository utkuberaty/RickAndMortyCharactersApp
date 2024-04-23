package com.utku.rickandmortycharacters.ui.screens.characterScreenList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.compose.paging.Pager
import com.utku.rickandmortycharacters.CharacterListQuery
import com.utku.rickandmortycharacters.data.CharacterRepositoryImp
import com.utku.rickandmortycharacters.fragment.Character

/**
 * ViewModel responsible for handling the character list UI logic.
 * This ViewModel retrieves character data from a repository and supports pagination using the Apollo GraphQL library.
 *
 * @param characterRepositoryImp The repository implementation used to fetch character data.
 */
class CharacterListViewModel(
    private val characterRepositoryImp: CharacterRepositoryImp
) : ViewModel() {

    // The flow of paging data, which is cached within the ViewModel scope to survive configuration changes.
    val pagingDataflow = createCharacterListPager()
        .flow
        .cachedIn(viewModelScope)

    /**
     * Creates a pager that handles the loading of character data pages from the GraphQL API.
     * The pager configures how data is loaded and how end-of-list is handled.
     *
     * @return A Pager object configured for Apollo GraphQL data fetching.
     */
    @OptIn(ApolloExperimental::class)
    private fun createCharacterListPager(): Pager<ApolloCall<CharacterListQuery.Data>, Character> {
        return Pager(
            config = PagingConfig(pageSize = 10), // Defines the number of items each page contains.
            appendCall = { response, _ ->
                // Determines if there is a next page available from the GraphQL data.
                val hasNextPage = response?.data?.characters?.info?.info?.next != null
                if (response != null && !hasNextPage) {
                    // If there is no next page, stop fetching more data.
                    return@Pager null
                }
                // Fetch the next page using the character repository implementation.
                characterRepositoryImp.getCharacters(
                    response?.data?.characters?.info?.info?.next ?: 1
                )
            },
            itemsAfter = { response, loadedItemsCount ->
                // Calculate the number of items remaining after the current load.
                response.data!!.characters!!.info!!.info.count!! - loadedItemsCount
            },
            getItems = { response ->
                // Handle errors in the GraphQL response.
                if (response.hasErrors()) {
                    Result.failure(
                        Throwable(response.errors.toString())
                    )
                } else {
                    // Successfully map the response to a list of Character objects.
                    Result.success(
                        response.data?.characters?.results?.mapNotNull {
                            it?.character
                        } ?: emptyList()
                    )
                }
            },
        )
    }
}