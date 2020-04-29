package com.engarnet.timetree

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engarnet.timetree.databinding.ActivityCalendarBinding
import com.engarnet.timetree.model.TCalendar
import com.engarnet.timetree.util.Container
import com.engarnet.timetree.view.EventRecyclerViewAdapter
import kotlinx.coroutines.runBlocking
import java.util.*

class CalendarActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityCalendarBinding>(this, R.layout.activity_calendar)
    }

    private val calendarId by lazy {
        intent?.extras?.get(KEY_CALENDAR_ID) as? String
            ?: throw IllegalStateException("calendarId must is needed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.adapter = EventRecyclerViewAdapter("イベント一覧", listOf()) { position ->
            startActivity(
                EventActivity.createIntentWithEvent(
                    context = this,
                    calendar = binding.calendar!!,
                    event = binding.adapter!!.items[position]
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            runCatching {
                Container.timeTreeClient.calendar(
                    calendarId = calendarId
                )
            }.onSuccess {
                this@CalendarActivity.title = it.name
                binding.calendar = it
            }.onFailure {
                print(it)
            }

            runCatching {
                Container.timeTreeClient.upcomingEvents(
                    calendarId = calendarId,
                    timezone = TimeZone.getDefault()
                )
            }.onSuccess {
                binding.adapter!!.items = it
                binding.adapter!!.notifyDataSetChanged()
            }.onFailure {
                print(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_add -> {
                startActivity(
                    EventActivity.createIntent(
                        context = this,
                        calendar = binding.calendar!!
                    )
                )
                true
            }
            else -> false
        }
    }

    companion object {
        private const val KEY_CALENDAR_ID = "key_calendar_id"
        fun createIntent(context: Context, calendar: TCalendar): Intent {
            return Intent(context, CalendarActivity::class.java).apply {
                putExtra(KEY_CALENDAR_ID, calendar.id)
            }
        }
    }
}