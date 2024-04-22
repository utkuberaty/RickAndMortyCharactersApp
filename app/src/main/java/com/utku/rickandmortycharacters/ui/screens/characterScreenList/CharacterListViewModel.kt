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


class CharacterListViewModel(
    private val characterRepositoryImp: CharacterRepositoryImp
) : ViewModel() {

    val pagingDataflow = createCharacterListPager()
        .flow
        .cachedIn(viewModelScope)

    @OptIn(ApolloExperimental::class)
    private fun createCharacterListPager(): Pager<ApolloCall<CharacterListQuery.Data>, Character> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            appendCall = { response, _ ->
                val hasNextPage = response?.data?.characters?.info?.info?.next != null
                if (response != null && !hasNextPage) {
                    // Reached the end of the list
                    return@Pager null
                }
                characterRepositoryImp.getCharacters(
                    response?.data?.characters?.info?.info?.next ?: 1
                )
            },
            itemsAfter = { response, loadedItemsCount ->
                response.data!!.characters!!.info!!.info.count!! - loadedItemsCount
            },
            getItems = { response ->
                if (response.hasErrors()) {
                    Result.failure(
                        Throwable(response.errors.toString())
                    )
                } else {
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