package dv.serg.news.model.dao.room.entity

interface Record {
    val id: Long
    val sourceName: String
    val title: String
    val shortDesc: String
    val url: String
    val publishedAt: Long
    val addedAt: Long
}