package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Region.Op
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

private const val SEARCH_DEBOUNCE_DELAY = 2000L

class SearchActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable:Runnable
    private var temporaryEditText = ""
    private lateinit var progressBar: FrameLayout
    private lateinit var optionAdaptersAndProgressBar: OptionAdaptersAndProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            val bottomPadding = if (ime.bottom > 0) ime.bottom else systemBars.bottom

            v.setPadding(systemBars.left, systemBars.top + ime.top, systemBars.right, bottomPadding)
            insets
        }
        progressBar = findViewById<FrameLayout>(R.id.progress_circular)
        val buttonSettingsBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        val clearHistoryButton = findViewById<Button>(R.id.clearHistoryButton)
        val buttonClearSearch = findViewById<ImageView>(R.id.clearSearchButton)
        val editSearch = findViewById<EditText>(R.id.editSearchText)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerSearch)
        optionAdaptersAndProgressBar = OptionAdaptersAndProgressBar(recyclerView, progressBar)
        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerSearchHistory)
        val viewGroupHistory = findViewById<LinearLayout>(R.id.viewGroupHistory)
        val sharedPrefsHistory =
            SearchHistory(getSharedPreferences(SearchHistory.MY_HISTORY_PREFERENCES, MODE_PRIVATE))
        recyclerViewHistory.adapter = sharedPrefsHistory.getAdapter()
        runnable =
            Runnable { searchRequest(editSearch.text.toString(), sharedPrefsHistory) }

        clearHistoryButton.setOnClickListener {
            sharedPrefsHistory.clearHistory()
            viewGroupHistory.isVisible = false
        }

        editSearch.setOnFocusChangeListener { _, hasFocus ->
            viewGroupHistory.isVisible =
                hasFocus && editSearch.text.isEmpty() && sharedPrefsHistory.getCount()
        }

        clearHistoryButton.setOnClickListener {
            sharedPrefsHistory.clearHistory()
            viewGroupHistory.isVisible = false
        }

        editSearch.setOnFocusChangeListener { _, hasFocus ->
            viewGroupHistory.isVisible =
                hasFocus && editSearch.text.isEmpty() && sharedPrefsHistory.getCount()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                temporaryEditText = s.toString()
                buttonClearSearch.isVisible = !s.isNullOrEmpty()
                viewGroupHistory.isVisible =
                    editSearch.hasFocus() && editSearch.text.isEmpty() && sharedPrefsHistory.getCount()
                if (s.toString().isNotEmpty())
                    searchDebounce()
                else recyclerView.adapter = null
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

    private fun searchDebounce() {
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest(
        requestText: String,
        sharedPrefsHistory: SearchHistory
    ) {
        if (requestText.isNotEmpty()) {
            ITunesService.load(
                requestText,
                sharedPrefsHistory,
                onProgressBarVisibleTrueCallback = { optionAdaptersAndProgressBar.progressBarVisibleTrue() },
                callback = { data -> optionAdaptersAndProgressBar.setAdapterSuccefull(data) },
                errorCallback = { errorData, text ->
                    optionAdaptersAndProgressBar.setAdaptersErrors(
                        errorData,
                        text
                    )
                },
                errorConnectionCallback = { optionAdaptersAndProgressBar.setAdaptersErrorConnect() }
            )
        }
    }
}