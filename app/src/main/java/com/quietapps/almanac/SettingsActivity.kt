package com.quietapps.almanac

import android.app.AlertDialog
import android.os.Bundle
import android.os.SystemClock
import android.widget.EditText
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
            
            if (currentTime - lastTapTime > 3000) {
                // Если прошло больше 3 секунд — сбрасываем счётчик
                tapCount = 1
            } else {
                tapCount++
            }
            lastTapTime = currentTime

            if (tapCount == 5) {
                tapCount = 0 // Сбрасываем
                showAdminLoginDialog()
            }
        }
    }

    private fun showAdminLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_admin_login, null)
        val inputCode = dialogView.findViewById<EditText>(R.id.inputAdminCode)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
            .apply {
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()

                dialogView.findViewById<android.widget.Button>(R.id.btnAdminCancel).setOnClickListener {
                    dismiss()
                }

                dialogView.findViewById<android.widget.Button>(R.id.btnAdminLogin).setOnClickListener {
                    val code = inputCode.text.toString().trim()
                    // Пока что тестовый код: admin123
                    if (code == "admin123") {
                        Toast.makeText(this@SettingsActivity, "Доступ разрешён. Добро пожаловать, Администратор!", Toast.LENGTH_LONG).show()
                        dismiss()
                        // TODO: Здесь будет переход на AdminPanelActivity
                    } else {
                        Toast.makeText(this@SettingsActivity, "Неверный код", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
