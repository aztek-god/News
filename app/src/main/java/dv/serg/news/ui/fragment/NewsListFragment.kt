package dv.serg.news.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.news.R
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.recycler.StandardAdapter
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.NewsPresenter
import dv.serg.news.ui.viewholder.ArticleHolder
import dv.serg.news.util.Injectable
import dv.serg.news.util.TAG
import javax.inject.Inject


class NewsListFragment : LiveFragment(), MvpView<Article>, Injectable, SwipeRefreshLayout.OnRefreshListener {


    private var parentContext: Context? = null
    private var adapter: StandardAdapter<Article, ArticleHolder>? = null
    private var recyclerView: RecyclerView? = null
    @Inject
    lateinit var presenter: NewsPresenter
    private var currentPage: Int = 1
    private var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
        Log.d(tag, "NewsListFragment:onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.d(TAG, "savedInstanceState:$savedInstanceState")
        return inflater!!.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(rootView: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        recyclerView = rootView!!.findViewById(R.id.recyclerView)
        adapter = StandardAdapter(R.layout.news_item, { view -> ArticleHolder(view) })
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(parentContext, LinearLayoutManager.VERTICAL, false)

        val swipeRefresh = (parentContext as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener(this)

        presenter.loadData(currentPage++)
    }

    override fun onStartLoading() {
        swipeRefresh?.isRefreshing = true
    }

    override fun show(data: List<Article>) {
        adapter?.appendData(data)
    }

    override fun showError(message: Throwable) {
    }

    override fun onFinishLoading() {
        swipeRefresh?.isRefreshing = false
    }


    override fun onAttach(context: Context?) {
        // todo make coordinator layout: https://stackoverflow.com/questions/30739806/coordinator-layout-with-toolbar-in-fragments-or-activity
        super.onAttach(context)
        parentContext = context
    }

    override fun onDetach() {
        super.onDetach()
        parentContext = null
    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(bundle: Bundle? = null): NewsListFragment {
            val fragment = NewsListFragment()
            if (bundle != null) {
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}
