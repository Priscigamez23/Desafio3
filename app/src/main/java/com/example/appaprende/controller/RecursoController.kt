package com.example.appaprende.controller

import com.example.appaprende.model.Recurso
import com.example.appaprende.model.api.RetrofitClient

class RecursoController {

    private val apiService = RetrofitClient.apiService

    suspend fun obtenerRecursos(): List<Recurso> {
        return apiService.obtenerRecursos()
    }

    suspend fun agregarRecurso(recurso: Recurso): Recurso {
        return apiService.agregarRecurso(recurso)
    }

    suspend fun buscarRecurso(titulo: String): List<Recurso> {
        return apiService.obtenerRecursos().filter {
            it.titulo.contains(titulo, ignoreCase = true)
        }
    }

    suspend fun actualizarRecurso(recursoActualizado: Recurso): Recurso {
        return apiService.actualizarRecurso(
            recursoActualizado.id,
            recursoActualizado
        )
    }

    suspend fun eliminarRecurso(id: String) {
        apiService.eliminarRecurso(id)
    }
}