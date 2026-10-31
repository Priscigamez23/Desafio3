package com.example.appaprende

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtCorreo = findViewById<EditText>(R.id.edtCorreoRegistro)
        val edtContrasena = findViewById<EditText>(R.id.edtContrasenaRegistro)
        val edtConfirmar = findViewById<EditText>(R.id.edtConfirmarContrasena)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val txtVolverLogin = findViewById<TextView>(R.id.txtVolverLogin)

        val preferencias = getSharedPreferences("AppAprende", MODE_PRIVATE)

        txtVolverLogin.setOnClickListener {
            finish()
        }

        btnRegistrar.setOnClickListener {

            val nombre = edtNombre.text.toString().trim()
            val correo = edtCorreo.text.toString().trim()
            val contrasena = edtContrasena.text.toString()
            val confirmar = edtConfirmar.text.toString()

            when {
                nombre.isEmpty() || correo.isEmpty() ||
                        contrasena.isEmpty() || confirmar.isEmpty() -> {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }

                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                    Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
                }

                contrasena.length < 8 -> {
                    Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                }

                contrasena != confirmar -> {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }

                preferencias.contains(correo) -> {
                    Toast.makeText(this, "Este correo ya está registrado", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    preferencias.edit()
                        .putString(correo, contrasena)
                        .putString(correo + "_nombre", nombre)
                        .apply()

                    edtNombre.text.clear()
                    edtCorreo.text.clear()
                    edtContrasena.text.clear()
                    edtConfirmar.text.clear()

                    Toast.makeText(this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show()

                    finish()
                }
            }
        }
    }
}
