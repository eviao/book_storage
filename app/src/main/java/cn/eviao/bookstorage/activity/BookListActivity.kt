package cn.eviao.bookstorage.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.adapter.BookListAdapter
import cn.eviao.bookstorage.databinding.ActivityBookListBinding
import cn.eviao.bookstorage.viewmodel.BookListViewModel

import kotlinx.android.synthetic.main.activity_book_list.*
import kotlinx.android.synthetic.main.content_book_list.*

class BookListActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<ActivityBookListBinding>(this, R.layout.activity_book_list)
    }
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this).get(BookListViewModel::class.java)
    }

    private lateinit var bookListAdapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        initBinding()
        initBookList()
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initBookList() {
        bookListAdapter = BookListAdapter(this)

        rv_bookList.adapter = bookListAdapter
        rv_bookList.layoutManager = LinearLayoutManager(this)

        viewModel.allBooks.observe(this, Observer(bookListAdapter::submitList))
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

}
