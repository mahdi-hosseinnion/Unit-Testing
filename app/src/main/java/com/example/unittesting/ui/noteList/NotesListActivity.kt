package com.example.unittesting.ui.noteList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unittesting.R
import com.example.unittesting.models.Note
import com.example.unittesting.ui.Status
import com.example.unittesting.ui.note.NoteActivity
import com.example.unittesting.util.VerticalSpacingItemDecoration
import com.example.unittesting.viewmodels.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_notes_list.*
import javax.inject.Inject

class NotesListActivity : DaggerAppCompatActivity(),
    NoteRecyclerAdapter.OnNoteListener,
    View.OnClickListener {
    private val TAG = "NotesListActivity"

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    //vars
    private lateinit var viewModel: NotesListViewModel
    private lateinit var recyclerAdapter: NoteRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)
        viewModel = ViewModelProviders.of(this, providerFactory).get(NotesListViewModel::class.java)
        subscribeObservers()
        initRecyclerView()
    }

    private fun subscribeObservers() {
        Log.d(TAG, "subscribeObservers: called.")

        viewModel.note.observe(this) { noteList ->
            noteList?.let {
                recyclerAdapter.submitList(it)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllNotes();
    }
    private fun initRecyclerView() {
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@NotesListActivity)
            val topSpacingDecorator = VerticalSpacingItemDecoration(10)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)
            recyclerAdapter = NoteRecyclerAdapter(this@NotesListActivity)
            ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(this)
            adapter = recyclerAdapter
        }
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteActivity::class.java)
        intent.putExtra(getString(R.string.intent_note), note)
        startActivity(intent)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab -> {
                startActivity(Intent(this, NoteActivity::class.java))
            }
        }
    }


    private fun showSnackBar(message: String) {
        if (message.isNotBlank()) {

            Snackbar.make(parent_view, message, Snackbar.LENGTH_SHORT).show();
        }
    }


    val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val note = recyclerAdapter.getNote(viewHolder.adapterPosition)

            recyclerAdapter.removeNote(note)
            val deleteAction = viewModel.deleteNote(note)
            deleteAction.observe(this@NotesListActivity, Observer { intResource ->
                intResource?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Log.d(TAG, "onSwiped: Note successfully deleted! ")
                            showSnackBar("Note successfully deleted!")
                        }
                        Status.ERROR -> {
                            Log.d(
                                TAG,
                                "onSwiped: Note did not delete! \n Error: ${resource.message}"
                            )
                            showSnackBar("Note did not delete! \n Error: ${resource.message}")
                        }
                        Status.LOADING -> {
                            Log.d(TAG, "onSwiped: Deleting Note LOADING")
                        }
                    }
                }
                deleteAction.removeObservers(this@NotesListActivity)
            })

        }

    }

}