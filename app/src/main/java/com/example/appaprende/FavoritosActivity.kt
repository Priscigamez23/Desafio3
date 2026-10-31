package com.example.appaprende

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.RecursoController
import com.example.appaprende.model.Recurso
import kotlinx.coroutines.launch

class FavoritosActivity : AppCompatActivity() {

    private val recursoController = RecursoController()

    private lateinit var contenedor: LinearLayout
    private lateinit var txtEstado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        contenedor = findViewById(R.id.contenedorFavoritos)
        txtEstado = findViewById(R.id.txtEstadoFavoritos)

        val btnVolver = findViewById<Button>(R.id.btnVolverFavoritos)

        btnVolver.setOnClickListener {
            finish()
        }

        cargarFavoritos()
    }

    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }

    private fun cargarFavoritos() {
        lifecycleScope.launch {
            try {
                val idsFavoritos =
                    FavoritosManager.obtenerFavoritos(this@FavoritosActivity)

                val recursos = recursoController.obtenerRecursos()

                val recursosFavoritos = recursos.filter { recurso ->
                    !recurso.id.isNullOrBlank() &&
                            idsFavoritos.contains(recurso.id)
                }

                contenedor.removeAllViews()

                if (recursosFavoritos.isEmpty()) {
                    txtEstado.text = "Todavía no tienes recursos favoritos."
                } else {
                    txtEstado.text =
                        "Mis favoritos: ${recursosFavoritos.size}"

                    for (recurso in recursosFavoritos) {
                        crearTarjeta(recurso)
                    }
                }

            } catch (e: Exception) {
                txtEstado.text =
                    "Error al cargar favoritos: ${e.message}"
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
        titulo.setTextColor(android.graphics.Color.rgb(38, 59, 112))

        val descripcion = TextView(this)
        descripcion.text = recurso.descripcion
        descripcion.textSize = 15f
        descripcion.setTextColor(android.graphics.Color.DKGRAY)

        val categoria = TextView(this)
        categoria.text = "Categoría: ${recurso.categoria}"

        val autor = TextView(this)
        autor.text = "Autor: ${recurso.autor}"

        val btnQuitar = Button(this)
        btnQuitar.text = "Quitar de favoritos"

        btnQuitar.setOnClickListener {
            val idRecurso = recurso.id

            if (idRecurso.isNullOrBlank()) {
                Toast.makeText(
                    this,
                    "Este recurso no tiene identificador.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            FavoritosManager.quitarFavorito(this, idRecurso)

            Toast.makeText(
                this,
                "Recurso quitado de favoritos",
                Toast.LENGTH_SHORT
            ).show()

            cargarFavoritos()
        }

        tarjeta.addView(titulo)
        tarjeta.addView(descripcion)
        tarjeta.addView(categoria)
        tarjeta.addView(autor)
        tarjeta.addView(btnQuitar)

        val parametros = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        parametros.bottomMargin = 20

        contenedor.addView(tarjeta, parametros)
    }
}
