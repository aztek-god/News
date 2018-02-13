package dv.serg.news.ui.presenter

import android.util.Log
import dv.serg.lib.dao.Dao
import dv.serg.news.model.dao.room.entity.Bookmark
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.model.rest.pojo.Response
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.util.convertToBookmark
import dv.serg.news.util.convertToHistory
import dv.serg.news.util.isInternetActive
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// todo implement attach detach
class NewsPresenter(private val view: MvpView<Article>,
                    private val retrofit: Retrofit,
                    private val sourceDao: Dao<Source>,
                    private val historyDao: Dao<History>,
                    private val bookmarkDao: Dao<Bookmark>,
                    val filterList: MutableList<String>) {


    class NoInternetException(message: String) : Exception(message)

    companion object {
        val TAG = "NewsPresenter"
    }

    var currentPage: Int = 1
    private val isSearch: Boolean get() = !searchQuery.isBlank()
    var searchQuery: String = ""
        set(value) {
            currentPage = 1
            field = value
        }

    // todo rewrite it to using of key interface
    fun addUrlToFilter(url: String) {
        filterList.add(url)
    }

    fun removefromFilter(url: String) {
        filterList.remove(url)
    }

    fun saveAsHistory(article: Article) {
        val history: History = convertToHistory(article)
        Log.d("sergdv", "saveAsHistory = $history")
        doAsync {
            historyDao.insert(history)
        }
    }

    fun saveAsBookmark(article: Article) {
        val history: Bookmark = convertToBookmark(article)
        doAsync {
            bookmarkDao.insert(history)
        }
    }

    fun deleteHistory(article: Article) {
        val history: History = convertToHistory(article)
        doAsync {
            historyDao.delete(history)
        }
    }


    fun loadDataFromLentaOnly() {
        view.onStartLoading()
        retrofit.create(News::class.java).loadPopularNews(searchQuery, "lenta_thumb", currentPage++)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .cache()
                .map { it: Response -> it.articles ?: emptyList() }
                .subscribe(
                        {
                            Observable
                                    .fromIterable(it)
                                    .filter { !filterList.contains(it.url) }
                                    .distinct()
                                    .buffer(500L, TimeUnit.MILLISECONDS, 20)
                                    .subscribe {
                                        //                                        liveData.articles?.addAll(it)
                                        view.show(it)
                                    }
                        }, { view.showError(it) }, { view.onFinishLoading() }
                )
    }

    fun loadNewsFromSources() {
        view.onStartLoading()
        if (isInternetActive) {
            doAsync {
                val sources = sourceDao.getAll()
                val sourcesString: String

                sourcesString = if (sources.isEmpty()) {
                    "lenta"
                } else {
                    sources.joinToString(separator = ",", transform = { it: Source ->
                        it.sourceCode
                    })
                }
                uiThread {
                    retrofit.create(News::class.java).loadPopularNews(searchQuery, sourcesString, currentPage++)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .flatMap {
                                Observable.just(it.articles)
                            }.subscribe({ articles: List<Article> -> view.show(articles) },
                                    { view.showError(it) },
                                    { view.onFinishLoading() })
                }
            }
        } else {
            Log.d("sergdv:loadNewsFromSo", "loadNewsFromSources")
            view.showError(NoInternetException("There is no active internet connections."))
        }
    }
}

//private class AdapterLiveData : ViewModel() {
//    var articles: MutableList<Article>? = null
//}

private interface News {
    @GET("everything?sortBy=publishedAt")
    fun loadPopularNews(@Query("q") query: String, @Query("sources") sources: String, @Query("page") page: Int): Observable<Response>
}
