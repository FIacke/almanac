package com.quietapps.almanac

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.EditText
import android.widget.LinearLayout
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

        val prefs = getSharedPreferences("almanac", Context.MODE_PRIVATE)
        val profileName = findViewById<TextView>(R.id.profileName)
        val profileAvatar = findViewById<TextView>(R.id.profileAvatar)
        profileName.text = prefs.getString("user_name", "Andriyan")
        profileAvatar.text = profileName.text.firstOrNull()?.uppercase() ?: "A"

        val tvProfileId = findViewById<TextView>(R.id.tvProfileId)
        tvProfileId.setOnClickListener {
            val now = SystemClock.elapsedRealtime()
            if (now - lastTapTime > 3000) tapCount = 1 else tapCount++
            lastTapTime = now
            if (tapCount == 5) { tapCount = 0; showAdminLoginDialog() }
        }

        findViewById<LinearLayout>(R.id.profileHeader).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.itemPrivacy).setOnClickListener {
            Toast.makeText(this, "Конфиденциальность (скоро)", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.itemNotifications).setOnClickListener {
            Toast.makeText(this, "Уведомления (скоро)", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.itemStorage).setOnClickListener {
            Toast.makeText(this, "Данные и память (скоро)", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.itemLanguage).setOnClickListener {
            Toast.makeText(this, "Русский (единственный доступный)", Toast.LENGTH_SHORT).show()
        }
        findViewById<LinearLayout>(R.id.itemAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.itemLogout).setOnClickListener { showLogoutDialog() }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("almanac", Context.MODE_PRIVATE)
        findViewById<TextView>(R.id.profileName).text = prefs.getString("user_name", "Andriyan")
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this, R.style.Theme_Almanac_Dialog)
            .setTitle("Выход")
            .setMessage("Вы уверены, что хотите выйти из аккаунта? Все локальные данные будут удалены.")
            .setPositiveButton("Выйти") { _, _ ->
                getSharedPreferences("almanac", Context.MODE_PRIVATE).edit().clear().apply()
                Toast.makeText(this, "Вы вышли из аккаунта", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showAdminLoginDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_admin_login, null)
        val inputCode = dialogView.findViewById<EditText>(R.id.inputAdminCode)
        AlertDialog.Builder(this, R.style.Theme_Almanac_Dialog).setView(dialogView).create().apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            show()
            dialogView.findViewById<android.widget.Button>(R.id.btnAdminCancel).setOnClickListener { dismiss() }
            dialogView.findViewById<android.widget.Button>(R.id.btnAdminLogin).setOnClickListener {
                if (inputCode.text.toString().trim() == "admin123") {
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
