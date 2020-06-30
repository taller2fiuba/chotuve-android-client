package com.taller2.chotuve.vista.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.taller2.chotuve.R


class PerfilFragment(val usuarioId: Long?) : Fragment() {
    companion object {
        fun newInstance(usuarioId: Long?): PerfilFragment =
            PerfilFragment(usuarioId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perfil, container, false) as View

        var pager = view.findViewById(R.id.perfil_view_pager) as ViewPager
        setupViewPager(pager)

        val toolbar = view.findViewById(R.id.perfil_toolbar) as TabLayout
        toolbar.setupWithViewPager(pager)

        return view
    }

    private fun setupViewPager(viewPager : ViewPager) {
        val viewPagerAdapter =
            ViewPagerAdapter(
                childFragmentManager
            )
        viewPagerAdapter.addFragment(InformacionFragment.newInstance(usuarioId), "información")
        viewPagerAdapter.addFragment(VideosFragment.newInstance(usuarioId), "videos")
        // contactos solo en mi perfil
        // TODO ver si se pueden ver contactos de contactos o de todos
        if (usuarioId == null) {
            viewPagerAdapter.addFragment(ContactosFragment.newInstance(), "contactos")
        }
        viewPager.adapter = viewPagerAdapter
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = ArrayList<Fragment>()
        private val fragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}
