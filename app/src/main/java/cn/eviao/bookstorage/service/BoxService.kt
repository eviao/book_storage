package cn.eviao.bookstorage.service

import androidx.paging.Config
import androidx.paging.toLiveData
import cn.eviao.bookstorage.model.Box
import cn.eviao.bookstorage.persistence.BoxDao
import cn.eviao.bookstorage.persistence.DataSource
import io.reactivex.Completable
import io.reactivex.Single

class BoxService(private val dataSource: DataSource) {

    private val boxDao: BoxDao = dataSource.boxDao()

    private val pagingConfig = Config(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    )

    fun add(box: Box): Single<Long> {
        return boxDao.insert(box)
    }

    fun update(box: Box): Completable {
        return boxDao.update(box)
    }

    fun delete(box: Box): Completable {
        return boxDao.delete(box)
    }

    fun loadBy(id: Long) = boxDao.loadBy(id)

    fun loadAll() = boxDao.loadAll().toLiveData(pagingConfig)
}