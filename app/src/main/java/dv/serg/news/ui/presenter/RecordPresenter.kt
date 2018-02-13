package dv.serg.news.ui.presenter

import dv.serg.lib.dao.Dao
import dv.serg.news.model.dao.room.entity.Record
import dv.serg.news.ui.abstr.MvpView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class RecordPresenter<T : Record>(private val view: MvpView<Record>, val dao: Dao<T>) {

    fun loadData() {
        view.onStartLoading()
        doAsync {
            val data = dao.getAll()
            uiThread {
                Observable
                        .just(data)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe({ view.show(it) }, { view.showError(it) }, { view.onFinishLoading() })
            }
        }
    }

}