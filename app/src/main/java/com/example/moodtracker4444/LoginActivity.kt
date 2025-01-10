package com.example.moodtracker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            login()
        }

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            register()
        }
    }

    private fun login() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        val savedPassword = sharedPreferences.getString(username, null)

        if (savedPassword != null && savedPassword == password) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register() {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            sharedPreferences.edit().putString(username, password).apply()
            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
        }
    }
}
