package dv.serg.news.model.dao.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import dv.serg.lib.dao.OrdinalKey

@Entity(tableName = "source")
data class Source(
        @PrimaryKey(autoGenerate = false)
        override val id: Long,
        @ColumnInfo(name = "source_name")
        val sourceName: String,
        @ColumnInfo(name = "source_code")
        val sourceCode: String,
        @ColumnInfo(name = "source_description")
        val description: String
) : OrdinalKey {
    @Ignore
    var isSubscribed: Boolean = false
}

