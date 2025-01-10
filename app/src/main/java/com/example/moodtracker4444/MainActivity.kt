package com.example.moodtracker

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var moodDatabaseHelper: MoodDatabaseHelper
    private lateinit var moodHistoryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moodDatabaseHelper = MoodDatabaseHelper(this)
        moodHistoryTextView = findViewById(R.id.textViewMoodHistory)

        findViewById<Button>(R.id.buttonGoodMood).setOnClickListener {
            saveMood("Хорошее")
            showMessage("Отлично! Надеемся, что ваш день будет таким же хорошим!")
            displayMoodHistory()
        }

        findViewById<Button>(R.id.buttonAverageMood).setOnClickListener {
            saveMood("Так себе")
            showMessage("Ничего страшного! Надеемся, что скоро станет лучше!")
            displayMoodHistory()
        }

        findViewById<Button>(R.id.buttonBadMood).setOnClickListener {
            saveMood("Плохое")
            showMessage("Дни бывают тяжелыми. Не забывайте заботиться о себе!")
            displayMoodHistory()
        }

        findViewById<Button>(R.id.buttonViewMostFrequentMood).setOnClickListener {
            showMostFrequentMood()
        }

        displayMoodHistory()
    }

    private fun saveMood(mood: String) {
        val timestamp = System.currentTimeMillis().toString()
        moodDatabaseHelper.addMood(mood, timestamp)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayMoodHistory() {
        val allMoods = moodDatabaseHelper.getAllMoods()
        val moodHistory = StringBuilder("История настроений:\n")

        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        for ((mood, timestamp) in allMoods) {
            val formattedDate = dateFormat.format(Date(timestamp.toLong()))
            moodHistory.append("$formattedDate: $mood\n")
        }

        if (allMoods.isEmpty()) {
            moodHistory.append("Нет записей.")
        }

        moodHistoryTextView.text = moodHistory.toString()
    }

    private fun showMostFrequentMood() {
        val allMoods = moodDatabaseHelper.getAllMoods()
        val moodCount = mutableMapOf<String, Int>()

        for ((mood, timestamp) in allMoods) {
            moodCount[mood] = moodCount.getOrDefault(mood, 0) + 1
        }

        val mostFrequentMood = moodCount.maxByOrNull { it.value }

        if (mostFrequentMood != null) {
            showMoodDialog(mostFrequentMood.key)
        } else {
            showMessage("Нет записей.")
        }
    }

    private fun showMoodDialog(mood: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Итоги дня")
        builder.setMessage("Самое частое настроение: $mood\nДоброй ночи! Завтра будет день намного лучше.")
        builder.setPositiveButton("ОК") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}
