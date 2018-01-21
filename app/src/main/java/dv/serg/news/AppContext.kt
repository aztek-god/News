package dv.serg.news

//import dv.serg.news.di.component.DaggerNewsListComponent
//import dv.serg.news.di.component.DaggerNewsListComponent
import android.app.Activity
import android.app.Application
import android.os.Bundle
import dv.serg.news.di.component.AppComponent
import dv.serg.news.di.component.DaggerAppComponent
import dv.serg.news.di.module.AppModule
import dv.serg.news.ui.activity.ListActivity

class AppContext : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()




        registerActivityLifecycleCallbacks(
                object : Application.ActivityLifecycleCallbacks {
                    override fun onActivityPaused(p0: Activity?) {

                    }

                    override fun onActivityResumed(p0: Activity?) {

                    }

                    override fun onActivityStarted(p0: Activity?) {

                    }

                    override fun onActivityDestroyed(p0: Activity?) {

                    }

                    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {

                    }

                    override fun onActivityStopped(p0: Activity?) {

                    }

                    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
                        when (activity) {
                            is ListActivity -> {
//                                DaggerNewsListComponent.builder().appComponent(appComponent)
//                                        .presenterModule(PresenterModule(activity))
//                                        .build().inject(activity)
                            }
                        }
                    }
                }
        )
    }
}