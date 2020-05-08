package com.engarnet.timetree.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engarnet.timetree.R
import com.engarnet.timetree.common.Container
import com.engarnet.timetree.databinding.ActivityCalendarListBinding
import com.engarnet.timetree.event.EventListActivity
import com.engarnet.timetree.view.CalendarRecyclerViewAdapter
import kotlinx.coroutines.runBlocking

class CalendarListActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityCalendarListBinding>(
            this,
            R.layout.activity_calendar_list
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.adapter = CalendarRecyclerViewAdapter("Calendar List", listOf()) { position ->
            startActivity(
                EventListActivity.createIntent(
                    this,
                    binding.adapter!!.items[position]
                )
            )
        }
        fetchCalendars()
    }

    private fun fetchCalendars() {
        runBlocking {
            runCatching {
                Container.timeTreeClient.calendars()
            }.onSuccess { calendars ->
                binding.adapter!!.items = calendars
                binding.adapter!!.notifyDataSetChanged()
            }.onFailure {
                print(it)
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, CalendarListActivity::class.java)
        }
    }
}
