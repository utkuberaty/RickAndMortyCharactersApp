package com.utku.rickandmortycharacters.data

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.utku.rickandmortycharacters.CharacterListQuery

class CharacterRepositoryImp(
    private val apolloClient: ApolloClient
) : CharacterRepository {

    override fun getCharacters(page: Int) = apolloClient.query(
        CharacterListQuery(page)
    ).fetchPolicy(FetchPolicy.CacheAndNetwork)
}

interface CharacterRepository {
    fun getCharacters(page: Int = 1): ApolloCall<CharacterListQuery.Data>
}