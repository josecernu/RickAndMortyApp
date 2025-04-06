package com.josecernu.rickandmortyapp.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.normalizedCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class GraphQlProvider {
    @Provides
    @Singleton
    fun provideBaseUrl() = "https://rickandmortyapi.com/graphql"

    @Provides
    @Singleton
    fun provideApolloClient(baseUrl: String): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(baseUrl)
            .normalizedCache(MemoryCacheFactory())
            .build()
    }
}
