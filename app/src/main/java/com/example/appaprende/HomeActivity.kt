package com.example.appaprende

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.RecursoController
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val recursoController = RecursoController()

    private var rolUsuario: String = "estudiante"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenida)
        val edtBuscar = findViewById<EditText>(R.id.edtBuscar)

        val btnLibros = findViewById<Button>(R.id.btnLibros)
        val btnVideos = findViewById<Button>(R.id.btnVideos)
        val btnArticulos = findViewById<Button>(R.id.btnArticulos)
        val btnAgregarRecurso = findViewById<Button>(R.id.btnAgregarRecurso)
        val btnVerFavoritos = findViewById<Button>(R.id.btnVerFavoritos)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        val nombre = intent.getStringExtra("nombreUsuario")
        rolUsuario = intent.getStringExtra("rolUsuario") ?: "estudiante"

        txtBienvenida.text = "¡Bienvenido, ${nombre ?: "estudiante"}!"

        // Solo los docentes pueden agregar recursos.
        if (rolUsuario.equals("docente", ignoreCase = true)) {
            btnAgregarRecurso.visibility = View.VISIBLE
        } else {
            btnAgregarRecurso.visibility = View.GONE
        }

        btnLibros.setOnClickListener {
            abrirRecursos("Libros")
        }

        btnVideos.setOnClickListener {
            abrirRecursos("Videos")
        }

        btnArticulos.setOnClickListener {
            abrirRecursos("Artículos")
        }

        btnAgregarRecurso.setOnClickListener {
            if (rolUsuario.equals("docente", ignoreCase = true)) {
                val intent = Intent(this, AgregarRecursoActivity::class.java)
                startActivity(intent)
            }
        }

        edtBuscar.setOnEditorActionListener { _, _, _ ->
            val texto = edtBuscar.text.toString().trim()

            if (texto.isEmpty()) {
                edtBuscar.error = "Escribe el nombre del recurso"
            } else {
                val intent = Intent(this, RecursosActivity::class.java)
                intent.putExtra("busqueda", texto)
                intent.putExtra("categoria", "Todos")
                intent.putExtra("rolUsuario", rolUsuario)
                startActivity(intent)
            }

            true
        }

        btnVerFavoritos.setOnClickListener {
            val intent = Intent(this, FavoritosActivity::class.java)
            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        probarConexionAPI()
    }

    private fun abrirRecursos(categoria: String) {
        val intent = Intent(this, RecursosActivity::class.java)
        intent.putExtra("categoria", categoria)
        intent.putExtra("rolUsuario", rolUsuario)
        startActivity(intent)
    }

    private fun probarConexionAPI() {
        lifecycleScope.launch {
            try {
                val recursos = recursoController.obtenerRecursos()

                Toast.makeText(
                    this@HomeActivity,
                    "Conexión exitosa. Recursos encontrados: ${recursos.size}",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@HomeActivity,
                    "Error de conexión: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
