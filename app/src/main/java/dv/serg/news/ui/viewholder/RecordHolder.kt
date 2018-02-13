package dv.serg.news.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.dao.room.entity.Record
import dv.serg.news.util.convertTimestamp

class RecordHolder(view: View) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Record, RecordHolder>, StandardAdapter.OnClickListener<History> {

    private val root: View = view.findViewById(R.id.historyRoot)
    private val description: TextView = view.findViewById(R.id.description)
    private val header: TextView = view.findViewById(R.id.header)
    private val sourceName: TextView = view.findViewById(R.id.sourceName)
    private val publishedAt: TextView = view.findViewById(R.id.publishedAt)
    private val publishLabel: TextView = view.findViewById(R.id.publishLabel)


    override fun onBind(position: Int, item: Record) {
        description.text = item.shortDesc
        header.text = item.title
        sourceName.text = item.sourceName
        publishedAt.text = convertTimestamp(item.publishedAt)
    }
}