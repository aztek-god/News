package dv.serg.news.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.ui.abstr.ListActivity
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.HistoryPresenter
import dv.serg.news.ui.viewholder.HistoryHolder
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.ordinal_toolbar.*
import javax.inject.Inject

class HistoryActivity : ListActivity(), MvpView<History> {

    private lateinit var standardAdapter: StandardAdapter<History, HistoryHolder>

    @Inject
    lateinit var presenter: HistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // todo to method
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE
        // todo -------------------

        standardAdapter = StandardAdapter(R.layout.history_item, { view -> HistoryHolder(view) })

        recyclerView.apply {
            this.adapter = standardAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity, LinearLayoutManager.VERTICAL, false)
        }

        presenter.loadData()

    }

    override fun onStartLoading() {
    }

    override fun show(data: List<History>) {
        standardAdapter.addAll(data)
    }

    override fun showError(message: Throwable) {
    }

    override fun onFinishLoading() {
    }
}