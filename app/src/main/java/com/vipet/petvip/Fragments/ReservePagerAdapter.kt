package com.vipet.petvip.Fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.vipet.petvip.Fragments.SelectTime.SelectTimeFragment

private val TAB_TITLES = arrayOf(
    "반려동물 선택",
    "전문가 선택",
    "시간 선택"
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager, service: Int) :
    FragmentPagerAdapter(fm) {

    val fragments = ArrayList<Fragment>()

    init {
        fragments.add(SelectPetFragment(service))
        fragments.add(SelectTimeFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}