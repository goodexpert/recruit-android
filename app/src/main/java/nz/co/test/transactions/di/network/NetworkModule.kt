package nz.co.test.transactions.di.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.test.transactions.converters.BigDecimalAdapter
import nz.co.test.transactions.converters.OffsetDateTimeAdapter
import nz.co.test.transactions.services.TransactionsService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    const val baseUrl = "https://gist.githubusercontent.com/"

    @Singleton
    @Provides
    fun providesHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(BigDecimalAdapter)
            .add(OffsetDateTimeAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Singleton
    @Provides
    fun providesTransactionService(retrofit: Retrofit): TransactionsService =
        retrofit.create(TransactionsService::class.java)
}