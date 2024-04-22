package com.utku.rickandmortycharacters.ui.screens.characterScreenList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.utku.rickandmortycharacters.fragment.Character
import com.utku.rickandmortycharacters.ui.components.swipeableCard.GenericLazyCardStack
import com.utku.rickandmortycharacters.ui.theme.BackGroundColors
import com.utku.rickandmortycharacters.ui.theme.CardBackgroundColors
import org.koin.compose.koinInject
import timber.log.Timber

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
    LaunchedEffect(isError) {
        Timber.e("isError: $isError")
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
        modifier = modifier.background(
            brush = Brush.linearGradient(BackGroundColors)
        ),
        items = createCharacterListPager,
    ) {
        Card(
            modifier = Modifier.align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.background(
                    Brush.linearGradient(CardBackgroundColors)
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(300.dp),
                    model = it?.image,
                    contentDescription = "character image"
                )
                Text(
                    text = it?.name ?: "",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                when (it?.status) {
                                    "Alive" -> {
                                        Color.Green
                                    }

                                    "Dead" -> {
                                        Color.Red
                                    }

                                    else -> Color(0xFF686766)
                                },
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    Text(
                        text = it?.status?.capitalize(Locale.current) ?: "",
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = it?.location?.name ?: "",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.padding(5.dp))
            }
        }
    }
}