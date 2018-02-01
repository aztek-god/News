package dv.serg.news.ui.presenter

import dv.serg.lib.dao.Dao
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.ui.abstr.MvpView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HistoryPresenter(val view: MvpView<History>, val dao: Dao<History>) {
    fun loadData() {
        view.onStartLoading()
        dao.getAll().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe({ view.show(it) },
                        { view.showError(it) },
                        { view.onFinishLoading() })
    }
}