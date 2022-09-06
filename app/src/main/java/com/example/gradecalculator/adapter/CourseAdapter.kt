package com.example.gradecalculator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gradecalculator.R
import com.example.gradecalculator.data.DataSource
import com.example.gradecalculator.model.Course
import org.w3c.dom.Text

/**
 * Adapter to inflate the appropriate course layout and populate the view with information from
 * the appropriate data source
 */

class CourseAdapter(
    private val context: Context?
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    // Initialize the data using the List found in data/DataSource
    private val data: MutableList<Course> = DataSource.courses

    /**
     * Initialize view elements
     */
    class CourseViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        val textView1: TextView = view!!.findViewById(R.id.course_name)
        val textView2: TextView = view!!.findViewById(R.id.course_average)
        val textView3: TextView = view!!.findViewById(R.id.average_to_pass)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {

        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.course_list_item, parent, false)

        return CourseViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val item = data[position]

        holder.textView1.text = item.name
        holder.textView2.text = item.average
        holder.textView3.text = item.averageToPass

    }

    /**
     * Returns the size of the data set
     */
    override fun getItemCount(): Int = data.size
}