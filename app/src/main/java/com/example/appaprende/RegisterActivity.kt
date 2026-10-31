package com.example.appaprende

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val txtVolverLogin = findViewById<TextView>(R.id.txtVolverLogin)

        txtVolverLogin.setOnClickListener {
            finish()
        }
    }
}
