package cn.eviao.bookstorage.persistence

import androidx.room.*
import androidx.paging.DataSource
import cn.eviao.bookstorage.model.Box
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BoxDao {

    @Insert
    fun insert(box: Box): Single<Long>

    @Update
    fun update(box: Box): Completable

    @Delete
    fun delete(box: Box): Completable

    @Query("select * from boxs where id = :id")
    fun loadBy(id: Long): Maybe<Box>

    @Query("select * from boxs order by id desc")
    fun loadAll(): DataSource.Factory<Int, Box>
}