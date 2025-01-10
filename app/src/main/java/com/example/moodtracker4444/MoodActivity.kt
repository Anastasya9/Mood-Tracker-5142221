package com.example.moodtracker

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MoodActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var moodEditText: EditText
    private lateinit var moodHistoryTextView: TextView
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood)

        sharedPreferences = getSharedPreferences("MoodTrackerPrefs", MODE_PRIVATE)
        username = intent.getStringExtra("USERNAME")

        moodEditText = findViewById(R.id.moodEditText)
        moodHistoryTextView = findViewById(R.id.moodHistoryTextView)

        findViewById<Button>(R.id.saveMoodButton).setOnClickListener {
            saveMood()
        }

        findViewById<Button>(R.id.mostFrequentMoodButton).setOnClickListener {
            showMostFrequentMood()
        }

        displayMoodHistory()
    }

    private fun saveMood() {
        val mood = moodEditText.text.toString()
        if (mood.isNotEmpty()) {
            val moodList = sharedPreferences.getStringSet(username!!, mutableSetOf()) ?: mutableSetOf()
            moodList.add("${getCurrentDate()}: $mood")
            sharedPreferences.edit().putStringSet(username, moodList).apply()
            Toast.makeText(this, "Настроение сохранено!", Toast.LENGTH_SHORT).show()
            displayMoodHistory()
        } else {
            Toast.makeText(this, "Введите настроение", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayMoodHistory() {
        val moodList = sharedPreferences.getStringSet(username!!, mutableSetOf())
        val moodHistory = StringBuilder("История настроений:\n")

        if (moodList.isNullOrEmpty()) {
            moodHistory.append("Нет записей.")
        } else {
            for (mood in moodList) {
                moodHistory.append("$mood\n")
            }
        }

        moodHistoryTextView.text = moodHistory.toString()
    }

    private fun showMostFrequentMood() {
        val moodList = sharedPreferences.getStringSet(username!!, mutableSetOf())
        val moodCount = mutableMapOf<String, Int>()

        if (!moodList.isNullOrEmpty()) {
            for (moodEntry in moodList) {
                val mood = moodEntry.substringAfter(": ").trim()
                moodCount[mood] = moodCount.getOrDefault(mood, 0) + 1
            }

            val mostFrequentMood = moodCount.maxByOrNull { it.value }
            if (mostFrequentMood != null) {
                Toast.makeText(this, "Самое частое настроение: ${mostFrequentMood.key}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Нет записей.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Нет записей.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
}
