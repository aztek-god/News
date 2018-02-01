package dv.serg.news.ui.viewholder

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.Source

class SourceHolder(view: View, private val action: (Source) -> Unit = {}) : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Source, SourceHolder> {
    private val root: View = view.findViewById(R.id.historyRoot)
    private val sourceName: TextView = view.findViewById(R.id.sourceName)
    private val description: TextView = view.findViewById(R.id.description)
    private val notification: ImageView = view.findViewById(R.id.notification)

    private val notificationActive: Drawable = ContextCompat.getDrawable(view.context, R.drawable.ic_notifications_24dp)
    private val notificationPassive: Drawable = ContextCompat.getDrawable(view.context, R.drawable.ic_notifications_none_24dp)

    override fun onBind(position: Int, item: Source) {
        this.sourceName.text = item.sourceName
        this.description.text = item.description

        root.setOnClickListener {
            if (item.isSubscribed) {
                notification.setImageDrawable(notificationPassive)
                item.isSubscribed = false
            } else {
                notification.setImageDrawable(notificationActive)
                item.isSubscribed = true
            }
            action.invoke(item)
        }

        if (item.isSubscribed) {
            notification.setImageDrawable(notificationActive)
        } else {
            notification.setImageDrawable(notificationPassive)
        }
    }
}