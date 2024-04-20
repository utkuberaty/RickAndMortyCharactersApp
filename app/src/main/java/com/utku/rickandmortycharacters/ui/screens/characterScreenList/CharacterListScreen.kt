package com.utku.rickandmortycharacters.ui.screens.characterScreenList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.utku.rickandmortycharacters.fragment.Character
import com.utku.rickandmortycharacters.ui.components.swipeableCard.GenericLazyCardStack
import org.koin.compose.koinInject

@Composable
fun CharacterListScreenController(
    modifier: Modifier = Modifier,
    characterListViewModel: CharacterListViewModel = koinInject()
) {
    val createCharacterListPager = characterListViewModel.pagingDataflow.collectAsLazyPagingItems()
    val state by remember {
        derivedStateOf {
            createCharacterListPager.loadState
        }
    }
    val isError by remember {
        derivedStateOf {
            state.refresh is LoadState.Error || state.append is LoadState.Error
        }
    }
    CharacterListScreen(
        modifier = modifier,
        createCharacterListPager = createCharacterListPager,
    )
}

@Composable
fun CharacterListScreen(
    createCharacterListPager: LazyPagingItems<Character>,
    modifier: Modifier = Modifier,
) {

    GenericLazyCardStack(
        modifier = modifier,
        items = createCharacterListPager.itemSnapshotList
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = it?.image,
                contentDescription = "character image"
            )
        }
    }
}
