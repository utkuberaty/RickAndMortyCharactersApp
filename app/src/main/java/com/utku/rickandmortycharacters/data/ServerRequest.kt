package com.utku.rickandmortycharacters.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.utku.rickandmortycharacters.CharacterListQuery

class ServerRequest(private val apolloClient: ApolloClient) {

    fun getCharacterList(page: Int = 1) = apolloClient.query(
        CharacterListQuery(page)
    ).fetchPolicy(FetchPolicy.CacheAndNetwork)

}