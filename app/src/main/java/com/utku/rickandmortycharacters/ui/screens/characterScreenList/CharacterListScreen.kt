package com.utku.rickandmortycharacters.ui.screens.characterScreenList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.utku.rickandmortycharacters.fragment.Character
import com.utku.rickandmortycharacters.ui.components.swipeableCard.GenericLazyCardStack
import com.utku.rickandmortycharacters.ui.theme.BlueLagoon
import com.utku.rickandmortycharacters.ui.theme.BrightSun
import org.koin.compose.koinInject

@Composable
fun CharacterListScreenController(
    modifier: Modifier = Modifier,
    characterListViewModel: CharacterListViewModel = koinInject()
) {
    val createCharacterListPager = characterListViewModel.pagingDataflow.collectAsLazyPagingItems()
    val state by remember {
        derivedStateOf { createCharacterListPager.loadState }
    }
    val isError by remember {
        derivedStateOf { state.refresh is LoadState.Error || state.append is LoadState.Error }
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
        modifier = modifier.background(color = BrightSun),
        items = createCharacterListPager,
    ) {
        Card(
            modifier = Modifier.align(Alignment.Center),
            colors = CardDefaults.cardColors().copy(containerColor = BlueLagoon)
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(20.dp)
                        .size(300.dp),
                    model = it?.image,
                    contentDescription = "character image"
                )
            }
        }
    }
}