package com.example.unittesting.ui.note

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.example.unittesting.R
import com.example.unittesting.models.Note
import com.example.unittesting.ui.Status
import com.example.unittesting.util.DateUtil
import com.example.unittesting.viewmodels.ViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_note_toolbar.*
import javax.inject.Inject

class NoteActivity : DaggerAppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener,
    View.OnClickListener,
    TextWatcher {
    private val TAG = "NoteActivity"

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: NoteViewModel
    private lateinit var mGestureDetector: GestureDetector
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        viewModel = ViewModelProviders.of(this, providerFactory).get(NoteViewModel::class.java)
        subscribeObservers()
        setListeners()
        if (savedInstanceState == null) {
            getIncomingIntent()
            enableEditMode()
        }
    }

    private fun getIncomingIntent() {
        try {
            var note: Note? = null
            if (intent.hasExtra(getString(R.string.intent_note))) {
                note =
                    Note(intent.getParcelableExtra<Note>(getString(R.string.intent_note)) as Note)
                viewModel.setInNewNote(false)
            } else {
                note = Note(0, "Title", "", DateUtil.getCurrentTimeStamp())
                viewModel.setInNewNote(true)
            }
            viewModel.setNote(note)
        } catch (e: Exception) {
            e.printStackTrace()
            showSnackBar(getString(R.string.error_intent_note));
        }
    }

    private fun setListeners() {
        mGestureDetector = GestureDetector(this, this)
        note_text.setOnTouchListener(this)
        toolbar_check.setOnClickListener(this)
        note_text_title.setOnClickListener(this)
        toolbar_back_arrow.setOnClickListener(this)
        note_edit_title.addTextChangedListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //TODO("maybe bug in source code it is under super")
        outState.putBoolean("has_started", true)
        super.onSaveInstanceState(outState)
    }

    private fun showSnackBar(message: String?) {
        if (!message.isNullOrBlank()) {
            Snackbar.make(parentView, message + "", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun subscribeObservers() {
        viewModel.note.observe(this) { note ->
            setNoteProperties(note)
        }
        viewModel.viewState.observe(this) { noteViewState ->
            noteViewState?.let {
                when (it) {
                    NoteViewState.EDIT -> {
                        enableContentInteraction()
                    }
                    NoteViewState.VIEW -> {
                        disableContentInteraction()
                    }
                }
            }
        }
    }

    private fun saveNote() {
        Log.d(TAG, "saveNote: called")
        try {
            viewModel.saveNote()?.observe(this) { resource ->
                resource?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            Log.d(TAG, "saveNote: save note: success")
                            showSnackBar("Note SUCCESSFULLY saved")
                        }
                        Status.ERROR -> {
                            Log.d(TAG, "saveNote: save note error: ${resource.message}")
                            showSnackBar(resource.message)
                        }
                        Status.LOADING -> {
                            Log.d(TAG, "saveNote: loading")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showSnackBar(e.message)
        }
    }

    private fun setNoteProperties(note: Note) {
        try {
            note_text_title.text = note.title
            note_edit_title.setText(note.title)
            note_text.setText(note.content)
        } catch (e: NullPointerException) {
            e.printStackTrace()
            showSnackBar("Error displaying note properties")
        }
    }

    private fun enableContentInteraction() {
        back_arrow_container.visibility = View.GONE
        check_container.visibility = View.VISIBLE

        note_text_title.visibility = View.GONE
        note_edit_title.visibility = View.VISIBLE

        note_text.keyListener = EditText(this).keyListener
        note_text.isFocusable = true
        note_text.isFocusableInTouchMode = true
        note_text.isCursorVisible = true
        note_text.requestFocus()
    }

    private fun disableContentInteraction() {
        hideKeyboard(this)

        back_arrow_container.visibility = View.VISIBLE
        check_container.visibility = View.GONE

        note_text_title.visibility = View.VISIBLE
        note_edit_title.visibility = View.GONE

        note_text.keyListener = null
        note_text.isFocusable = false
        note_text.isFocusableInTouchMode = false
        note_text.isCursorVisible = false
        note_text.clearFocus()
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun enableEditMode() {
        Log.d(TAG, "enableEditMode: called.");
        viewModel.setViewState(NoteViewState.EDIT);
    }

    private fun disableEditMode() {
        Log.d(TAG, "disableEditMode: called.");
        viewModel.setViewState(NoteViewState.VIEW);

        if (!note_text.text.isNullOrBlank()) {
            try {
                viewModel.updateNote(note_edit_title.text.toString(), note_text.text.toString());
            } catch (e: Exception) {
                e.printStackTrace();
                showSnackBar("Error setting note properties");
            }
        }

        saveNote();
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return mGestureDetector.onTouchEvent(p1)
    }

    override fun onDown(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return false

    }

    override fun onSingleTapConfirmed(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onDoubleTap(p0: MotionEvent?): Boolean {
        enableEditMode();
        return false
    }

    override fun onDoubleTapEvent(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onClick(p0: View?) = when (p0?.id) {
        R.id.toolbar_back_arrow -> {
            finish()
        }
        R.id.toolbar_check -> {
            disableEditMode()
        }
        R.id.note_text_title -> {
            enableEditMode()
            note_edit_title.requestFocus()
            note_edit_title.setSelection(note_edit_title.length())
        }
        else -> {

        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        note_text_title.text = p0.toString();

    }

    override fun afterTextChanged(p0: Editable?) {}

    override fun onBackPressed() {
        if(viewModel.shouldNavigateBack()){
            super.onBackPressed();
        }
        else{
            onClick(toolbar_check);
        }

    }
}