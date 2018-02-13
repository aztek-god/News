package dv.serg.news.ui.viewholder

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.R
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.util.convertDatetimeFromString
import dv.serg.news.util.convertDatetimeToHandyString

class NewsHolder(private val view: View, private val clickListener: StandardAdapter.OnClickListener<Article>)
    : RecyclerView.ViewHolder(view), StandardAdapter.BindViewHolder<Article, NewsHolder>, StandardAdapter.OnClickListener<Article> {
    private val root: View = view.findViewById(R.id.newsRoot)
    private val description: TextView = view.findViewById(R.id.description)
    private val publishedAt: TextView = view.findViewById(R.id.publishedAt)
    private val title: TextView = view.findViewById(R.id.title)
    private val sourceName: TextView = view.findViewById(R.id.sourceName)
    private val previewImage: ImageView = view.findViewById(R.id.previewImage)
    private val popupMenu: ImageView = view.findViewById(R.id.popupMenu)

    var popupListener: NewsPopupListener = object : NewsPopupListener {}

    interface NewsPopupListener {
        fun onAddToBookmark(item: Article) {}
        fun onAddToFilter(item: Article) {}
        fun onCopyUrl(url: String) {}
    }

    override fun onBind(position: Int, item: Article) {

        with(root) {
            setOnClickListener {
                clickListener.onClick(item, position)
            }
            setOnLongClickListener {
                clickListener.onLongClick(item, position)
                true
            }
        }

        popupMenu.setOnClickListener {
            PopupMenu(popupMenu.context, popupMenu).also {
                it.menuInflater.inflate(R.menu.popup_menu, it.menu)

                it.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.add_to_bookmark_menu -> {
                            popupListener.onAddToBookmark(item)
                            true
                        }
                        R.id.add_to_filter_menu -> {
                            popupListener.onAddToFilter(item)
                            true
                        }
                        R.id.copy_url_menu -> {
                            popupListener.onCopyUrl(item.url ?: "")
                            true
                        }
                        else -> false
                    }
                }

                it.show()
            }


        }

        description.text = item.description
//        convertDatetimeToHandyString


//        publishedAt.text = item.publishedAt

        publishedAt.text = convertDatetimeToHandyString(convertDatetimeFromString(item.publishedAt
                ?: ""))

        title.text = item.title
        sourceName.text = item.source?.name
        Glide.with(view.context).load(item.urlToImage).into(previewImage)
    }
}