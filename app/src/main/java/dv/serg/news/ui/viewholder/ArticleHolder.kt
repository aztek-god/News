package dv.serg.news.ui.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dv.serg.news.R
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.recycler.StandardAdapter

class ArticleHolder(private val view: View) : RecyclerView.ViewHolder(view), StandardAdapter.AbstractViewHolder<Article> {

    private val title: TextView = view.findViewById(R.id.title)
    private val publishedAt: TextView = view.findViewById(R.id.publishedAt)
    private val description: TextView = view.findViewById(R.id.description)
    private val source: TextView = view.findViewById(R.id.source)
    private val previewImage: ImageView = view.findViewById(R.id.previewImage)

    override fun bind(item: Article, clickListener: (Int) -> Unit, longClickListener: (Int) -> Unit) {
        title.text = item.title
        publishedAt.text = item.publishedAt
        description.text = item.description
        source.text = item.source?.name
        Glide.with(view.context).load(item.urlToImage).into(previewImage)
    }
}