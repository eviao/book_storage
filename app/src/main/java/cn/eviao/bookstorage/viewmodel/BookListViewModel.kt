package cn.eviao.bookstorage.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Config
import androidx.paging.toLiveData
import cn.eviao.bookstorage.data.AppDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class BookListViewModel(app: Application) : AndroidViewModel(app) {
    private val bookDao = AppDatabase.get(app).bookDao()

    val booksCount = MutableLiveData<Int>()

    val allBooks = bookDao.loadAll().toLiveData(Config(
        pageSize = 20,
        enablePlaceholders = true,
        maxSize = 200
    ))

    fun loadBooksCount(): Flowable<Int> {
        return bookDao.count().map {
            booksCount.postValue(it)
            it
        }
    }
}