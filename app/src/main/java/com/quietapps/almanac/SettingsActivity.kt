package com.quietapps.almanac

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private var tapCount = 0
    private var lastTapTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = "Настройки"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tvProfileId = findViewById<TextView>(R.id.tvProfileId)

        // Стелс-логика: 5 кликов по ID
        tvProfileId.setOnClickListener {
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastTapTime > 3000) tapCount = 1 else tapCount++
            lastTapTime = currentTime
            if (tapCount == 5) {
                tapCount = 0
                showAdminLoginDialog()
            }
        }

        // Обработчики свитчей
        findViewById<Switch>(R.id.switchNotifications).setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Уведомления: ${if (isChecked) "Вкл" else "Выкл"}", Toast.LENGTH_SHORT).show()
        }
        findViewById<Switch>(R.id.switchSounds).setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Звуки: ${if (isChecked) "Вкл" else "Выкл"}", Toast.LENGTH_SHORT).show()
        }
        findViewById<Switch>(R.id.switchMediaAuto).setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Автозагрузка: ${if (isChecked) "Вкл" else "Выкл"}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAdminLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_admin_login, null)
        val inputCode = dialogView.findViewById<EditText>(R.id.inputAdminCode)

        AlertDialog.Builder(this, R.style.Theme_Almanac_Dialog)
            .setView(dialogView).create().apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()
                dialogView.findViewById<android.widget.Button>(R.id.btnAdminCancel).setOnClickListener { dismiss() }
                dialogView.findViewById<android.widget.Button>(R.id.btnAdminLogin).setOnClickListener {
                    val code = inputCode.text.toString().trim()
                    if (code == "admin123") {
                        Toast.makeText(this@SettingsActivity, "Добро пожаловать, Администратор!", Toast.LENGTH_LONG).show()
                        dismiss()
                    } else {
                        Toast.makeText(this@SettingsActivity, "Неверный код", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
