package com.taller2.chotuve.vista.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.taller2.chotuve.R

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : Fragment() {
    companion object {
        fun newInstance(): ChatsFragment =
            ChatsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

}
