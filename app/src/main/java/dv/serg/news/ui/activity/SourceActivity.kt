package dv.serg.news.ui.activity

import android.os.Bundle
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.ui.abstr.ListActivity
import dv.serg.news.ui.presenter.SourcePresenter
import dv.serg.news.ui.viewholder.SourceHolder
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.ordinal_toolbar.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import javax.inject.Inject

class SourceActivity : ListActivity(), ActionMode.Callback {

    companion object {
        val TAG = "sergdv"
    }

    // todo implement screen orientation surviving
    // todo

    private lateinit var adapter: StandardAdapter<Source, SourceHolder>
    private var actionMode: ActionMode? = null

    @Inject
    lateinit var presenter: SourcePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE

        adapter = StandardAdapter(R.layout.source_item,
                { view ->
                    SourceHolder(view) {
                        val isAtLeastOne = adapter.any { it.isSubscribed == true }
                        handleActionMode(isAtLeastOne)
                    }
                })

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

                        // todo start activity here
                    }
                }
            }
        }

        return true
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
}

fun StandardAdapter<Source, SourceHolder>.clearSelected() {
    forEach { it.isSubscribed = false }
    notifyDataSetChanged()
}
