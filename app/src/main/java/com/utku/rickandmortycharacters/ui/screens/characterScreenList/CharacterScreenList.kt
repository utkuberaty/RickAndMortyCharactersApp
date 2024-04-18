package com.utku.rickandmortycharacters.ui.screens.characterScreenList

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.utku.rickandmortycharacters.ui.theme.RickAndMortyCharactersTheme
import org.koin.compose.koinInject

@Composable
fun CharacterScreenList(
    modifier: Modifier = Modifier,
    characterListViewModel: CharacterListViewModel = koinInject()
) {

}

@Preview
@Composable
fun CharacterScreenListPreview() {
    RickAndMortyCharactersTheme {
        CharacterScreenList()
    }
}