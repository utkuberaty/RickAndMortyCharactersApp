package com.utku.rickandmortycharacters.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.Executable
import com.apollographql.apollo3.api.http.HttpMethod
import com.apollographql.apollo3.cache.normalized.api.CacheKey
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGeneratorContext
import com.apollographql.apollo3.cache.normalized.api.CacheKeyResolver
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.api.TypePolicyCacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.logCacheMisses
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.apollographql.apollo3.network.okHttpClient
import com.utku.rickandmortycharacters.BuildConfig
import com.utku.rickandmortycharacters.data.CharacterRepositoryImp
import com.utku.rickandmortycharacters.ui.screens.characterScreenList.CharacterListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Module definition for the ViewModel layer of the application.
 * Provides a ViewModel component that handles the presentation logic
 * by interacting with the model through a repository.
 */
val viewModelModule = module {
    // Provide an instance of CharacterListViewModel to be used by Koin.
    viewModelOf(::CharacterListViewModel)
}

/**
 * Module definition for the Repository layer of the application.
 * Provides a singleton instance of the CharacterRepository implementation.
 */
val repositoryModule = module {
    // Create a singleton instance of CharacterRepositoryImp,
    // ensuring only one instance is used throughout the application.
    single { CharacterRepositoryImp(get()) }
}

/**
 * Module definition for the Network configuration of the application.
 * Provides a single instance of ApolloClient configured with caching and logging.
 */
val networkModule = module {
    // Configure and provide a singleton ApolloClient for GraphQL operations.
    single {
        // Define cache size and initialize SQL and memory caches.
        val memoryCacheSize: Int = 10 * 1024 * 1024 // 10 MB memory cache.
        val memoryCache = MemoryCacheFactory(maxSizeBytes = memoryCacheSize).chain(
            SqlNormalizedCacheFactory(
                androidContext()
            )
        )

        // Configure cache key generator for Apollo client.
        val cacheKeyGenerator = object : CacheKeyGenerator {
            override fun cacheKeyForObject(
                obj: Map<String, Any?>,
                context: CacheKeyGeneratorContext
            ): CacheKey? {
                return obj["id"]?.toString()?.let { CacheKey(it) }
                    ?: TypePolicyCacheKeyGenerator.cacheKeyForObject(
                        obj,
                        context,
                    )
            }
        }

        // Configure cache key resolver for handling field specific cache logic.
        val cacheKeyResolve = object : CacheKeyResolver() {
            override fun cacheKeyForField(
                field: CompiledField,
                variables: Executable.Variables
            ): CacheKey? {
                return (field.resolveArgument("id", variables) as String?)?.let { CacheKey(it) }
            }
        }

        // Define a function to configure and return an OkHttpClient instance.
        fun okHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            else logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }

        // Build the ApolloClient with the specified server URL, logging, and caching configurations.
        ApolloClient
            .Builder()
            .serverUrl(BuildConfig.BASE_URL)
            .logCacheMisses()
            .okHttpClient(okHttpClient())
            .normalizedCache(
                normalizedCacheFactory = memoryCache,
                cacheKeyGenerator = cacheKeyGenerator,
                cacheResolver = cacheKeyResolve
            ).build()
    }
}