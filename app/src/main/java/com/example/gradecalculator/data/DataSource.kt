package com.example.gradecalculator.data

import android.view.View
import com.example.gradecalculator.model.Course

/**
 * An object to generate a dynamic list of courses
 */

object DataSource {

    val courses: MutableList<Course> = mutableListOf()

    val rows: MutableList<View> = mutableListOf()
}