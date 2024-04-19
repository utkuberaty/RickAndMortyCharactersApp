package com.utku.rickandmortycharacters.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.CompiledField
import com.apollographql.apollo3.api.Executable
import com.apollographql.apollo3.cache.normalized.api.CacheKey
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.api.CacheKeyGeneratorContext
import com.apollographql.apollo3.cache.normalized.api.CacheKeyResolver
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.api.TypePolicyCacheKeyGenerator
import com.apollographql.apollo3.cache.normalized.logCacheMisses
import com.apollographql.apollo3.cache.normalized.normalizedCache
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.utku.rickandmortycharacters.BuildConfig
import com.utku.rickandmortycharacters.data.CharacterRepository
import com.utku.rickandmortycharacters.data.ServerRequest
import com.utku.rickandmortycharacters.ui.screens.characterScreenList.CharacterListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CharacterListViewModel)
}

val repositoryModule = module {
    single { CharacterRepository(get()) }
}

val networkModule = module {
    single {
        val memoryCacheSize: Int = 10 * 1024 * 1024
        val memoryCache = MemoryCacheFactory(maxSizeBytes = memoryCacheSize).chain(
            SqlNormalizedCacheFactory(
                androidContext()
            )
        )
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
        val cacheKeyResolve = object : CacheKeyResolver() {
            override fun cacheKeyForField(
                field: CompiledField,
                variables: Executable.Variables
            ): CacheKey? {
                return (field.resolveArgument("id", variables) as String?)?.let { CacheKey(it) }
            }
        }
        ApolloClient
            .Builder()
            .serverUrl(BuildConfig.BASE_URL)
            .logCacheMisses()
            .normalizedCache(
                normalizedCacheFactory = memoryCache,
                cacheKeyGenerator = cacheKeyGenerator,
                cacheResolver = cacheKeyResolve
            ).build()
    }

    single { ServerRequest(get()) }
}