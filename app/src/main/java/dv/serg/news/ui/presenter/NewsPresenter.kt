package dv.serg.news.ui.presenter

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import dv.serg.news.model.rest.abstr.NewsDao
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.util.InjectablePresenter
import dv.serg.news.util.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NewsPresenter(private val view: MvpView<Article>, private val remoteDao: NewsDao<String, Article>) : InjectablePresenter, Presenter {

    private var compositeSubscription: CompositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null

    fun loadData(page: Int) {
        view.onStartLoading()

        val activity: FragmentActivity = (view as Fragment).activity
        val persistData: PersistData = ViewModelProviders.of(activity).get(PersistData::class.java)
        if (disposable == null) {
            if (persistData.articles != null) {
                view.show(persistData.articles ?: emptyList())
            } else {
                disposable = remoteDao.loadNewsRx(page)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .cache()
                        .subscribe({ it: List<Article> ->
                            persistData.articles = it
                            view.show(it)
                        }, { view.showError(it) }, { view.onFinishLoading() })
            }
        } else {
            compositeSubscription.add(disposable)
        }
    }

    private class PersistData : ViewModel() {
        var articles: List<Article>? = null
    }

    override fun attach() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detach() {
        Log.d(TAG, "NewsPresenter:detach")
        compositeSubscription.clear()
    }

}