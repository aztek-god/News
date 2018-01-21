package dv.serg.news.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dv.serg.news.di.PerFragment
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule {

    companion object {
        private const val AUTH_KEY = "a1f8deb5e535412cbd4f401b32725855"
        private const val BASE_URL = "https://newsapi.org/v2/"
        private const val HEADER_AUTH = "X-Api-Key"
        private const val CACHE_SIZE: Long = 10 * 1024 * 1024
    }

    @PerFragment
    @Provides
    fun provideCache(appContext: Application): Cache = Cache(appContext.cacheDir, CACHE_SIZE)

    @PerFragment
    @Provides
    fun provideHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder().header(HEADER_AUTH, AUTH_KEY)
                    val newRequest = requestBuilder.build()
                    chain.proceed(newRequest)
                }
                .cache(cache)
                .build()
    }

    @PerFragment
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

}