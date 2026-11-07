package com.quietapps.almanac

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesContainer: LinearLayout
    private lateinit var messagesScroll: ScrollView
    private lateinit var input: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var micButton: ImageButton
    private var contactName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        contactName = intent.getStringExtra("CONTACT_NAME") ?: "Чат"
        supportActionBar?.title = contactName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        messagesContainer = findViewById(R.id.messagesContainer)
        messagesScroll = findViewById(R.id.messagesScroll)
        input = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        micButton = findViewById(R.id.micButton)
        val attachButton = findViewById<ImageButton>(R.id.attachButton)

        addMessage("Привет! Это тестовое сообщение из тёмного чата.", isOutgoing = false)

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    sendButton.visibility = View.GONE
                    micButton.visibility = View.VISIBLE
                } else {
                    sendButton.visibility = View.VISIBLE
                    micButton.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        sendButton.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                addMessage(text, isOutgoing = true)
                input.text.clear()
            }
        }

        micButton.setOnClickListener { Toast.makeText(this, "Запись голоса (скоро)", Toast.LENGTH_SHORT).show() }
        attachButton.setOnClickListener { showAttachmentsMenu() }
    }

    private fun showAttachmentsMenu() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_attachments, null)
        val dialog = AlertDialog.Builder(this, R.style.Theme_Almanac_Dialog).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        dialogView.findViewById<LinearLayout>(R.id.attachPhoto).setOnClickListener { Toast.makeText(this, "Фото (скоро)", Toast.LENGTH_SHORT).show(); dialog.dismiss() }
        dialogView.findViewById<LinearLayout>(R.id.attachFile).setOnClickListener { Toast.makeText(this, "Файл (скоро)", Toast.LENGTH_SHORT).show(); dialog.dismiss() }
        dialogView.findViewById<LinearLayout>(R.id.attachLocation).setOnClickListener { Toast.makeText(this, "Локация (скоро)", Toast.LENGTH_SHORT).show(); dialog.dismiss() }
    }

    private fun showMessageContextMenu(text: String, bubbleView: View) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_message_context, null)
        val dialog = AlertDialog.Builder(this, R.style.Theme_Almanac_Dialog).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialogView.findViewById<TextView>(R.id.msgCtxCopy).setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("message", text))
            Toast.makeText(this, "Скопировано", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.msgCtxReply).setOnClickListener {
            input.setText("Ответ: $text")
            input.setSelection(input.text.length)
            dialog.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.msgCtxDelete).setOnClickListener {
            messagesContainer.removeView(bubbleView)
            dialog.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { menuInflater.inflate(R.menu.chat_menu, menu); return true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { finish(); return true }
            R.id.action_profile -> { startActivity(Intent(this, ProfileActivity::class.java).putExtra("PROFILE_NAME", contactName)); return true }
            R.id.action_call -> { startActivity(Intent(this, CallActivity::class.java).putExtra("CONTACT_NAME", contactName)); return true }
            R.id.action_search -> { Toast.makeText(this, "Поиск (скоро)", Toast.LENGTH_SHORT).show(); return true }
            R.id.action_clear -> { messagesContainer.removeAllViews(); Toast.makeText(this, "Очищено", Toast.LENGTH_SHORT).show(); return true }
            R.id.action_block -> { Toast.makeText(this, "Заблокирован", Toast.LENGTH_SHORT).show(); return true }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addMessage(text: String, isOutgoing: Boolean) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        val bubbleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = if (isOutgoing) Gravity.END else Gravity.START
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(if (isOutgoing) 60 else 0, 0, if (isOutgoing) 0 else 60, 12)
            params.gravity = if (isOutgoing) Gravity.END else Gravity.START
            layoutParams = params
            
            setOnLongClickListener {
                showMessageContextMenu(text, this)
                true
            }
        }

        val textView = TextView(this).apply {
            this.text = text; textSize = 16f; setTextColor(Color.WHITE); setPadding(24, 16, 24, 16)
            background = GradientDrawable().apply {
                setColor(if (isOutgoing) Color.parseColor("#2B5278") else Color.parseColor("#182533"))
                if (isOutgoing) cornerRadii = floatArrayOf(32f, 32f, 32f, 32f, 32f, 32f, 0f, 0f)
                else cornerRadii = floatArrayOf(32f, 32f, 32f, 32f, 0f, 0f, 32f, 32f)
            }
        }

        val timeLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.BOTTOM
            setPadding(8, 0, 16, 4)
        }

        val timeView = TextView(this).apply {
            this.text = currentTime; textSize = 11f; setTextColor(Color.parseColor("#8B9BA5"))
        }
        timeLayout.addView(timeView)

        // Галочка для исходящих
        if (isOutgoing) {
            val checkView = TextView(this).apply {
                this.text = "✓"
                textSize = 12f
                setTextColor(Color.parseColor("#8B9BA5"))
                setPadding(6, 0, 0, 0)
            }
            timeLayout.addView(checkView)
        }

        bubbleLayout.addView(textView)
        bubbleLayout.addView(timeLayout)
        messagesContainer.addView(bubbleLayout)
        messagesScroll.post { messagesScroll.fullScroll(View.FOCUS_DOWN) }
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
