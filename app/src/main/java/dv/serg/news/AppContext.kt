package dv.serg.news

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import dv.serg.lib.android.ActivityCallbacks
import dv.serg.news.AppContext.Companion.RecordType.Companion.BOOKMARKS
import dv.serg.news.AppContext.Companion.RecordType.Companion.HISTORY
import dv.serg.news.AppContext.Companion.RecordType.Companion.RECORD
import dv.serg.news.di.component.*
import dv.serg.news.di.module.AppModule
import dv.serg.news.di.module.BookmarkModule
import dv.serg.news.di.module.HistoryModule
import dv.serg.news.di.module.NewsModule
import dv.serg.news.ui.activity.NewsActivity
import dv.serg.news.ui.activity.RecordActivity
import dv.serg.news.ui.activity.SourceActivity

class AppContext : Application() {
    companion object {
        interface RecordType {
            companion object {
                const val HISTORY = 101
                const val BOOKMARKS = 102
                const val RECORD = "record"
            }
        }

        interface TimePattern {
            companion object {
                const val PRETTY = "dd-MM-yyyy HH:mm:ss"
                const val CURRENT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
            }
        }

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }


    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)

        context = this

        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        registerActivityLifecycleCallbacks(object : ActivityCallbacks() {
            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
                super.onActivityCreated(p0, p1)

//                MenuInflater(this@AppContext).
//                p0.menuInflater.inflate(R.menu.popup_menu, R.menu.activity_list_drawer)

                AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

                when (p0) {
                    is SourceActivity -> {
//                        DaggerSourceComponent.create().inject(p0)
                        DaggerSourceComponent.builder().appComponent(appComponent).build().inject(p0)
                    }
                    is NewsActivity -> {
                        DaggerNewsComponent.builder().appComponent(appComponent).newsModule(NewsModule(p0)).bookmarkModule(BookmarkModule(null)).historyModule(HistoryModule(null)).build().inject(p0)
                    }
                    is RecordActivity -> {
//                        Log.d("sergdv", "p1 = ${p0.intent.extras["trash"]}")
                        val flag: Int = p0.intent.extras.getInt(RECORD)
                        if (flag == HISTORY) {
                            DaggerHistoryComponent.builder().appComponent(appComponent).historyModule(HistoryModule(p0)).build().inject(p0)
                        } else if (flag == BOOKMARKS) {
                            DaggerBookmarkComponent.builder().appComponent(appComponent).bookmarkModule(BookmarkModule(p0)).build().inject(p0)
                        }
                    }

                }
            }
        })
    }
}