package com.utku.rickandmortycharacters.data

import com.apollographql.apollo3.ApolloClient
import com.utku.rickandmortycharacters.CharacterListQuery

class ServerRequest(private val apolloClient: ApolloClient) {

    fun getCharacterList(page: Int = 1) = apolloClient.query(
        CharacterListQuery(page)
    )

}