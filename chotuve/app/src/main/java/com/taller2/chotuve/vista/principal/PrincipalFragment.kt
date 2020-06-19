package com.taller2.chotuve.vista.principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taller2.chotuve.R

class PrincipalFragment : Fragment() {
    private val MURO_VIDEOS_TAG =  "MURO_VIDEOS_TAG"

    companion object {
        fun newInstance(): PrincipalFragment =
            PrincipalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_principal, container, false)
        val muroDeVideosFragment = MuroDeVideosFragment()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, muroDeVideosFragment, MURO_VIDEOS_TAG)
        transaction.addToBackStack(null)
        transaction.commit()
        return view
    }

    fun scrollTop() {
        val muroDeVideosFragment = activity!!.supportFragmentManager.findFragmentByTag(MURO_VIDEOS_TAG) as MuroDeVideosFragment
        if (muroDeVideosFragment.isVisible) {
            muroDeVideosFragment.scrollTop()
        }
    }
}
