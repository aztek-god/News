package dv.serg.news.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import dv.serg.news.di.PerApplication

@Module
class AppModule(private val appContext: Application) {
    @PerApplication
    @Provides
    fun provideApplication() = appContext
}