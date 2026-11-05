package com.quietapps.almanac

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        supportActionBar?.title = "Контакты"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val container = findViewById<LinearLayout>(R.id.contactsContainer)

        addContactItem(container, "Алексей", "А", "#607D8B", "в сети")
        addContactItem(container, "Мария", "М", "#795548", "был(а) недавно")
        addContactItem(container, "Дмитрий", "Д", "#455A64", "не в сети")
        addContactItem(container, "Елена", "Е", "#8E24AA", "в сети")
        addContactItem(container, "Иван", "И", "#00897B", "был(а) 2 часа назад")
    }

    private fun addContactItem(
        container: LinearLayout,
        name: String,
        initial: String,
        avatarColor: String,
        status: String
    ) {
        val item = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), 0, dp(16), 0)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(72))
            isClickable = true
            isFocusable = true
            setOnClickListener {
                // Переход в чат с этим контактом
                val intent = Intent(this@ContactsActivity, ChatActivity::class.java)
                intent.putExtra("CONTACT_NAME", name)
                startActivity(intent)
            }
        }

        val avatar = TextView(this).apply {
            text = initial
            setTextColor(Color.WHITE)
            textSize = 20f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(dp(48), dp(48))
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor(avatarColor))
            }
        }
        item.addView(avatar)

        val textLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        textLayout.addView(TextView(this).apply {
            text = name
            setTextColor(Color.WHITE)
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
        })

        textLayout.addView(TextView(this).apply {
            text = status
            setTextColor(Color.parseColor("#8B9BA5"))
            textSize = 14f
        })

        item.addView(textLayout)
        container.addView(item)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
