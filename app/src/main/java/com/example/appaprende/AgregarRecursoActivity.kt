package com.example.appaprende

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.RecursoController
import com.example.appaprende.model.Recurso
import kotlinx.coroutines.launch
import java.util.UUID

class AgregarRecursoActivity : AppCompatActivity() {

    private val recursoController = RecursoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_recurso)

        val edtTitulo = findViewById<EditText>(R.id.edtTituloRecurso)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcionRecurso)
        val spCategoria = findViewById<Spinner>(R.id.spCategoriaRecurso)
        val edtEnlace = findViewById<EditText>(R.id.edtEnlaceRecurso)
        val edtAutor = findViewById<EditText>(R.id.edtAutorRecurso)

        val btnGuardar = findViewById<Button>(R.id.btnGuardarRecurso)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarRecurso)

        val categorias = listOf("Libros", "Videos", "Artículos")

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spCategoria.adapter = adaptador

        btnCancelar.setOnClickListener {
            finish()
        }

        btnGuardar.setOnClickListener {

            val titulo = edtTitulo.text.toString().trim()
            val descripcion = edtDescripcion.text.toString().trim()
            val categoria = spCategoria.selectedItem.toString()
            val enlace = edtEnlace.text.toString().trim()
            val autor = edtAutor.text.toString().trim()

            when {
                titulo.isEmpty() || descripcion.isEmpty() ||
                        enlace.isEmpty() || autor.isEmpty() -> {
                    Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                !android.util.Patterns.WEB_URL.matcher(enlace).matches() -> {
                    Toast.makeText(
                        this,
                        "Ingresa un enlace válido",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {

                    btnGuardar.isEnabled = false

                    lifecycleScope.launch {
                        try {
                            val recurso = Recurso(
                                id = UUID.randomUUID().toString(),
                                titulo = titulo,
                                descripcion = descripcion,
                                categoria = categoria,
                                enlace = enlace,
                                autor = autor
                            )

                            recursoController.agregarRecurso(recurso)

                            Toast.makeText(
                                this@AgregarRecursoActivity,
                                "¡Recurso guardado correctamente!",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()

                        } catch (e: Exception) {
                            Toast.makeText(
                                this@AgregarRecursoActivity,
                                "Error al guardar: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        } finally {
                            btnGuardar.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
