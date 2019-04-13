package cn.eviao.bookstorage.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabPagerAdapter(
    fragmentManager: FragmentManager,
    val items: List<Pair<String, Fragment>>
) : FragmentPagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return items.count()
    }

    override fun getItem(position: Int): Fragment {
        return items.get(position).second
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return items.get(position).first
    }
}