package com.engarnet.timetree.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.engarnet.timetree.R
import com.engarnet.timetree.common.Container
import com.engarnet.timetree.databinding.ActivityEventDetailBinding
import kotlinx.coroutines.runBlocking

class EventDetailActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityEventDetailBinding>(
            this,
            R.layout.activity_event_detail
        )
    }

    private val calendarId by lazy {
        intent.extras?.get(EventDetailActivity.KEY_CALENDAR_ID) as? String
            ?: throw IllegalStateException("calendarId must is needed")
    }

    private val eventId by lazy {
        intent.extras?.get(EventDetailActivity.KEY_EVENT_ID) as? String
            ?: throw IllegalStateException("eventId must is needed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        runBlocking {
            runCatching {
                Container.timeTreeClient.events(
                    calendarId = calendarId,
                    eventId = eventId
                )
            }.onSuccess {
                this@EventDetailActivity.title = it.title
                binding.event = it
                binding.webView.webViewClient = WebViewClient()
                binding.webView.loadUrl(it.url.toString())
            }.onFailure {
                print(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_event_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.menu_update -> {
                startActivity(
                    EventEditActivity.createIntent(
                        context = this,
                        calendarId = calendarId,
                        eventId = eventId
                    )
                )
                true
            }
            R.id.menu_delete -> {
                runBlocking {
                    runCatching {
                        Container.timeTreeClient.deleteEvent(
                            calendarId = calendarId,
                            eventId = eventId
                        )
                    }.onSuccess {
                        finish()
                    }.onFailure {
                        print(it)
                    }
                }
                true
            }
            else -> false
        }
    }

    companion object {
        private const val KEY_CALENDAR_ID = "key_calendar_id"
        private const val KEY_EVENT_ID = "key_event_id"
        fun createIntent(context: Context, calendarId: String, eventId: String): Intent {
            return Intent(context, EventDetailActivity::class.java).apply {
                putExtra(KEY_CALENDAR_ID, calendarId)
                putExtra(KEY_EVENT_ID, eventId)
            }
        }
    }
}
