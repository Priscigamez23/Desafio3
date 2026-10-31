package com.example.appaprende.controller

import com.example.appaprende.model.Usuario
import com.example.appaprende.model.api.RetrofitClient

class UsuarioController {

    private val apiService = RetrofitClient.apiService

    suspend fun obtenerUsuarios(): List<Usuario> {
        return apiService.obtenerUsuarios()
    }

    suspend fun agregarUsuario(usuario: Usuario): Usuario {
        return apiService.agregarUsuario(usuario)
    }

    suspend fun buscarUsuario(correo: String): Usuario? {
        return apiService.obtenerUsuarios().find {
            it.correo.equals(correo, ignoreCase = true)
        }
    }

    suspend fun actualizarUsuario(usuarioActualizado: Usuario): Usuario {
        return apiService.actualizarUsuario(
            usuarioActualizado.id,
            usuarioActualizado
        )
    }

    suspend fun eliminarUsuario(id: String) {
        apiService.eliminarUsuario(id)
    }
}