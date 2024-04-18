package com.utku.rickandmortycharacters.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import com.utku.rickandmortycharacters.ui.screens.characterScreenList.CharacterScreenList
import com.utku.rickandmortycharacters.ui.theme.RickAndMortyCharactersTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.core.annotation.KoinExperimentalAPI

class RootActivity : ComponentActivity() {
    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                RickAndMortyCharactersTheme {
                    CharacterScreenList(modifier = Modifier)
                }
            }
        }
    }
}