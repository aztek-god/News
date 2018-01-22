package dv.serg.news.di.module

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import dagger.Module
import dagger.Provides
import dv.serg.news.di.PerFragment
import dv.serg.news.util.TAG
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

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
    @Named("apiKey")
    fun provideApiKey(appContext: Application): String {
        try {
            val applicationInfo = appContext.packageManager.getApplicationInfo(appContext.packageName, PackageManager.GET_META_DATA)
            val bundle = applicationInfo.metaData
            return bundle["apiKey"] as String
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound")
            throw PackageManager.NameNotFoundException("Failed to load meta-data, NameNotFound: ${e.message}")
        } catch (e: NullPointerException) {
            throw NullPointerException("Failed to load meta-data, NullPointer: ${e.message}")
        }
    }

    @PerFragment
    @Provides
    fun provideCache(appContext: Application): Cache = Cache(appContext.cacheDir, CACHE_SIZE)

    @PerFragment
    @Provides
    fun provideHttpClient(cache: Cache, @Named("apiKey") apiKey: String): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder().header(HEADER_AUTH, apiKey)
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