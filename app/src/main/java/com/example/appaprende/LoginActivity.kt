package com.example.appaprende

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.UsuarioController
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val usuarioController = UsuarioController()

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
                    Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !preferencias.contains(correo) -> {
                    Toast.makeText(
                        this,
                        "Esta cuenta no está registrada",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                preferencias.getString(correo, "") != contrasena -> {
                    Toast.makeText(
                        this,
                        "Correo o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    btnIniciarSesion.isEnabled = false

                    lifecycleScope.launch {
                        try {
                            val usuario = usuarioController.buscarUsuario(correo)

                            if (usuario == null) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "La cuenta no existe en el servidor",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                val intent = Intent(
                                    this@LoginActivity,
                                    HomeActivity::class.java
                                )

                                intent.putExtra("nombreUsuario", usuario.nombre)
                                intent.putExtra("rolUsuario", usuario.rol)

                                startActivity(intent)
                                finish()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Error al conectar con el servidor: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            btnIniciarSesion.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
