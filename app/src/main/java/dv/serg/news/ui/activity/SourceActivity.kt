package dv.serg.news.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.DrawerLayout
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.lib.collection.ObservableList
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.ui.abstr.ListActivity
import dv.serg.news.ui.presenter.SourcePresenter
import dv.serg.news.ui.viewholder.SourceHolder
import dv.serg.news.util.clearSelected
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.empty_window.*
import kotlinx.android.synthetic.main.ordinal_toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class SourceActivity : ListActivity(), ActionMode.Callback, ObservableList.ListObserver<Source> {
    override fun observeChanges(oldData: List<Source>, newData: List<Source>) {
        if (newData.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            nothing_to_show.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            nothing_to_show.visibility = View.VISIBLE
        }
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }

    companion object {
        val TAG = "sergdv"
    }



    private lateinit var adapter: StandardAdapter<Source, SourceHolder>
    private var actionMode: ActionMode? = null

    @Inject
    lateinit var presenter: SourcePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        supportActionBar?.title = getString(R.string.source)

        swipeRefresh.isEnabled = false
        swipeRefresh.isRefreshing = false

        findViewById<DrawerLayout>(R.id.drawer_layout).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE

        toolbar.visibility = View.VISIBLE

        adapter = StandardAdapter(R.layout.source_item,
                { view ->
                    SourceHolder(view) {
                        val isAtLeastOne = adapter.any { it.isSubscribed == true }
                        handleActionMode(isAtLeastOne)
                    }
                }).also {
            it.addObserver(this)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.addAll(presenter.sources)

    }

    private fun getSelectedCount(): Int = adapter.count { it.isSubscribed }

    private fun handleActionMode(isAtLeastOne: Boolean) {
        if (isAtLeastOne && actionMode == null) {
            actionMode = startSupportActionMode(this)
        }

        if (!isAtLeastOne) {
            actionMode?.finish()
        }

        actionMode?.title = "Selected: ${getSelectedCount()}"
    }

    override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
        when (p1?.itemId) {
            R.id.action_submit -> {
                doAsync {
                    presenter.dao.deleteAll()
                    presenter.dao.insertAll(adapter.filter { it.isSubscribed })
                    uiThread {
                        adapter.clearSelected()
                        actionMode?.finish()

                        startNewsActivity()
                    }
                }
            }
        }

        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, NewsActivity::class.java))
    }

    override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        p0?.menuInflater?.inflate(R.menu.source_menu, p1)

        return true
    }

    override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
        return false
    }

    override fun onDestroyActionMode(p0: ActionMode?) {
        adapter.clearSelected()
        actionMode = null
    }

    private fun startNewsActivity() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }
}


