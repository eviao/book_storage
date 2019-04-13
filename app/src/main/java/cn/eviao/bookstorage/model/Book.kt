package cn.eviao.bookstorage.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["title"]),
        Index(value = ["isbn"], unique = true)
    ]
)
@Parcelize
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val subtitle: String? = null,
    val originTitle: String? = null,

    val isbn: String,
    val pubdate: String? = null,
    val image: String? = null,

    val rating: Double? = null,
    val catalog: String? = null,
    val summary: String? = null,

    val author: String? = null,
    val publisher: String? = null
) : Parcelable {

    fun isNew(): Boolean {
        return id == 0L
    }
}