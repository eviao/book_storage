package cn.eviao.bookstorage.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.os.AsyncTask
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.adapter.BookAdapter
import cn.eviao.bookstorage.model.Book
import cn.eviao.bookstorage.ui.RecycleViewDivider

import kotlinx.android.synthetic.main.activity_book_list.*
import kotlinx.android.synthetic.main.content_book_list.*

class LoadMoreTask(
    val refresh: BGARefreshLayout,
    val adapter: BookAdapter
) : AsyncTask<String, Void, List<Book>>() {

    override fun doInBackground(vararg params: String): List<Book> {
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return listOf(
            Book(title = "title 6", subtitle = "subtitle 1", rating = 1.1, isbn = "isbn 1", image = "https://img3.doubanio.com/view/subject/l/public/s4510534.jpg")
        )
    }

    override fun onPostExecute(result: List<Book>) {
        refresh.endLoadingMore()
        adapter.addDatas(result)
    }
}

class BookListActivity : AppCompatActivity(), BGARefreshLayout.BGARefreshLayoutDelegate {

    private lateinit var listAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)
        setSupportActionBar(toolbar)

        initRefreshLayout()
        initRVList()
    }

    private fun initRefreshLayout() {
        refresh.setDelegate(this)
        refresh.setRefreshViewHolder(BGANormalRefreshViewHolder(this, true))
    }

    private fun initRVList() {

        val data = mutableListOf<Book>(
            Book(title = "title 1", subtitle = "subtitle 1", rating = 1.1, isbn = "isbn 1", image = "https://img3.doubanio.com/view/subject/l/public/s4510534.jpg"),
            Book(title = "title 2", subtitle = "subtitle 2", rating = 1.2, isbn = "isbn 2", image = "https://img3.doubanio.com/view/subject/l/public/s4510534.jpg"),
            Book(title = "title 3", subtitle = "subtitle 3", rating = 1.3, isbn = "isbn 3", image = "https://img3.doubanio.com/view/subject/l/public/s4510534.jpg"),
            Book(title = "title 4", subtitle = "subtitle 4", rating = 1.4, isbn = "isbn 4", image = "https://img3.doubanio.com/view/subject/l/public/s4510534.jpg"),
            Book(title = "title 5", subtitle = "subtitle 5", rating = 1.5, isbn = "isbn 5", image = "https://img3.doubanio.com/view/subject/l/public/s4510534.jpg")
        )

        rv_list.layoutManager = LinearLayoutManager(this)

        listAdapter = BookAdapter(this, data)

        rv_list.adapter = listAdapter
        rv_list.addItemDecoration(
            RecycleViewDivider(
                this,
                LinearLayoutManager.VERTICAL,
                10,
                getColor(R.color.colorGray)
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_book_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout?): Boolean {
        LoadMoreTask(refresh, listAdapter).execute()
        return true
    }

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout?) {
    }
}
