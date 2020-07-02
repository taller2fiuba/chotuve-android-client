package com.taller2.chotuve.vista.principal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.taller2.chotuve.R

class PrincipalFragment : Fragment() {
    companion object {
        fun newInstance(): PrincipalFragment =
            PrincipalFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_container, container, false)
        val muroDeVideosFragment = MuroDeVideosFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, muroDeVideosFragment)
        transaction.commit()
        return view
    }

    //fun scrollTop() {
    //    val muroDeVideosFragment = activity!!.supportFragmentManager.findFragmentByTag(MURO_VIDEOS_TAG) as MuroDeVideosFragment
    //    if (muroDeVideosFragment.isVisible) {
    //        muroDeVideosFragment.scrollTop()
    //    }
    //}
}
