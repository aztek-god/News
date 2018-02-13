package dv.serg.news.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.Source

class SourceHolder(private val view: View, private val action: (Source) -> Unit = {}) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Source, SourceHolder> {
    private val root: View = view.findViewById(R.id.sourceRoot)
    private val sourceName: TextView = view.findViewById(R.id.header)
    private val description: TextView = view.findViewById(R.id.descr)
    private val icon: ImageView = view.findViewById(R.id.icon)
    private val checkBox: CheckBox = view.findViewById(R.id.checkBox)


    override fun onBind(position: Int, item: Source) {
        this.sourceName.text = item.sourceName
        this.description.text = item.description

        root.setOnClickListener {
            item.isSubscribed = !item.isSubscribed
            checkBox.isChecked = item.isSubscribed
            action.invoke(item)
        }

        val resources = view.context?.resources
        val sourceCode = item.sourceCode.replace("-", "_")
        val identifier = resources?.getIdentifier(sourceCode,
                "drawable", view.context?.packageName)

        icon.setImageResource(identifier
                ?: throw Exception("Cannot handle resource. Wrong resource identifier."))

        checkBox.isChecked = item.isSubscribed
    }
}