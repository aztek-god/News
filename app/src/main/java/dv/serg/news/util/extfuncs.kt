package dv.serg.news.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import dv.serg.news.AppContext
import dv.serg.news.di.component.DaggerNewsListComponent
import dv.serg.news.di.module.PresenterModule
import dv.serg.news.ui.fragment.NewsFragment
import dv.serg.news.ui.presenter.Presenter
import io.reactivex.disposables.CompositeDisposable
import kotlin.reflect.KFunction0

fun <T : Fragment> AppCompatActivity.startFragment(fragment: Class<T>, placeholder: Int, isAddToBackstack: Boolean = false) {
    val supportFragmentManager = supportFragmentManager
    val newInstance: T = fragment.newInstance()
    val transaction = supportFragmentManager.beginTransaction()
            .replace(placeholder, newInstance, newInstance.TAG)

    if (isAddToBackstack) {
        transaction.addToBackStack(newInstance.TAG)
    }

    transaction.commit()
}

fun <T : Fragment> AppCompatActivity.startFragment(fragment: KFunction0<T>, placeholder: Int, isAddToBackstack: Boolean = false) {
    val supportFragmentManager = supportFragmentManager
    val newInstance: T = fragment()
    val transaction = supportFragmentManager.beginTransaction()
            .replace(placeholder, newInstance, newInstance.TAG)

    if (isAddToBackstack) {
        transaction.addToBackStack(newInstance.TAG)
    }

    transaction.commit()
}

interface InjectablePresenter : Presenter {

    fun inject(receiver: InjectablePresenter) {
        when (receiver) {
            is NewsFragment -> {
                DaggerNewsListComponent
                        .builder()
                        .appComponent((receiver.activity.application as AppContext).appComponent)
                        .presenterModule(PresenterModule(receiver))
                        .build().inject(receiver)
            }
            else -> throw Exception("There is no such component registered.")
        }
    }
}


//interface InjectablePresenter

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }

val CompositeDisposable.isEmpty: Boolean get() = this.size() == 0
