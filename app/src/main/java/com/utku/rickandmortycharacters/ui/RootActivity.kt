package com.utku.rickandmortycharacters.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.utku.rickandmortycharacters.ui.screens.characterScreenList.CharacterListScreenController
import com.utku.rickandmortycharacters.ui.theme.RickAndMortyCharactersTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class RootActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoinAndroidContext {
                RickAndMortyCharactersTheme {
                    CharacterListScreenController(modifier = Modifier)
                }
            }
        }
    }
}