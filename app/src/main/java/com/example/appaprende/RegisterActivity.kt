package com.example.appaprende

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.UsuarioController
import com.example.appaprende.model.Usuario
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private val usuarioController = UsuarioController()

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

            val tieneMayuscula = contrasena.any { it.isUpperCase() }
            val tieneMinuscula = contrasena.any { it.isLowerCase() }
            val tieneNumero = contrasena.any { it.isDigit() }
            val tieneEspecial = contrasena.any {
                it in "!@#$%^&*"
            }

            when {
                nombre.isEmpty() || correo.isEmpty() ||
                        contrasena.isEmpty() || confirmar.isEmpty() -> {

                    Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {

                    Toast.makeText(
                        this,
                        "Ingresa un correo válido",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                contrasena.length < 12 -> {

                    Toast.makeText(
                        this,
                        "La contraseña debe tener al menos 12 caracteres",
                        Toast.LENGTH_LONG
                    ).show()
                }

                !tieneMayuscula -> {

                    Toast.makeText(
                        this,
                        "La contraseña debe incluir una mayúscula",
                        Toast.LENGTH_LONG
                    ).show()
                }

                !tieneMinuscula -> {

                    Toast.makeText(
                        this,
                        "La contraseña debe incluir una minúscula",
                        Toast.LENGTH_LONG
                    ).show()
                }

                !tieneNumero -> {

                    Toast.makeText(
                        this,
                        "La contraseña debe incluir un número",
                        Toast.LENGTH_LONG
                    ).show()
                }

                !tieneEspecial -> {

                    Toast.makeText(
                        this,
                        "La contraseña debe incluir un carácter especial (!@#$%^&*)",
                        Toast.LENGTH_LONG
                    ).show()
                }

                contrasena != confirmar -> {

                    Toast.makeText(
                        this,
                        "Las contraseñas no coinciden",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                preferencias.contains(correo) -> {

                    Toast.makeText(
                        this,
                        "Este correo ya está registrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    btnRegistrar.isEnabled = false

                    lifecycleScope.launch {
                        try {
                            val usuarioExistente =
                                usuarioController.buscarUsuario(correo)

                            if (usuarioExistente != null) {

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Este correo ya está registrado",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {

                                val nombreCorreo = correo
                                    .substringBefore("@")
                                    .lowercase()

                                val rolAsignado = if (
                                    nombreCorreo.contains("docente") ||
                                    nombreCorreo.contains("profesor")
                                ) {
                                    "docente"
                                } else {
                                    "estudiante"
                                }

                                val usuario = Usuario(
                                    id = UUID.randomUUID().toString(),
                                    nombre = nombre,
                                    correo = correo,
                                    rol = rolAsignado
                                )

                                usuarioController.agregarUsuario(usuario)

                                preferencias.edit()
                                    .putString(correo, contrasena)
                                    .putString(correo + "_nombre", nombre)
                                    .apply()

                                edtNombre.text.clear()
                                edtCorreo.text.clear()
                                edtContrasena.text.clear()
                                edtConfirmar.text.clear()

                                Toast.makeText(
                                    this@RegisterActivity,
                                    "¡Registro exitoso! Rol asignado: $rolAsignado",
                                    Toast.LENGTH_LONG
                                ).show()

                                finish()
                            }

                        } catch (e: Exception) {

                            Toast.makeText(
                                this@RegisterActivity,
                                "No se pudo completar el registro: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()

                        } finally {
                            btnRegistrar.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
