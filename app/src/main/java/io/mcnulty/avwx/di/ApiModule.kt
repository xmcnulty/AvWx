package io.mcnulty.avwx.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mcnulty.avwx.data.remote.AvWxApi
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

const val AVWX_BASE_URL = "https://avwx.rest/"

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun providesAvWxApi(): AvWxApi {
        return Retrofit.Builder()
            .baseUrl(AVWX_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AvWxApi::class.java)
    }
}