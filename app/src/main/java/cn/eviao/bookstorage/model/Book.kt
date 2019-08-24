package cn.eviao.bookstorage.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "books",
    indices = [
        Index(value = ["title"]),
        Index(value = ["isbn"], unique = true)
    ],
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Box::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("box_id"),
            onDelete = ForeignKey.SET_NULL
        )
    )
)
@Parcelize
data class Book(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val title: String? = null,

    val subtitle: String? = null,

    val originTitle: String? = null,

    val isbn: String? = null,

    val pubdate: String? = null,

    val image: String? = null,

    val rating: Double? = null,

    val catalog: String? = null,

    val summary: String? = null,

    val authors: String? = null,

    val publisher: String? = null,

    val tags: String? = null,

    @ColumnInfo(name = "box_id")
    val boxId: Long? = null
) : Identifiable, Parcelable {

    override fun identity(): Long? = id

    fun isNew() = (id == null || id == 0L)
}