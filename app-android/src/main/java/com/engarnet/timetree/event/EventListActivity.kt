package com.engarnet.timetree.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engarnet.timetree.R
import com.engarnet.timetree.common.Container
import com.engarnet.timetree.databinding.ActivityEventListBinding
import com.engarnet.timetree.model.TCalendar
import com.engarnet.timetree.view.EventRecyclerViewAdapter
import kotlinx.coroutines.runBlocking
import java.util.*

class EventListActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityEventListBinding>(
            this,
            R.layout.activity_event_list
        )
    }

    private val calendarId by lazy {
        intent?.extras?.get(KEY_CALENDAR_ID) as? String
            ?: throw IllegalStateException("calendarId must is needed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.adapter = EventRecyclerViewAdapter("Upcoming Event List", listOf()) { position ->
            startActivity(
                EventDetailActivity.createIntent(
                    context = this,
                    calendarId = binding.calendar!!.id,
                    eventId = binding.adapter!!.items[position].id
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
                this@EventListActivity.title = it.name
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
        menuInflater.inflate(R.menu.menu_event_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_add -> {
                startActivity(
                    EventEditActivity.createIntent(
                        context = this,
                        calendarId = calendarId
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
            return Intent(context, EventListActivity::class.java).apply {
                putExtra(KEY_CALENDAR_ID, calendar.id)
            }
        }
    }
}