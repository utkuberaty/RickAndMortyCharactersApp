package com.utku.rickandmortycharacters.data

import com.apollographql.apollo3.exception.ApolloException
import com.utku.rickandmortycharacters.CharacterListQuery

class CharacterRepository(
    private val serverRequest: ServerRequest
) {

    fun getCharacters(page: Int = 1) = serverRequest.getCharacterList(page)
}