package com.utku.rickandmortycharacters.data

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.utku.rickandmortycharacters.CharacterListQuery

/**
 * Implementation of [CharacterRepository] using an ApolloClient to fetch data from a GraphQL API.
 *
 * @property apolloClient The ApolloClient instance used for GraphQL queries.
 */
class CharacterRepositoryImp(
    private val apolloClient: ApolloClient
) : CharacterRepository {

    /**
     * Fetches a list of characters from the GraphQL server based on the given page number.
     *
     * @param page The page number to query for characters.
     * @return An ApolloCall object that will fetch the data when executed.
     */
    override fun getCharacters(page: Int) = apolloClient.query(
        CharacterListQuery(page)
    )
}

/**
 * Interface defining the repository for accessing characters data.
 */
interface CharacterRepository {
    /**
     * Gets characters data from the repository.
     *
     * @param page The page number to fetch, defaulting to 1 if not specified.
     * @return An ApolloCall object containing [CharacterListQuery.Data].
     */
    fun getCharacters(page: Int = 1): ApolloCall<CharacterListQuery.Data>
}
