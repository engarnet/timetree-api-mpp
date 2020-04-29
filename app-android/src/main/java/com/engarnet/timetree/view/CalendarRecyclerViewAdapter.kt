package com.engarnet.timetree.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engarnet.timetree.databinding.RecyclerviewCalendarContentBinding
import com.engarnet.timetree.databinding.RecyclerviewHeaderBinding
import com.engarnet.timetree.model.TCalendar

typealias OnCalendarItemClicked = (position: Int) -> Unit

class CalendarRecyclerViewAdapter(
    private val title: String,
    var items: List<TCalendar>,
    private val onItemClicked: OnCalendarItemClicked
) :
    RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewType.of(viewType).onCreateViewHolder(parent, onItemClicked)
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.Header -> holder.bind(title)
            is ViewHolder.Content -> holder.bind(items[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.Header.viewType
            else -> ViewType.Content.viewType
        }
    }

    enum class ViewType(val viewType: Int) {
        Header(0),
        Content(1);

        fun onCreateViewHolder(
            parent: ViewGroup,
            onItemClicked: OnCalendarItemClicked
        ): ViewHolder {
            return when (this) {
                Header -> ViewHolder.Header(parent.context)
                Content -> ViewHolder.Content(parent.context, onItemClicked)
            }
        }

        companion object {
            fun of(viewType: Int): ViewType {
                return values().firstOrNull { it.viewType == viewType }
                    ?: throw IllegalStateException()
            }
        }
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class Header(
            context: Context,
            private var binding: RecyclerviewHeaderBinding = RecyclerviewHeaderBinding.inflate(
                LayoutInflater.from(context)
            )
        ) : ViewHolder(binding.root) {
            fun bind(title: String) {
                binding.title = title
            }
        }

        class Content(
            context: Context,
            onItemClicked: OnCalendarItemClicked,
            private var binding: RecyclerviewCalendarContentBinding = RecyclerviewCalendarContentBinding.inflate(
                LayoutInflater.from(context)
            )
        ) : ViewHolder(binding.root) {
            init {
                itemView.setOnClickListener {
                    onItemClicked(adapterPosition - 1)
                }
            }

            fun bind(calendar: TCalendar) {
                binding.calendar = calendar
            }
        }
    }
}