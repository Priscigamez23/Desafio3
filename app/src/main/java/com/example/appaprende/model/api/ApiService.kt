package com.example.appaprende.model.api

import com.example.appaprende.model.Recurso
import com.example.appaprende.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ================= RECURSOS =================

    @GET("recursos")
    suspend fun obtenerRecursos(): List<Recurso>

    @GET("recursos/{id}")
    suspend fun obtenerRecurso(
        @Path("id") id: String
    ): Recurso

    @POST("recursos")
    suspend fun agregarRecurso(
        @Body recurso: Recurso
    ): Recurso

    @PUT("recursos/{id}")
    suspend fun actualizarRecurso(
        @Path("id") id: String,
        @Body recurso: Recurso
    ): Recurso

    @DELETE("recursos/{id}")
    suspend fun eliminarRecurso(
        @Path("id") id: String
    ): Response<Unit>


    // ================= USUARIOS =================

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

    @GET("usuarios/{id}")
    suspend fun obtenerUsuario(
        @Path("id") id: String
    ): Usuario

    @POST("usuarios")
    suspend fun agregarUsuario(
        @Body usuario: Usuario
    ): Usuario

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: String,
        @Body usuario: Usuario
    ): Usuario

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(
        @Path("id") id: String
    ): Response<Unit>
}