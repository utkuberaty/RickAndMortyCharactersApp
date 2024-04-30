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
import androidx.compose.runtime.mutableStateOf
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

/**
 * A controller composable that manages the character list UI by handling state and rendering
 * the CharacterListScreen based on the ViewModel's data.
 *
 * @param modifier A [Modifier] applied to the CharacterListScreen composable.
 * @param characterListViewModel The ViewModel that provides character data and state management.
 */
@Composable
fun CharacterListScreenController(
    modifier: Modifier = Modifier,
    characterListViewModel: CharacterListViewModel = koinInject()
) {
    // Collect paging data from the ViewModel and convert it to lazy paging items.
    val createCharacterListPager = characterListViewModel.pagingDataflow.collectAsLazyPagingItems()
    // Remember and derive the current load state of the paging data.
    val state by remember {
        derivedStateOf { createCharacterListPager.loadState }
    }
    // Track if there's an error in either the refresh or append load states.
    val isError by remember {
        mutableStateOf(state.refresh is LoadState.Error || state.append is LoadState.Error)
    }
    // Log errors when they occur.
    LaunchedEffect(isError) {
        Timber.e("isError: $isError")
    }

    // Render the character list screen with the paging items.
    CharacterListScreen(
        modifier = modifier,
        createCharacterListPager = createCharacterListPager,
    )
}

/**
 * Displays a screen of swipeable cards for a list of characters.
 *
 * @param createCharacterListPager Lazy paging items for character data.
 * @param modifier A [Modifier] applied to the GenericLazyCardStack composable.
 */
@Composable
fun CharacterListScreen(
    createCharacterListPager: LazyPagingItems<Character>,
    modifier: Modifier = Modifier,
) {
    // Display a stack of cards with characters using a lazy card stack layout.
    GenericLazyCardStack(
        modifier = modifier.background(
            brush = Brush.linearGradient(BackGroundColors) // Apply a linear gradient background.
        ),
        items = createCharacterListPager,
    ) {
        // Define the appearance and layout of each card.
        Card(
            modifier = Modifier.align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
        ) {
            // Layout for the content of the card.
            Column(
                modifier = Modifier.background(
                    Brush.linearGradient(CardBackgroundColors) // Gradient background for each card.
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Display character image.
                AsyncImage(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(300.dp),
                    model = it?.image,
                    contentDescription = "character image"
                )
                // Display character name.
                Text(
                    text = it?.name ?: "",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
                Spacer(modifier = Modifier.padding(5.dp))
                // Display character status with a colored dot and text.
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(
                                when (it?.status) {
                                    "Alive" -> Color.Green
                                    "Dead" -> Color.Red
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
                // Display location name of the character.
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
