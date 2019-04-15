package cn.eviao.bookstorage.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cn.eviao.bookstorage.R
import cn.eviao.bookstorage.databinding.FragmentBookDetailBasicBinding
import cn.eviao.bookstorage.model.Book
import java.util.ArrayList

private const val ARG_BOOK = "book"
private const val ARG_TAGS = "tags"

class BookDetailBasicFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentBookDetailBasicBinding>(
            inflater, R.layout.fragment_book_detail_basic, container, false)
        binding.book = arguments?.getParcelable(ARG_BOOK)
        binding.tags = arguments?.getStringArrayList(ARG_TAGS)
        return binding.root
    }

    companion object {
        fun newInstance(book: Book, tags: ArrayList<String>) =
            BookDetailBasicFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_BOOK, book)
                    putStringArrayList(ARG_TAGS, tags)
                }
            }
    }
}
