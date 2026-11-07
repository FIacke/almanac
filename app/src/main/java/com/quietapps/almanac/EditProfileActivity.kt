package com.quietapps.almanac

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        supportActionBar?.title = "Профиль"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = getSharedPreferences("almanac", Context.MODE_PRIVATE)
        val editName = findViewById<EditText>(R.id.editName)
        val editBio = findViewById<EditText>(R.id.editBio)

        editName.setText(prefs.getString("user_name", "Andriyan"))
        editBio.setText(prefs.getString("user_bio", ""))

        findViewById<Button>(R.id.btnSaveProfile).setOnClickListener {
            val name = editName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Имя не может быть пустым", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            prefs.edit()
                .putString("user_name", name)
                .putString("user_bio", editBio.text.toString().trim())
                .apply()
            Toast.makeText(this, "Профиль сохранён", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
