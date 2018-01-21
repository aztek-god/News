package dv.serg.news.di.component

import android.app.Application
import dagger.Component
import dv.serg.news.di.PerApplication
import dv.serg.news.di.module.AppModule

@PerApplication
@Component(modules = [AppModule::class])
interface AppComponent {
    fun application(): Application
}