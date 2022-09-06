package com.example.gradecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gradecalculator.adapter.CourseAdapter
import com.example.gradecalculator.data.DataSource
import com.example.gradecalculator.databinding.FragmentTrackerBinding
import com.example.gradecalculator.model.Course
import com.example.gradecalculator.model.GradesViewModel

/**
 * This is the first screen of the Grade Calculator App. The user can view their average grades of
 * their current courses, as well as add new courses.
 */

class TrackerFragment : Fragment() {

    private val sharedViewModel: GradesViewModel by activityViewModels()

    // Initialize the data using the List found in data/DataSource
    private val data: MutableList<Course> = DataSource.courses

    // Binding object instance corresponding to the fragment_tracker.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentTrackerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        val fragmentBinding = FragmentTrackerBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Grade Tracker"

        binding?.trackerFragment = this
        binding!!.verticalRecyclerView.adapter = CourseAdapter(activity?.applicationContext)

    }

    /**
     * Navigate to the next screen to add a new course
     */
    fun launchAdd() {
        findNavController().navigate(R.id.action_trackerFragment_to_calculatorFragment)
    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}