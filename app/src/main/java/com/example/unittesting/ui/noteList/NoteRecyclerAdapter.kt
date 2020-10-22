package com.example.unittesting.ui.noteList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unittesting.R
import com.example.unittesting.models.Note
import com.example.unittesting.util.DateUtil
import kotlinx.android.synthetic.main.layout_note_list_item.view.*

public class NoteRecyclerAdapter(
    private val onNoteListener: OnNoteListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "NoteRecyclerAdapter"

    public var notes = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_note_list_item, parent, false)
        return ViewHolder(view, onNoteListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(notes[position])
        }
    }

    override fun getItemCount(): Int = notes.size

    public fun submitList(list: List<Note>) {
        this.notes.addAll(list)
        notifyDataSetChanged()
    }

    public fun removeNote(note: Note) {
        notes.remove(note)
        notifyDataSetChanged()
    }

    public fun removeNote(position: Int) {
        notes.removeAt(position)
        notifyDataSetChanged()
    }

    public fun getNote(position: Int): Note = notes[position]


    public class ViewHolder(
        itemView: View,
        private val onNoteListener: OnNoteListener?
    ) : RecyclerView.ViewHolder(
        itemView
    ) {
        fun bind(noteItem: Note) = with(itemView) {
            itemView.setOnClickListener {
                onNoteListener?.onNoteClick(noteItem)
            }
            var month: String = noteItem.timeStamp.substring(0, 2)
            month = DateUtil.getMonthFromNumber(month)
            val year: String = noteItem.timeStamp.substring(3)
            val timestamp = "$month $year"
            itemView.note_timestamp.text = timestamp
            itemView.note_title.text = noteItem.title
        }
    }


    interface OnNoteListener {
        fun onNoteClick(note: Note)
    }

}