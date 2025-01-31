package com.example.playlistmaker

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
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonSettingsBack = findViewById<ImageView>(R.id.search_back)
        val buttonClearSearch = findViewById<Button>(R.id.clearSearchButton)
        val editSearch = findViewById<EditText>(R.id.editSearchText)
        Log.v("vasa", "onCreateActivity")
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClearSearch.visibility = if(editSearch.text.toString() == "")View.GONE else  View.VISIBLE
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

        buttonSettingsBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}