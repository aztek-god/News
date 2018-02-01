package dv.serg.news.model.rest.abstr

import io.reactivex.Observable

interface NewsDao<K : Comparable<K>, V> {

    var query: String
    var pageSize: Int
    var sources: List<String>

    fun loadNews(page: Int): List<V>

    fun loadNewsRx(page: Int): Observable<List<V>>

    fun sourcesUrl(): String {
        return sources.joinToString(separator = ",")
    }

    companion object {
        const val DEF_PAGE_SIZE = 20
    }
}