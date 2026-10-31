package com.example.appaprende

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.RecursoController
import com.example.appaprende.model.Recurso
import kotlinx.coroutines.launch

class RecursosActivity : AppCompatActivity() {

    private val recursoController = RecursoController()

    private lateinit var contenedor: LinearLayout
    private lateinit var txtEstado: TextView
    private lateinit var categoria: String

    private var busqueda: String = ""
    private var rolUsuario: String = "estudiante"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursos)

        val txtCategoria = findViewById<TextView>(R.id.txtCategoria)
        txtEstado = findViewById(R.id.txtEstadoRecursos)
        contenedor = findViewById(R.id.contenedorRecursos)

        val btnVolver = findViewById<Button>(R.id.btnVolver)

        categoria = intent.getStringExtra("categoria") ?: "Todos"
        busqueda = intent.getStringExtra("busqueda") ?: ""
        rolUsuario = intent.getStringExtra("rolUsuario") ?: "estudiante"

        txtCategoria.text = if (busqueda.isNotEmpty()) {
            "Resultados de búsqueda: $busqueda"
        } else {
            "Categoría: $categoria"
        }

        btnVolver.setOnClickListener {
            finish()
        }

        cargarRecursos()
    }

    private fun cargarRecursos() {
        lifecycleScope.launch {
            try {
                txtEstado.text = "Cargando recursos..."

                val recursos = recursoController.obtenerRecursos()

                val recursosFiltrados = recursos.filter { recurso ->
                    val coincideCategoria =
                        categoria == "Todos" ||
                                recurso.categoria.equals(
                                    categoria,
                                    ignoreCase = true
                                )

                    val coincideBusqueda =
                        busqueda.isEmpty() ||
                                recurso.titulo.contains(
                                    busqueda,
                                    ignoreCase = true
                                ) ||
                                recurso.descripcion.contains(
                                    busqueda,
                                    ignoreCase = true
                                )

                    coincideCategoria && coincideBusqueda
                }

                contenedor.removeAllViews()

                if (recursosFiltrados.isEmpty()) {
                    txtEstado.text = "No se encontraron recursos."
                } else {
                    txtEstado.text =
                        "Recursos encontrados: ${recursosFiltrados.size}"

                    for (recurso in recursosFiltrados) {
                        crearTarjeta(recurso)
                    }
                }

            } catch (e: Exception) {
                txtEstado.text =
                    "Error al cargar recursos: ${e.message}"
            }
        }
    }

    private fun crearTarjeta(recurso: Recurso) {

        val tarjeta = LinearLayout(this)
        tarjeta.orientation = LinearLayout.VERTICAL
        tarjeta.setPadding(20, 20, 20, 20)
        tarjeta.setBackgroundColor(android.graphics.Color.WHITE)

        val titulo = TextView(this)
        titulo.text = recurso.titulo
        titulo.textSize = 19f
        titulo.setTextColor(
            android.graphics.Color.rgb(38, 59, 112)
        )

        val descripcion = TextView(this)
        descripcion.text = recurso.descripcion
        descripcion.textSize = 15f
        descripcion.setTextColor(android.graphics.Color.DKGRAY)

        val autor = TextView(this)
        autor.text = "Autor: ${recurso.autor}"
        autor.textSize = 13f

        val enlace = TextView(this)
        enlace.text = "Enlace: ${recurso.enlace}"
        enlace.textSize = 13f
        enlace.setTextColor(android.graphics.Color.BLUE)

        val filaBotones = LinearLayout(this)
        filaBotones.orientation = LinearLayout.HORIZONTAL

        val btnEditar = Button(this)
        btnEditar.text = "Editar"

        val btnEliminar = Button(this)
        btnEliminar.text = "Eliminar"

        val btnFavorito = Button(this)
        val idRecurso = recurso.id

        // Los estudiantes no pueden editar ni eliminar.
        if (!rolUsuario.equals("docente", ignoreCase = true)) {
            btnEditar.visibility = View.GONE
            btnEliminar.visibility = View.GONE
        }

        if (idRecurso.isNullOrBlank()) {
            btnFavorito.text = "Sin ID"
            btnFavorito.isEnabled = false
            btnEditar.isEnabled = false
            btnEliminar.isEnabled = false
        } else {

            actualizarBotonFavorito(btnFavorito, idRecurso)

            if (rolUsuario.equals("docente", ignoreCase = true)) {

                btnEditar.setOnClickListener {
                    mostrarDialogoEditar(recurso)
                }

                btnEliminar.setOnClickListener {
                    confirmarEliminar(recurso)
                }
            }

            btnFavorito.setOnClickListener {
                if (FavoritosManager.esFavorito(this, idRecurso)) {
                    FavoritosManager.quitarFavorito(this, idRecurso)
                    btnFavorito.text = "☆ Favorito"
                    mostrarMensaje("Recurso quitado de favoritos")
                } else {
                    FavoritosManager.agregarFavorito(this, idRecurso)
                    btnFavorito.text = "★ Favorito"
                    mostrarMensaje("Recurso agregado a favoritos")
                }
            }
        }

        if (rolUsuario.equals("docente", ignoreCase = true)) {
            filaBotones.addView(
                btnEditar,
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            )

            filaBotones.addView(
                btnEliminar,
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            )
        }

        filaBotones.addView(
            btnFavorito,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        tarjeta.addView(titulo)
        tarjeta.addView(descripcion)
        tarjeta.addView(autor)
        tarjeta.addView(enlace)
        tarjeta.addView(filaBotones)

        val parametros = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        parametros.bottomMargin = 20

        contenedor.addView(tarjeta, parametros)
    }

    private fun actualizarBotonFavorito(
        boton: Button,
        id: String
    ) {
        boton.text = if (FavoritosManager.esFavorito(this, id)) {
            "★ Favorito"
        } else {
            "☆ Favorito"
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun mostrarDialogoEditar(recurso: Recurso) {

        if (!rolUsuario.equals("docente", ignoreCase = true)) {
            mostrarMensaje("No tienes permiso para editar recursos")
            return
        }

        val idRecurso = recurso.id

        if (idRecurso.isNullOrBlank()) {
            mostrarMensaje("El recurso no tiene identificador.")
            return
        }

        val formulario = LinearLayout(this)
        formulario.orientation = LinearLayout.VERTICAL
        formulario.setPadding(30, 10, 30, 10)

        val edtTitulo = EditText(this)
        edtTitulo.hint = "Título"
        edtTitulo.setText(recurso.titulo)

        val edtDescripcion = EditText(this)
        edtDescripcion.hint = "Descripción"
        edtDescripcion.setText(recurso.descripcion)

        val spCategoria = Spinner(this)
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
        spCategoria.setSelection(
            categorias.indexOf(recurso.categoria).coerceAtLeast(0)
        )

        val edtEnlace = EditText(this)
        edtEnlace.hint = "Enlace"
        edtEnlace.setText(recurso.enlace)

        val edtAutor = EditText(this)
        edtAutor.hint = "Autor"
        edtAutor.setText(recurso.autor)

        formulario.addView(edtTitulo)
        formulario.addView(edtDescripcion)
        formulario.addView(spCategoria)
        formulario.addView(edtEnlace)
        formulario.addView(edtAutor)

        val dialogo = AlertDialog.Builder(this)
            .setTitle("Editar recurso")
            .setView(formulario)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar", null)
            .create()

        dialogo.setOnShowListener {
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {

                    val titulo = edtTitulo.text.toString().trim()
                    val descripcion =
                        edtDescripcion.text.toString().trim()
                    val nuevaCategoria =
                        spCategoria.selectedItem.toString()
                    val enlace = edtEnlace.text.toString().trim()
                    val autor = edtAutor.text.toString().trim()

                    if (
                        titulo.isEmpty() ||
                        descripcion.isEmpty() ||
                        enlace.isEmpty() ||
                        autor.isEmpty()
                    ) {
                        mostrarMensaje("Completa todos los campos")
                        return@setOnClickListener
                    }

                    if (!android.util.Patterns.WEB_URL.matcher(enlace).matches()) {
                        mostrarMensaje("Ingresa un enlace válido")
                        return@setOnClickListener
                    }

                    val recursoActualizado = Recurso(
                        id = idRecurso,
                        titulo = titulo,
                        descripcion = descripcion,
                        categoria = nuevaCategoria,
                        enlace = enlace,
                        autor = autor
                    )

                    lifecycleScope.launch {
                        try {
                            recursoController.actualizarRecurso(
                                recursoActualizado
                            )

                            mostrarMensaje(
                                "Recurso actualizado correctamente"
                            )

                            dialogo.dismiss()
                            cargarRecursos()

                        } catch (e: Exception) {
                            mostrarMensaje(
                                "Error al actualizar: ${e.message}"
                            )
                        }
                    }
                }
        }

        dialogo.show()
    }

    private fun confirmarEliminar(recurso: Recurso) {

        if (!rolUsuario.equals("docente", ignoreCase = true)) {
            mostrarMensaje("No tienes permiso para eliminar recursos")
            return
        }

        val idRecurso = recurso.id

        if (idRecurso.isNullOrBlank()) {
            mostrarMensaje("El recurso no tiene identificador.")
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Eliminar recurso")
            .setMessage(
                "¿Estás segura de que deseas eliminar '${recurso.titulo}'?"
            )
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                lifecycleScope.launch {
                    try {
                        recursoController.eliminarRecurso(idRecurso)

                        FavoritosManager.quitarFavorito(
                            this@RecursosActivity,
                            idRecurso
                        )

                        mostrarMensaje(
                            "Recurso eliminado correctamente"
                        )

                        cargarRecursos()

                    } catch (e: Exception) {
                        mostrarMensaje(
                            "Error al eliminar: ${e.message}"
                        )
                    }
                }
            }
            .show()
    }
}
