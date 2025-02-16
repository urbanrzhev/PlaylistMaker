package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
   private var temporaryEditText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonSettingsBack = findViewById<MaterialToolbar>(R.id.toolbar_search)
        val buttonClearSearch = findViewById<ImageView>(R.id.clearSearchButton)
        val editSearch = findViewById<EditText>(R.id.editSearchText)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                temporaryEditText = s.toString()
                buttonClearSearch.isVisible = !s.isNullOrEmpty()
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

        buttonSettingsBack.setNavigationOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.secret_code), temporaryEditText)

    }

    @SuppressLint("SetTextI18n")
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val textEdit = savedInstanceState.getString(getString(R.string.secret_code))
        findViewById<EditText>(R.id.editSearchText).setText(textEdit)
    }
}