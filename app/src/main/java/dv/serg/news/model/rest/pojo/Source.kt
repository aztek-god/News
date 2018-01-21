package dv.serg.news.model.rest.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Source(
        val orderId: Int,
        @SerializedName("id")
        @Expose
        val id: String?,
        @SerializedName("name")
        @Expose
        val name: String?
)