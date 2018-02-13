package dv.serg.news.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.MenuItem
import android.view.View
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.lib.collection.ObservableList
import dv.serg.news.R
import dv.serg.news.RecordType
import dv.serg.news.model.dao.room.entity.Record
import dv.serg.news.ui.abstr.ListActivity
import dv.serg.news.ui.abstr.MvpView
import dv.serg.news.ui.presenter.RecordPresenter
import dv.serg.news.ui.viewholder.RecordHolder
import dv.serg.news.util.SwipeManager
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.app_bar_list.*
import kotlinx.android.synthetic.main.content_list.*
import kotlinx.android.synthetic.main.empty_window.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import javax.inject.Inject

class RecordActivity : ListActivity(), MvpView<Record>, ObservableList.ListObserver<Record> {
    override fun observeChanges(oldData: List<Record>, newData: List<Record>) {
        if (newData.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            nothing_to_show.visibility = View.GONE
            fab.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.GONE
            nothing_to_show.visibility = View.VISIBLE
            fab.visibility = View.GONE
        }
    }


    private lateinit var standardAdapter: StandardAdapter<Record, RecordHolder>

    @Inject
    lateinit var presenter: RecordPresenter<Record>

    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
//        presenter.dao.insert()

        standardAdapter = StandardAdapter(R.layout.history_item, { view -> RecordHolder(view) }).also {
            it.addObserver(this)
        }
//
        recyclerView.apply {
            this.adapter = standardAdapter
            layoutManager = LinearLayoutManager(this@RecordActivity, LinearLayoutManager.VERTICAL, false)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)

        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_delete_sweep_24dp))


        fab.setOnClickListener {
            Log.d("sergdv", "fab:click")
            val restoreList = ArrayList(standardAdapter)
            standardAdapter.clear()
            doAsync {
                presenter.dao.deleteAll()
                uiThread {
                    Snackbar.make(parentList, getString(R.string.all_remove_items), Snackbar.LENGTH_LONG).setAction("Dismiss") {
                        standardAdapter.addAll(restoreList)
                        doAsync {
                            presenter.dao.insertAll(restoreList)
                        }
                    }.show()
                }
            }
        }

        swipeRefresh.isEnabled = false
        swipeRefresh.isRefreshing = false

        SwipeManager(ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT), object : SwipeManager.SwipeCallback {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val position = viewHolder!!.adapterPosition
                val removeItem: Record = standardAdapter.removeAt(position)
                doAsync {
                    presenter.dao.delete(removeItem)
                    uiThread {
                        Snackbar.make(parentList, "Item deleted", Snackbar.LENGTH_LONG).setAction("Dismiss") {
                            standardAdapter.add(position, removeItem)
                            doAsync {
                                presenter.dao.insert(removeItem)
                            }
                        }.show()
                    }
                }
            }
        }).attachToRecycler(recyclerView)

        val navView = findViewById<NavigationView>(R.id.nav_view)

        if (intent?.extras?.getInt(RecordType.RECORD) == RecordType.HISTORY) {
            navView.inflateMenu(R.menu.history_menu)
            supportActionBar?.title = getString(R.string.history)
        } else if (intent?.extras?.getInt(RecordType.RECORD) == RecordType.BOOKMARKS) {
            navView.inflateMenu(R.menu.bookmark_menu)
            supportActionBar?.title = getString(R.string.bookmark)
        }

        nav_view.setNavigationItemSelectedListener(this)


        presenter.loadData()


    }

    override fun onStartLoading() {
    }

    override fun show(data: List<Record>) {
        standardAdapter.addAll(data)

//        checkAdapter()
    }

    override fun showError(message: Throwable) {
    }

    override fun onFinishLoading() {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("sergdv", "onNavigationItemSelected")
        when (item.itemId) {
            R.id.news_view -> {
                val intent: Intent = Intent(this, NewsActivity::class.java)
//                intent.putExtra(RecordType.RECORD, RecordType.HISTORY)

                startActivity(intent)
            }
            R.id.history_view -> {
                val intent: Intent = Intent(this, RecordActivity::class.java)
                intent.putExtra(RecordType.RECORD, RecordType.HISTORY)

                startActivity(intent)
            }
            R.id.bookmark_view -> {
                val intent: Intent = Intent(this, RecordActivity::class.java)
                intent.putExtra(RecordType.RECORD, RecordType.BOOKMARKS)

                startActivity(intent)
            }
            R.id.source_view -> {
                Log.d("sergdv", "nav_share")

                val intent: Intent = Intent(this, SourceActivity::class.java)

                startActivity(intent)

//                toast("nav_share")

            }
            R.id.nav_send -> {
                val intent: Intent = Intent(this, SecretActivity::class.java)

                startActivity(intent)
//                toast("nav_send")
            }
        }

        return true
//        return super.onNavigationItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, NewsActivity::class.java))
    }

//    private fun checkAdapter() {
//        if (standardAdapter.isEmpty()) {
//            setNothingVisible()
//        } else {
//            setDataVisible()
//        }
//    }

//    private fun setDataVisible() {
//        recyclerView.visibility = View.VISIBLE
//        nothing_to_show.visibility = View.GONE
//    }
//
//    private fun setNothingVisible() {
//        recyclerView.visibility = View.GONE
//        nothing_to_show.visibility = View.VISIBLE
//    }

}