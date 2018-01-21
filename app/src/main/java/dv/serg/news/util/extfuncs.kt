package dv.serg.news.util

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dv.serg.news.AppContext
import dv.serg.news.R
import dv.serg.news.di.component.DaggerNewsListComponent
import dv.serg.news.di.module.PresenterModule
import dv.serg.news.ui.activity.ListActivity
import dv.serg.news.ui.fragment.NewsListFragment

fun <T : Fragment> AppCompatActivity.addAppCompatFragment(containerId: Int, fragment: T?) {
    supportFragmentManager
            .beginTransaction()
            .add(containerId, fragment, fragment?.TAG)
            .commit()
}

//fun AppCompatActivity.replaceAddBackstackCompatFragment(containerId: Int,
//                                                        fragment: Fragment,
//                                                        tag: String? = null,
//                                                        backStackName: String? = null) {
//    if (tag != null) {
//        supportFragmentManager
//                .beginTransaction()
//                .replace(containerId, fragment, tag)
//                .addToBackStack(backStackName)
//                .commit()
//    } else {
//        supportFragmentManager
//                .beginTransaction()
//                .replace(containerId, fragment)
//                .addToBackStack(backStackName)
//                .commit()
//    }
//}

fun <T : Fragment?> ListActivity.startFragment(appActivity: AppCompatActivity, clazz: Class<T>, bundle: Bundle? = null) {

    val fragmentManager = appActivity.supportFragmentManager

    var fragment: Fragment? = fragmentManager.findFragmentByTag(clazz.TAG)

    if (fragment == null) {
        when (fragment) {
            is NewsListFragment -> fragment = NewsListFragment.newInstance(bundle)
        }
    }

    addAppCompatFragment(R.id.fragmentHolder,
            fragment)
}

interface Injectable {
    fun inject(receiver: Injectable?) {
        if (receiver != null) {
            when (receiver) {
                is NewsListFragment -> {
                    DaggerNewsListComponent
                            .builder()
                            .appComponent((receiver.activity.application as AppContext).appComponent)
                            .presenterModule(PresenterModule(receiver))
                            .build().inject(receiver)
                }
                else -> throw Exception()
            }
        }
    }
}

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }
