package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    private var temporaryEditText = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val buttonSettingsBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val buttonClearSearch = findViewById<ImageView>(R.id.clearSearchButton)
        val editSearch = findViewById<EditText>(R.id.editSearchText)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerSearch)
        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerSearchHistory)
        val viewGroupHistory = findViewById<LinearLayout>(R.id.viewGroupHistory)
        val sharedPrefsHistory =
            SearchHistory(getSharedPreferences(SearchHistory.MY_HISTORY_PREFERENCES, MODE_PRIVATE))

        recyclerViewHistory.adapter = sharedPrefsHistory.getAdapter()

        clearHistoryButton.setOnClickListener {
            sharedPrefsHistory.clearHistory()
            viewGroupHistory.visibility = View.GONE
        }

        editSearch.setOnFocusChangeListener { _, hasFocus ->
            viewGroupHistory.visibility =
                if (hasFocus && editSearch.text.isEmpty() && sharedPrefsHistory.getCount()) View.VISIBLE else View.GONE
        }

        recyclerViewHistory.adapter = sharedPrefsHistory.getAdapter()

        clearHistoryButton.setOnClickListener {
            sharedPrefsHistory.clearHistory()
            viewGroupHistory.visibility = View.GONE
        }

        editSearch.setOnFocusChangeListener { _, hasFocus ->
            viewGroupHistory.isVisible = hasFocus && editSearch.text.isEmpty() && sharedPrefsHistory.getCount()
        }

        editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ITunesService.load(editSearch.text.toString(), recyclerView, sharedPrefsHistory)
                true
            }
            false
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                temporaryEditText = s.toString()
                buttonClearSearch.isVisible = !s.isNullOrEmpty()
                viewGroupHistory.visibility =
                    if (editSearch.hasFocus() && editSearch.text.isEmpty() && sharedPrefsHistory.getCount()) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editSearch.addTextChangedListener(simpleTextWatcher)
        buttonClearSearch.setOnClickListener {
            editSearch.text = null
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                buttonClearSearch.windowToken,
                0
            )
        }

        buttonSettingsBack.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.secret_code), temporaryEditText)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textEdit = savedInstanceState.getString(getString(R.string.secret_code))
        findViewById<EditText>(R.id.editSearchText).setText(textEdit)
    }
}