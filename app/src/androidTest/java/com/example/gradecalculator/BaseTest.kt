package com.example.gradecalculator

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class BaseTest {

    object DrawableMatcher {

        /**
         * Invokes the [RecyclerViewAssertion] to check the RecyclerView has the correct count
         *
         * @param count The expected number of items in the RecyclerView adapter
         */
        fun hasItemCount(count: Int): ViewAssertion {
            return RecyclerViewAssertion(count)
        }

        private class RecyclerViewAssertion(private val count: Int) : ViewAssertion {
            override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
                if (noViewFoundException != null) {
                    throw noViewFoundException
                }

                if (view !is RecyclerView) {
                    throw IllegalStateException("The view is not a RecyclerView")
                }

                if (view.adapter == null) {
                    throw IllegalStateException("No adapter assigned to RecyclerView")
                }

                // Check item count
                ViewMatchers.assertThat(
                    "RecyclerView item count",
                    view.adapter?.itemCount,
                    CoreMatchers.equalTo(count)
                )
            }
        }
    }
}