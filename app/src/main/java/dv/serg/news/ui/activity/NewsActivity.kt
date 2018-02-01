package dv.serg.news.ui.activity

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.ui.abstr.ListActivity
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.NewsPresenter
import dv.serg.news.ui.viewholder.NewsHolder
import dv.serg.news.util.PaginationScrollListener
import dv.serg.news.util.SwipeManager
import dv.serg.news.util.startBrowser
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.app_bar_list.*
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.search_toolbar.*
import javax.inject.Inject


class NewsActivity : ListActivity(), MvpView<Article> {


    @Inject
    lateinit var presenter: NewsPresenter
    private lateinit var adapter: StandardAdapter<Article, NewsHolder>

    private var isLoading: Boolean = false
    private lateinit var liveData: AdapterLiveData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list) //todo

        liveData = ViewModelProviders.of(this).get(AdapterLiveData::class.java)

        Log.d("sergdv", "onCreate")


        setSupportActionBar(searchToolbar)
        searchToolbar.visibility = View.VISIBLE

        searchToolbarView.attachNavigationDrawerToMenuButton(drawer_layout)

        SwipeManager(ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT),
                object : SwipeManager.SwipeCallback {
                    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                        val position = viewHolder!!.adapterPosition
                        adapter.removeAt(position)
                    }
                }).attachToRecycler(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0)
                    fab.hide()
                else if (dy < 0)
                    fab.show()
            }
        })

        swipeRefresh.setOnRefreshListener {
            adapter.clear()
            presenter.currentPage = 1
            presenter.loadDataFromLentaOnly()
        }

        fab.setOnClickListener {
            presenter.searchQuery = ""
            searchToolbarView.setSearchText("")
            adapter.clear()
            presenter.loadDataFromLentaOnly()
        }

        adapter = StandardAdapter(R.layout.news_item, { view ->
            NewsHolder(view, object : StandardAdapter.OnClickListener<Article> {
                override fun onClick(data: Article, position: Int) {
                    presenter.saveAsHistory(data)
                    startBrowser(data.url!!)
                }
            }).also {
                it.popupListener = object : NewsHolder.NewsPopupListener {
                    override fun onAddToBookmark(item: Article) {
                        Log.d("sergdv", "onAddToBookmark:item = $item")
                        presenter.saveAsBookmark(item)
                    }

                    override fun onAddToFilter(item: Article) {
                        presenter.addUrlToFilter(item.url ?: "")
                    }

                    override fun onCopyUrl(url: String) {
                        val data = ClipData.newPlainText("url", url)
                        val systemService = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        systemService.primaryClip = data
                    }
                }
            }
        })

        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                presenter.loadDataFromLentaOnly()
            }

            override fun getTotalPageCount(): Int = TOTAL_PAGE_COUNT

            override fun isLastPage(): Boolean = false

            override fun isLoading(): Boolean = isLoading
        })

        searchToolbarView.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {
                if (currentQuery?.isNotEmpty() == true) {
                    adapter.clear()
                    presenter.searchQuery = currentQuery
                    presenter.loadDataFromLentaOnly()
                }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
            }

        })


        if (savedInstanceState == null) {
            presenter.loadDataFromLentaOnly()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        liveData.articles = adapter
        liveData.currentSearchQuery = presenter.searchQuery
        liveData.currentPage = presenter.currentPage
        Log.d("sergdv", "onSaveInstanceState adapter size onSaveInstanceState = ${liveData.articles?.size
                ?: 0}")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.addAll(liveData.articles ?: emptyList())
        presenter.searchQuery = liveData.currentSearchQuery
        presenter.currentPage = liveData.currentPage
        Log.d("sergdv", "onRestoreInstanceState adapter size = ${adapter.size}")
    }

    override fun onStartLoading() {
        isLoading = true
        swipeRefresh.isRefreshing = true
    }

    override fun show(data: List<Article>) {
        adapter.addAll(data)
    }

    override fun showError(message: Throwable) {
        // todo
    }

    override fun onFinishLoading() {
        isLoading = false
        swipeRefresh.isRefreshing = false

    }

    private class AdapterLiveData : ViewModel() {
        var currentSearchQuery: String = ""
        var currentPage: Int = 1
        var articles: MutableList<Article>? = null
    }


    companion object {
        const val TAG = "NewsActivity"
        const val TOTAL_PAGE_COUNT = 20
    }
}