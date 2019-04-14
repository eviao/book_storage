package cn.eviao.bookstorage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.databinding.FragmentBookDetailCatalogBinding
import cn.eviao.bookstorage.model.Book

private const val ARG_BOOK = "book"

class BookDetailCatalogFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentBookDetailCatalogBinding>(
            inflater, R.layout.fragment_book_detail_catalog, container, false)
        binding.book = arguments?.getParcelable(ARG_BOOK)
        return binding.root
    }

    companion object {
        fun newInstance(book: Book) =
            BookDetailCatalogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BOOK, book)
                }
            }
    }
}
