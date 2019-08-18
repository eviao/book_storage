package cn.eviao.bookstorage.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(
    tableName = "boxs",
    indices = [ Index(value = ["name"]) ]
)
@Parcelize
data class Box(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val name: String? = null,

    val intro: String? = null
) : Identifiable, Parcelable {

    override fun identity(): Long? = id
}