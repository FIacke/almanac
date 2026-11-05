package com.quietapps.almanac

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chatsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatsContainer = findViewById(R.id.chatsContainer)

        addChatItem(chatsContainer, "Алексей", "А", "#607D8B", "Привет! Как дела?", "14:32", 2)
        addChatItem(chatsContainer, "Мария", "М", "#795548", "Скинь документы", "12:05", 0)
        addChatItem(chatsContainer, "Дмитрий", "Д", "#455A64", "Хорошо, договорились", "Вчера", 0)

        findViewById<ImageButton>(R.id.fabNewChat).setOnClickListener {
            showNewChatDialog()
        }

        findViewById<LinearLayout>(R.id.navContacts).setOnClickListener {
            startActivity(Intent(this, ContactsActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.navSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun showNewChatDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_new_chat, null)
        val inputContact = dialogView.findViewById<EditText>(R.id.inputContact)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
            .apply {
                // Делаем фон диалога прозрачным, чтобы был виден наш кастомный фон
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                show()

                dialogView.findViewById<android.widget.Button>(R.id.btnCancel).setOnClickListener {
                    dismiss()
                }

                dialogView.findViewById<android.widget.Button>(R.id.btnCreate).setOnClickListener {
                    val name = inputContact.text.toString().trim()
                    if (name.isNotEmpty()) {
                        // Добавляем новый чат в список
                        val initial = name.first().toString().uppercase()
                        val color = getRandomAvatarColor()
                        addChatItem(chatsContainer, name, initial, color, "Новый чат", "Сейчас", 0)
                        dismiss()
                        // Сразу открываем чат
                        openChat(name)
                    } else {
                        Toast.makeText(this@MainActivity, "Введите имя или ID", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun getRandomAvatarColor(): String {
        val colors = listOf("#607D8B", "#795548", "#455A64", "#8E24AA", "#00897B", "#F4511E", "#3949AB")
        return colors.random()
    }

    private fun addChatItem(
        container: LinearLayout, name: String, initial: String, avatarColor: String,
        lastMessage: String, time: String, unread: Int
    ) {
        val item = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), 0, dp(16), 0)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(72))
            isClickable = true
            isFocusable = true
        }

        val avatar = TextView(this).apply {
            text = initial; setTextColor(Color.WHITE); textSize = 20f; gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(dp(48), dp(48))
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL; setColor(Color.parseColor(avatarColor))
            }
        }
        item.addView(avatar)

        val center = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; setPadding(dp(12), 0, dp(12), 0)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        center.addView(TextView(this).apply { text = name; setTextColor(Color.WHITE); textSize = 16f; setTypeface(typeface, Typeface.BOLD) })
        center.addView(TextView(this).apply { text = lastMessage; setTextColor(Color.parseColor("#8B9BA5")); textSize = 14f })
        item.addView(center)

        val right = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        right.addView(TextView(this).apply { text = time; setTextColor(Color.parseColor("#8B9BA5")); textSize = 12f })
        if (unread > 0) {
            right.addView(TextView(this).apply {
                text = unread.toString(); setTextColor(Color.WHITE); textSize = 10f; gravity = Gravity.CENTER; setTypeface(typeface, Typeface.BOLD)
                val lp = LinearLayout.LayoutParams(dp(20), dp(20)); lp.topMargin = dp(4); layoutParams = lp
                background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(Color.parseColor("#FF5252")) }
            })
        }
        item.addView(right)

        item.setOnClickListener { openChat(name) }
        container.addView(item)
    }

    private fun openChat(name: String) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("CONTACT_NAME", name)
        startActivity(intent)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
