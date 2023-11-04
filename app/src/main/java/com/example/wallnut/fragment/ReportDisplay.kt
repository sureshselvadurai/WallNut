package com.example.wallnut.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wallnut.R

/**
 * A fragment to display a report.
 */
class ReportDisplay : Fragment() {

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views.
     * @param container The parent view that this fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state if available.
     * @return The root view for this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report_display, container, false)
    }
}
