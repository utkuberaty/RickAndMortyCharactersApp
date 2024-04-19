package com.utku.rickandmortycharacters.ui.screens.characterScreenList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.utku.rickandmortycharacters.fragment.Character
import com.utku.rickandmortycharacters.ui.theme.RickAndMortyCharactersTheme
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
    getCharacterList: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(createCharacterListPager) {
            Text(text = it?.id ?: "")
        }
    }
}