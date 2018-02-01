package dv.serg.news.ui.presenter

import android.util.Log
import dv.serg.lib.dao.Dao
import dv.serg.news.model.dao.room.entity.Bookmark
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.model.rest.pojo.Response
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.util.convertToBookmark
import dv.serg.news.util.convertToHistory
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// todo implement attach detach
class NewsPresenter(private val view: MvpView<Article>,
                    private val retrofit: Retrofit,
                    private val sourceDao: Flowable<List<String>>,
                    private val historyDao: Dao<History>,
                    private val bookmarkDao: Dao<Bookmark>,
                    val filterList: MutableList<String>) {


//    init {
//        if (liveData.articles == null) {
//            Log.d("sergdv", "liveData.articles")
//            liveData.articles = ArrayList()
//        }
//    }
//
//    fun retainArticles(): List<Article> = liveData.articles ?: emptyList()


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
        retrofit.create(News::class.java).loadPopularNews(searchQuery, "lenta", currentPage++)
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
        sourceDao.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe { it: List<String> ->
                    val sources: String = it.joinToString(separator = ",")
                    // todo replace with page var
                    retrofit.create(News::class.java).loadPopularNews(searchQuery, sources, currentPage++)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .map { response: Response -> response.articles ?: emptyList() }
                            .subscribe(
                                    { view.show(it) }, { view.showError(it) }, { view.onFinishLoading() }
                            )
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
