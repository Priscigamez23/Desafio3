package com.example.appaprende

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appaprende.controller.RecursoController
import kotlinx.coroutines.launch

class RecursosActivity : AppCompatActivity() {

    private val recursoController = RecursoController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recursos)

        val txtCategoria = findViewById<TextView>(R.id.txtCategoria)
        val txtEstado = findViewById<TextView>(R.id.txtEstadoRecursos)
        val contenedor = findViewById<LinearLayout>(R.id.contenedorRecursos)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val categoria = intent.getStringExtra("categoria") ?: "Todos"

        txtCategoria.text = "Categoría: $categoria"

        btnVolver.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            try {
                val recursos = recursoController.obtenerRecursos()

                val recursosFiltrados = if (categoria == "Todos") {
                    recursos
                } else {
                    recursos.filter {
                        it.categoria.equals(categoria, ignoreCase = true)
                    }
                }

                contenedor.removeAllViews()

                if (recursosFiltrados.isEmpty()) {
                    txtEstado.text = "Todavía no hay recursos en esta categoría."
                } else {
                    txtEstado.text =
                        "Recursos encontrados: ${recursosFiltrados.size}"

                    for (recurso in recursosFiltrados) {

                        val tarjeta = LinearLayout(this@RecursosActivity)
                        tarjeta.orientation = LinearLayout.VERTICAL
                        tarjeta.setPadding(20, 20, 20, 20)
                        tarjeta.setBackgroundColor(
                            android.graphics.Color.WHITE
                        )

                        val titulo = TextView(this@RecursosActivity)
                        titulo.text = recurso.titulo
                        titulo.textSize = 19f
                        titulo.setTextColor(
                            android.graphics.Color.rgb(38, 59, 112)
                        )

                        val descripcion = TextView(this@RecursosActivity)
                        descripcion.text = recurso.descripcion
                        descripcion.textSize = 15f
                        descripcion.setTextColor(
                            android.graphics.Color.DKGRAY
                        )

                        val autor = TextView(this@RecursosActivity)
                        autor.text = "Autor: ${recurso.autor}"
                        autor.textSize = 13f

                        tarjeta.addView(titulo)
                        tarjeta.addView(descripcion)
                        tarjeta.addView(autor)

                        val parametros = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        parametros.bottomMargin = 20

                        contenedor.addView(tarjeta, parametros)
                    }
                }

            } catch (e: Exception) {
                txtEstado.text = "Error al cargar recursos: ${e.message}"
            }
        }
    }
}
