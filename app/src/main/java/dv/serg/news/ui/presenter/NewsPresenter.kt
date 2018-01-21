package dv.serg.news.ui.presenter

import dv.serg.news.model.rest.abstr.NewsDao
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.ui.abstr.MvpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NewsPresenter(private val view: MvpView<Article>, private val remoteDao: NewsDao<String, Article>) {

    private var compositeSubscription: CompositeDisposable = CompositeDisposable()

    fun loadData(page: Int) {
        view.onStartLoading()
        val subscription = remoteDao.loadNewsRx(page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .cache()
                .subscribe({ it: List<Article> -> view.show(it) }, { view.showError(it) }, { view.onFinishLoading() })

        compositeSubscription.add(subscription)
    }

    fun detachSubscription() {
        compositeSubscription.clear()

    }
}