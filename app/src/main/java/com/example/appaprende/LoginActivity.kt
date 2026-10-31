package com.example.appaprende

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtCorreo = findViewById<EditText>(R.id.edtCorreo)
        val edtContrasena = findViewById<EditText>(R.id.edtContrasena)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val txtIrRegistro = findViewById<TextView>(R.id.txtIrRegistro)

        val preferencias = getSharedPreferences("AppAprende", MODE_PRIVATE)

        txtIrRegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnIniciarSesion.setOnClickListener {

            val correo = edtCorreo.text.toString().trim()
            val contrasena = edtContrasena.text.toString()

            when {
                correo.isEmpty() || contrasena.isEmpty() -> {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }

                !preferencias.contains(correo) -> {
                    Toast.makeText(this, "Esta cuenta no está registrada", Toast.LENGTH_SHORT).show()
                }

                preferencias.getString(correo, "") != contrasena -> {
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(this, "¡Bienvenido a AppAprende!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
