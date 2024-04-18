package com.utku.rickandmortycharacters.di

import com.utku.rickandmortycharacters.ui.screens.characterScreenList.CharacterListViewModel
import com.utku.rickandmortycharacters.ui.screens.characterScreenList.CharacterScreenList
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CharacterListViewModel)
}