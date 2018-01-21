package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.news.di.PerFragment
import dv.serg.news.model.rest.abstr.NewsDao
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.model.rest.pojo.Response
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

@Module(includes = [(RetrofitModule::class)])
class NewsApiModule {

    @PerFragment
    @Provides
    fun provideApi(retrofit: Retrofit): NewsDao<String, Article> {
        val newsApi: News = retrofit.create(News::class.java)

        return object : NewsDao<String, Article> {

            override fun loadNewsRx(page: Int): Observable<List<Article>> {
                return newsApi.loadPopularNews(query, sourcesUrl(), page, pageSize)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .map { it.articles }
            }

            override fun loadNews(page: Int): List<Article> {
                throw UnsupportedOperationException("The operation temporarily not supported.")
            }

            override fun getById(id: String): Article {
                throw UnsupportedOperationException("The operation temporarily not supported.")
            }

            override fun getAll(): List<Article> {
                throw UnsupportedOperationException("The operation temporarily not supported.")
            }

            override var query: String = DEFAULT_QUERY

            override var pageSize: Int = DEFAULT_PAGE_SIZE

            override var sources: List<String> = DEFAULT_SOURCES

        }
    }

    private interface News {
        @GET("everything?sortBy=publishedAt")
        fun loadPopularNews(@Query("q") query: String, @Query("sources") sources: String, @Query("page") page: Int,
                            @Query("pageSize") pageSize: Int): Observable<Response>
    }

    companion object {
        private const val DEFAULT_QUERY = ""
        private const val DEFAULT_PAGE_SIZE = 20
        private val DEFAULT_SOURCES = listOf("lenta")
    }
}