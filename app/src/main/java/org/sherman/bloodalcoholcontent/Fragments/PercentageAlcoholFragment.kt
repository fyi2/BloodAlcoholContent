package org.sherman.bloodalcoholcontent.Fragments


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.sherman.bloodalcoholcontent.R


/**
 * A simple [Fragment] subclass.
 */
class PercentageAlcoholFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percentage_alcohol, container, false)
    }

}// Required empty public constructor
