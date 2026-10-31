package com.example.appaprende

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenida)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        val preferencias = getSharedPreferences("AppAprende", MODE_PRIVATE)

        val nombre = intent.getStringExtra("nombreUsuario")

        txtBienvenida.text = "¡Bienvenido, ${nombre ?: "estudiante"}!"

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
