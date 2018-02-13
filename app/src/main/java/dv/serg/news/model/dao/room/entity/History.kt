package dv.serg.news.model.dao.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import dv.serg.news.util.getCurrentTimestamp

@Entity(tableName = "history")
data class History(
        @PrimaryKey(autoGenerate = true)
        override val id: Long,
        @ColumnInfo(name = "source_from")
        override val sourceName: String,
        @ColumnInfo(name = "header")
        override val title: String,
        @ColumnInfo(name = "short_desc")
        override val shortDesc: String,
        @ColumnInfo(name = "url")
        override val url: String,
        @ColumnInfo(name = "published_at")
        override val publishedAt: Long,
        @ColumnInfo(name = "visited_at")
        override val addedAt: Long = getCurrentTimestamp()
) : Record
