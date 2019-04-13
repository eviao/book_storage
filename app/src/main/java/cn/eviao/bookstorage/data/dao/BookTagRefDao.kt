package cn.eviao.bookstorage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import cn.eviao.bookstorage.model.BookTagRef
import io.reactivex.Single

@Dao
interface BookTagRefDao {
    @Insert
    fun insert(refs: List<BookTagRef>): Single<List<Long>>
}