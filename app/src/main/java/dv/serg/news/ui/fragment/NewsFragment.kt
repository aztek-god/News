package dv.serg.news.ui.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dv.serg.news.R
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.recycler.StandardAdapter
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.NewsPresenter
import dv.serg.news.ui.viewholder.ArticleHolder
import dv.serg.news.util.InjectablePresenter


class NewsFragment : StandardFragment<NewsPresenter>(), MvpView<Article>, InjectablePresenter, SwipeRefreshLayout.OnRefreshListener {

    private var adapter: StandardAdapter<Article, ArticleHolder>? = null

    private var recyclerView: RecyclerView? = null

    private var swipeRefresh: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = StandardAdapter(R.layout.news_item, { view0 -> ArticleHolder(view0) })
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(parentContext, LinearLayoutManager.VERTICAL, false)

        val swipeRefresh = (parentContext as AppCompatActivity).findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener(this)

        // todo page

        presenter.loadData(1)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val fab = activity.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            // todo fab
        }
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

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
