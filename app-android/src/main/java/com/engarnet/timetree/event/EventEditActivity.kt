package com.engarnet.timetree.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.engarnet.timetree.R
import com.engarnet.timetree.common.Container
import com.engarnet.timetree.databinding.ActivityEventEditBinding
import com.engarnet.timetree.model.TEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class EventEditActivity : AppCompatActivity() {
    private val bindingModel by lazy {
        binding.bindingModel ?: throw java.lang.IllegalStateException("viewModel is null")
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityEventEditBinding>(
            this,
            R.layout.activity_event_edit
        )
    }

    private val calendarId by lazy {
        intent.extras?.get(KEY_CALENDAR_ID) as? String
            ?: throw IllegalStateException("calendarId must is needed")
    }

    private val eventId by lazy {
        intent.extras?.get(KEY_EVENT_ID) as? String
    }

    private val isUpdate by lazy {
        eventId != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this

        eventId?.also { eventId ->
            runBlocking {
                runCatching {
                    Container.timeTreeClient.event(
                        calendarId = calendarId,
                        eventId = eventId
                    )
                }.onSuccess {
                    binding.bindingModel =
                        BindingModel.from(
                            this@EventEditActivity,
                            it
                        )
                    this@EventEditActivity.title = it.title
                }.onFailure {
                    print(it)
                }
            }
        } ?: run {
            binding.bindingModel =
                BindingModel.empty(
                    this
                )
            this@EventEditActivity.title = "NewEvent"
        }

        binding.saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                onSaveClicked()
            }
        }
        binding.startDateText.setOnClickListener {
            val year = Integer.parseInt(bindingModel.startDate.value!!.substring(0, 4))
            val month = Integer.parseInt(bindingModel.startDate.value!!.substring(5, 7)) - 1
            val day = Integer.parseInt(bindingModel.startDate.value!!.substring(8, 10))
            showDatePickerDialog(year, month, day) {
                bindingModel.startDate.value = it
                binding.invalidateAll()
            }
        }
        binding.endDateText.setOnClickListener {
            val year = Integer.parseInt(bindingModel.endDate.value!!.substring(0, 4))
            val month = Integer.parseInt(bindingModel.endDate.value!!.substring(5, 7)) - 1
            val day = Integer.parseInt(bindingModel.endDate.value!!.substring(8, 10))
            showDatePickerDialog(year, month, day) {
                bindingModel.endDate.value = it
                binding.invalidateAll()
            }
        }
        binding.startTimeText.setOnClickListener {
            val hour = Integer.parseInt(bindingModel.startTime.value!!.substring(0, 2))
            val minute = Integer.parseInt(bindingModel.startTime.value!!.substring(3, 5))
            showTimePickerDialog(hour, minute) {
                bindingModel.startTime.value = it
                binding.invalidateAll()
            }

        }
        binding.endTimeText.setOnClickListener {
            val hour = Integer.parseInt(bindingModel.endTime.value!!.substring(0, 2))
            val minute = Integer.parseInt(bindingModel.endTime.value!!.substring(3, 5))
            showTimePickerDialog(hour, minute) {
                bindingModel.endTime.value = it
                binding.invalidateAll()
            }
        }
    }

    private fun showDatePickerDialog(year: Int, month: Int, day: Int, onChanged: (String) -> Unit) {
        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, y, m, d ->
                Calendar.getInstance().apply {
                    set(y, m, d)
                }.let {
                    onChanged(
                        DateFormat.format(
                            this@EventEditActivity.getString(R.string.date_format), it
                        ).toString()
                    )
                }
            }, year, month, day
        ).run {
            show()
        }
    }

    private fun showTimePickerDialog(hour: Int, minute: Int, onChanged: (String) -> Unit) {
        TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, h, m ->
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, h)
                    set(Calendar.MINUTE, m)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.let {
                    onChanged(
                        DateFormat.format(
                            this@EventEditActivity.getString(R.string.time_format), it
                        ).toString()
                    )
                }
            }, hour, minute, true
        ).run {
            show()
        }
    }

    private suspend fun onSaveClicked() {
        if (isUpdate) {
            handleUpdateButtonClicked()
        } else {
            handleAddButtonClicked()
        }
    }

    private suspend fun handleAddButtonClicked() {
        val start =
            if (bindingModel.allDay.value == true) bindingModel.startDate.value + "00:00" else bindingModel.startDate.value + bindingModel.startTime.value
        val end =
            if (bindingModel.allDay.value == true) bindingModel.endDate.value + "00:00" else bindingModel.endDate.value + bindingModel.endTime.value

        runCatching {
            val labels = Container.timeTreeClient.calendarLabels(calendarId)
            val members = Container.timeTreeClient.calendarMembers(calendarId)
            if (bindingModel.isKeep.value!!) {
                Container.timeTreeClient.addKeep(
                    calendarId = calendarId,
                    title = bindingModel.title.value!!,
                    description = bindingModel.description.value,
                    location = bindingModel.location.value,
                    url = bindingModel.url.value?.let { Uri.parse(it) },
                    label = labels[0],
                    attendees = members
                )
            } else {
                Container.timeTreeClient.addSchedule(
                    calendarId = calendarId,
                    title = bindingModel.title.value!!,
                    allDay = bindingModel.allDay.value!!,
                    startAt = start.toDate(this),
                    endAt = end.toDate(this),
                    timeZone = TimeZone.getDefault(),
                    description = bindingModel.description.value,
                    location = bindingModel.location.value,
                    url = bindingModel.url.value?.let { Uri.parse(it) },
                    label = labels[0],
                    attendees = members
                )
            }
        }.onSuccess {
            finish()
        }.onFailure {
            print(it)
        }
    }

    private suspend fun handleUpdateButtonClicked() {
        val bindingModel = binding.bindingModel ?: return
        val start =
            if (bindingModel.allDay.value == true) bindingModel.startDate.value + "00:00" else bindingModel.startDate.value + bindingModel.startTime.value
        val end =
            if (bindingModel.allDay.value == true) bindingModel.endDate.value + "00:00" else bindingModel.endDate.value + bindingModel.endTime.value

        runCatching {
            val labels = Container.timeTreeClient.calendarLabels(calendarId)
            val members = Container.timeTreeClient.calendarMembers(calendarId)
            if (bindingModel.isKeep.value!!) {
                Container.timeTreeClient.updateKeep(
                    calendarId = calendarId,
                    eventId = bindingModel.eventId.value!!,
                    title = bindingModel.title.value!!,
                    description = bindingModel.description.value!!,
                    location = bindingModel.location.value,
                    url = bindingModel.url.value?.let { Uri.parse(it) },
                    label = labels.get(0),
                    attendees = members
                )
            } else {
                Container.timeTreeClient.updateSchedule(
                    calendarId = calendarId,
                    eventId = bindingModel.eventId.value!!,
                    title = bindingModel.title.value!!,
                    allDay = bindingModel.allDay.value!!,
                    startAt = start.toDate(this),
                    endAt = end.toDate(this),
                    timeZone = TimeZone.getDefault(),
                    description = bindingModel.description.value,
                    location = bindingModel.location.value,
                    url = bindingModel.url.value?.let { Uri.parse(it) },
                    label = labels.get(0),
                    attendees = members
                )
            }
        }.onSuccess {
            finish()
        }.onFailure {
            print(it)
        }
    }

    private fun String.toDate(context: Context): Date {
        val format =
            context.getString(R.string.date_format) + context.getString(
                R.string.time_format
            )
        return SimpleDateFormat(format).parse(this)
    }

    companion object {
        private const val KEY_CALENDAR_ID = "key_calendar_id"
        private const val KEY_EVENT_ID = "key_event_id"
        fun createIntent(context: Context, calendarId: String, eventId: String? = null): Intent {
            return Intent(context, EventEditActivity::class.java).apply {
                putExtra(KEY_CALENDAR_ID, calendarId)
                eventId?.let {
                    putExtra(KEY_EVENT_ID, eventId)
                }
            }
        }
    }

    data class BindingModel(
        val eventId: MutableLiveData<String> = MutableLiveData(),
        val isKeep: MutableLiveData<Boolean> = MutableLiveData(),
        val title: MutableLiveData<String> = MutableLiveData(),
        val description: MutableLiveData<String> = MutableLiveData(),
        val allDay: MutableLiveData<Boolean> = MutableLiveData(),
        val startDate: MutableLiveData<String> = MutableLiveData(),
        val startTime: MutableLiveData<String> = MutableLiveData(),
        val endDate: MutableLiveData<String> = MutableLiveData(),
        val endTime: MutableLiveData<String> = MutableLiveData(),
        val location: MutableLiveData<String> = MutableLiveData(),
        val url: MutableLiveData<String> = MutableLiveData()
    ) {

        companion object {
            fun empty(context: Context): BindingModel {
                return BindingModel().apply {
                    this.eventId.value = ""
                    this.isKeep.value = false
                    this.allDay.value = false
                    this.startDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        Calendar.getInstance()
                    ).toString()
                    this.startTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        Calendar.getInstance()
                    ).toString()
                    this.endDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        Calendar.getInstance()
                    ).toString()
                    this.endTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        Calendar.getInstance()
                    ).toString()
                }
            }

            fun from(context: Context, event: TEvent): BindingModel {
                return BindingModel().apply {
                    this.eventId.value = event.id
                    this.isKeep.value = event.isKeep
                    this.title.value = event.title
                    this.description.value = event.description
                    this.allDay.value = event.allDay
                    this.startDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        event.startAt
                    ).toString()
                    this.startTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        event.startAt
                    ).toString()
                    this.endDate.value = DateFormat.format(
                        context.getString(R.string.date_format),
                        event.endAt
                    ).toString()
                    this.endTime.value = DateFormat.format(
                        context.getString(R.string.time_format),
                        event.endAt
                    ).toString()
                    this.location.value = event.location
                    this.url.value = event.url?.toString()
                }
            }
        }
    }
}