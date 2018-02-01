package dv.serg.news.model.dao.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import dv.serg.news.util.getCurrentTimestamp


@Entity(tableName = "bookmark")
data class Bookmark(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        @ColumnInfo(name = "source_from")
        val sourceName: String,
        @ColumnInfo(name = "header")
        val title: String,
        @ColumnInfo(name = "short_desc")
        val shortDesc: String,
        @ColumnInfo(name = "url")
        val url: String,
        @ColumnInfo(name = "published_at")
        val publishedAt: Long,
        @ColumnInfo(name = "added_at")
        val addedAt: Long = getCurrentTimestamp()
)