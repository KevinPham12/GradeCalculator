package com.example.gradecalculator

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gradecalculator.data.DataSource
import com.example.gradecalculator.databinding.FragmentCalculatorBinding
import com.example.gradecalculator.model.Course
import com.example.gradecalculator.model.GradesViewModel
import kotlinx.android.synthetic.main.row.view.*
import kotlin.math.roundToInt

/**
 * [CalculatorFragment] allows the user to add a new course for display in the [TrackerFragment].
 * The users grades are calculated in this fragment.
 */

class CalculatorFragment : Fragment() {

    private val sharedViewModel: GradesViewModel by activityViewModels()

    // Initialize the data using the List found in data/DataSource
    private val data: MutableList<Course> = DataSource.courses
    private var rowViews: MutableList<View> = DataSource.rows

    // Binding object instance corresponding to the fragment_calculator.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment.
    private var binding: FragmentCalculatorBinding? = null

    private var numRows = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout XML file and return a binding object instance
        val fragmentBinding = FragmentCalculatorBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Grade Calculator"

        binding?.calculatorFragment = this

        binding?.courseTitle?.setOnKeyListener { listenerView, keyCode, _ ->
            handleKeyEvent(
                listenerView,
                keyCode
            )
        }

        for (i in 1..3) onAddRow()
    }

    /**
     * Add a new row for user to input additional grades.
     */
    fun onAddRow() {
        ++numRows
        // Inflate the instance corresponding to the row.xml layout
        // This is added to the screen and mutableList of views to access
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val currentRow: View = inflater.inflate(R.layout.row, null)
        currentRow.id = numRows
        Log.i("TAG", "The ID of the new row is " + currentRow.id)

        currentRow.delete_button.setOnClickListener { onDelete(currentRow) }

        currentRow.apply {
            assignment.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
            grade.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
            weight.setOnKeyListener { view, keyCode, _ -> handleKeyEvent(view, keyCode) }
        }

        binding?.parentLinearLayout?.addView(
            currentRow,
            binding!!.parentLinearLayout.childCount - 1
        )

        rowViews.add(currentRow)


        Log.i("TAG", "The array has " + rowViews.size + " entries")


    }

    /**
     * Delete an existing row.
     */
    private fun onDelete(view: View) {
        --numRows

        binding?.parentLinearLayout?.removeView(view)
        Log.i("TAG", "The ID of the deleted row is " + view.id)
        rowViews.removeAt(view.id - 1)

        updateRows()

        Log.i(
            "TAG",
            "The array has " + rowViews.size + " entries"
        )
    }

    /**
     * Calculate the course average by taking user inputs.
     */
    fun calculate() {

        var gradesTimesWeight = 0.0
        var weightSum = 0.0

        for (i in 0..rowViews.lastIndex) {

            val gradeInTextField = rowViews[i].findViewById<EditText>(R.id.grade).text.toString()
            val grade = if (gradeInTextField == "") 0.0
            else gradeInTextField.toDoubleOrNull()!!

            val weightInTextField = rowViews[i].findViewById<EditText>(R.id.weight).text.toString()
            val weight = if (weightInTextField == "") 0.0
            else weightInTextField.toDoubleOrNull()!!

            gradesTimesWeight += grade * weight
            weightSum += weight
        }

        val averageGrade = gradesTimesWeight / weightSum

        var goal = binding?.desiredGradeEditText?.text.toString().toDoubleOrNull()
        if (goal == null) goal = 0.0

        val forGoal =
            100 * (goal - ((averageGrade / 100.0) * weightSum)) / (100 - weightSum)

        Log.i("TAG", "The total grade is $averageGrade")
        Log.i("TAG", "Additional grade required is $forGoal")

        var roundedAverageGrade = 0.0
        var roundedForGoal = 0.0

        if (!averageGrade.isNaN()) {
            roundedAverageGrade = (averageGrade * 100.0).roundToInt() / 100.0
        }

        if (!forGoal.isNaN()) {
            roundedForGoal = (forGoal * 100.0).roundToInt() / 100.0
        }

        val course = Course(
            binding?.courseTitle?.text.toString(),
            roundedAverageGrade.toString(),
            roundedForGoal.toString()
        )
        data.add(course)

        Log.i("TAG", "The name of the course is " + binding?.courseTitle?.text.toString())

        Log.i("TAG", "The adapter has " + data.size + " courses")

        findNavController().navigate(R.id.action_calculatorFragment_to_trackerFragment)
    }

    /**
     * Updates the IDs of each row so that they are numbered corrected within the rowViews list
     */
    private fun updateRows() {
        var newId = 1

        for (item in rowViews) {
            item.id = newId
            ++newId
        }

    }

    /**
     * This fragment lifecycle method is called when the view hierarchy associated with the fragment
     * is being removed. As a result, clear out the binding object.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Hides the keyboard when pressing enter on the keyboard
     */
    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }

}
