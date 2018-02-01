package dv.serg.news.di.component

import android.app.Application
import dagger.Component
import dv.serg.news.di.PerApplication
import dv.serg.news.di.module.AppModule
import dv.serg.news.di.module.RetrofitModule
import dv.serg.news.model.dao.room.database.AppDatabase
import retrofit2.Retrofit

@PerApplication
@Component(modules = [AppModule::class, RetrofitModule::class])
interface AppComponent {
    fun application(): Application
    fun retrofit(): Retrofit
    fun database(): AppDatabase
    fun filter(): MutableList<String>
}