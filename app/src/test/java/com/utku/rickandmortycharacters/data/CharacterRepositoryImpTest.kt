package com.utku.rickandmortycharacters.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.utku.rickandmortycharacters.CharacterListQuery
import com.utku.rickandmortycharacters.type.buildCharacter
import com.utku.rickandmortycharacters.type.buildCharacters
import com.utku.rickandmortycharacters.type.buildLocation
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class CharacterRepositoryTest {
    private lateinit var apolloClient: ApolloClient
    private lateinit var characterRepository: CharacterRepository

    @OptIn(ApolloExperimental::class)
    @Before
    fun setUp() = runBlocking {
        apolloClient = ApolloClient.Builder().networkTransport(QueueTestNetworkTransport()).build()
        characterRepository = CharacterRepositoryImp(apolloClient)
    }

    @OptIn(ApolloExperimental::class)
    @Test
    fun `test getCharacters returns expected data`() = runBlocking {
        // Mock GraphQL response
        val testQuery = CharacterListQuery(1)
        val testData = CharacterListQuery.Data {
            characters = buildCharacters {
                results = listOf(
                    buildCharacter {
                        id = "1"
                        name = "Luke Skywalker"
                        status = "Alive"
                        image = "Luke Image"
                        location = buildLocation {
                            id = "1"
                            name = "Tatooine"
                        }
                    }
                )
            }
        }
        apolloClient.enqueueTestResponse(testQuery, testData)
        val actual = characterRepository.getCharacters(1).execute().data
        assertNotNull(actual)
        assertEquals("Luke Skywalker", actual?.characters?.results?.firstOrNull()?.character?.name)
    }
}
