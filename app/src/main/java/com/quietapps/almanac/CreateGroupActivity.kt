package com.quietapps.almanac

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateGroupActivity : AppCompatActivity() {

    private val selectedContacts = mutableListOf<String>()
    private val contacts = listOf(
        Pair("Алексей", "#607D8B"),
        Pair("Мария", "#795548"),
        Pair("Дмитрий", "#455A64"),
        Pair("Елена", "#8E24AA"),
        Pair("Иван", "#00897B")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        supportActionBar?.title = "Новая группа"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val container = findViewById<LinearLayout>(R.id.contactsCheckList)
        for ((name, color) in contacts) {
            addContactRow(container, name, color)
        }

        findViewById<android.widget.Button>(R.id.btnCreateGroup).setOnClickListener {
            val groupName = findViewById<EditText>(R.id.groupNameInput).text.toString().trim()
            if (groupName.isEmpty()) {
                Toast.makeText(this, "Введите название группы", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedContacts.isEmpty()) {
                Toast.makeText(this, "Выберите хотя бы одного участника", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val result = Intent()
            result.putExtra("GROUP_NAME", groupName)
            result.putStringArrayListExtra("GROUP_MEMBERS", ArrayList(selectedContacts))
            setResult(RESULT_OK, result)
            finish()
        }
    }

    private fun addContactRow(container: LinearLayout, name: String, color: String) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), 0, dp(16), 0)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp(64))
            isClickable = true
            isFocusable = true
        }

        val avatar = TextView(this).apply {
            text = name.first().uppercase()
            setTextColor(Color.WHITE)
            textSize = 18f
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(dp(40), dp(40))
            background = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(Color.parseColor(color))
            }
        }
        row.addView(avatar)

        val nameView = TextView(this).apply {
            text = name
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(dp(12), 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        row.addView(nameView)

        val checkBox = CheckBox(this)
        row.addView(checkBox)

        row.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked
            if (checkBox.isChecked) selectedContacts.add(name) else selectedContacts.remove(name)
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && name !in selectedContacts) selectedContacts.add(name)
            else if (!isChecked) selectedContacts.remove(name)
        }

        container.addView(row)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
