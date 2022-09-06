package com.example.gradecalculator

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gradecalculator.BaseTest.DrawableMatcher.hasItemCount
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.StringContains.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorTests {

    private fun withIndex(matcher: Matcher<View?>, index: Int): Matcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            var currentIndex = 0

            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View?): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun no_user_input() {
        onView(withId(R.id.floatingActionButton)).perform(click())

        onView(withId(R.id.calculate_button)).perform(click())

        onView(withId(R.id.course_name))
            .check(matches(withText(containsString(""))))

        onView(withId(R.id.course_average))
            .check(matches(withText(containsString("0.0"))))

        onView(withId(R.id.average_to_pass))
            .check(matches(withText(containsString("0.0"))))
    }

    @Test
    fun calculate_50_percent_desired_grade() {
        onView(withId(R.id.floatingActionButton)).perform(click())

        onView(withId(R.id.course_title)).perform(typeText("Math"))

        onView(withId(R.id.desired_grade_edit_text))
            .check(matches(withText(containsString("50"))))

        onView(withIndex(withId(R.id.grade), 0)).perform(typeText("80"))
        onView(withIndex(withId(R.id.weight), 0)).perform(typeText("50"))

        onView(withIndex(withId(R.id.grade), 1)).perform(typeText("50"))
        onView(withIndex(withId(R.id.weight), 1))
            .perform(typeText("10"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.calculate_button)).perform(click())

        onView(withId(R.id.course_name))
            .check(matches(withText(containsString("Math"))))

        onView(withId(R.id.course_average))
            .check(matches(withText(containsString("76.67"))))

        onView(withId(R.id.average_to_pass))
            .check(matches(withText(containsString("10.00"))))

    }

    @Test
    fun calculate_80_percent_desired_grade() {
        onView(withId(R.id.floatingActionButton)).perform(click())

        onView(withId(R.id.course_title)).perform(typeText("English"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.desired_grade_edit_text))
            .perform(clearText())
            .perform(typeText("80"))

        onView(withIndex(withId(R.id.grade), 0)).perform(typeText("80"))
        onView(withIndex(withId(R.id.weight), 0)).perform(typeText("50"))

        onView(withIndex(withId(R.id.grade), 1)).perform(typeText("50"))
        onView(withIndex(withId(R.id.weight), 1))
            .perform(typeText("10"))
            .perform(closeSoftKeyboard())

        onView(withId(R.id.calculate_button)).perform(click())

        onView(withId(R.id.course_name))
            .check(matches(withText(containsString("English"))))

        onView(withId(R.id.course_average))
            .check(matches(withText(containsString("76.67"))))

        onView(withId(R.id.average_to_pass))
            .check(matches(withText(containsString("85.00"))))
    }

    @Test
    fun add_and_delete_rows() {

        for (i in 1..3) onView(withIndex(withId(R.id.delete_button), 0)).perform(click())

        for (i in 1..3) onView(withId(R.id.add_button)).perform(click())

    }

    @Test
    fun recycler_view_item_count() {

        for (i in 1..6) {
            onView(withId(R.id.floatingActionButton)).perform(click())
            onView(withId(R.id.calculate_button)).perform(click())
        }

        onView(withId(R.id.vertical_recycler_view)).check(hasItemCount(6))
    }
}

