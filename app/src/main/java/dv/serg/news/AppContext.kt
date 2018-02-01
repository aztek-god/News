package dv.serg.news

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.facebook.stetho.Stetho
import dv.serg.lib.android.ActivityCallbacks
import dv.serg.news.di.component.*
import dv.serg.news.di.module.AppModule
import dv.serg.news.di.module.HistoryModule
import dv.serg.news.di.module.NewsModule
import dv.serg.news.ui.activity.HistoryActivity
import dv.serg.news.ui.activity.NewsActivity
import dv.serg.news.ui.activity.SourceActivity

class AppContext : Application() {
    companion object {
        interface ActivityType {
            companion object {
                const val HISTORY = 101
                const val BOOKMARKS = 102
            }
        }

        interface TimePattern {
            companion object {
                const val PRETTY = "dd-MM-yyyy HH:mm:ss"
                const val CURRENT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            }
        }
    }


    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        registerActivityLifecycleCallbacks(object : ActivityCallbacks() {
            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
                super.onActivityCreated(p0, p1)
                when (p0) {
                    is SourceActivity -> {
//                        DaggerSourceComponent.create().inject(p0)
                        DaggerSourceComponent.builder().appComponent(appComponent).build().inject(p0)
                    }
                    is NewsActivity -> {
                        DaggerNewsComponent.builder().appComponent(appComponent).newsModule(NewsModule(p0)).build().inject(p0)
                    }
                    is HistoryActivity -> {
                        DaggerHistoryComponent.builder().appComponent(appComponent).historyModule(HistoryModule(p0)).build().inject(p0)
                    }

                }
            }
        })
    }
}