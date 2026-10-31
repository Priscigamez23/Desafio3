package com.example.appaprende

import android.content.Context

object FavoritosManager {

    private const val NOMBRE_PREFERENCIAS = "AppAprendeFavoritos"
    private const val CLAVE_FAVORITOS = "recursos_favoritos"

    fun obtenerFavoritos(context: Context): Set<String> {
        val preferencias = context.getSharedPreferences(
            NOMBRE_PREFERENCIAS,
            Context.MODE_PRIVATE
        )

        return preferencias.getStringSet(
            CLAVE_FAVORITOS,
            emptySet()
        )?.toSet() ?: emptySet()
    }

    fun agregarFavorito(context: Context, id: String) {
        val favoritos = obtenerFavoritos(context).toMutableSet()
        favoritos.add(id)

        guardarFavoritos(context, favoritos)
    }

    fun quitarFavorito(context: Context, id: String) {
        val favoritos = obtenerFavoritos(context).toMutableSet()
        favoritos.remove(id)

        guardarFavoritos(context, favoritos)
    }

    fun esFavorito(context: Context, id: String): Boolean {
        return obtenerFavoritos(context).contains(id)
    }

    private fun guardarFavoritos(context: Context, favoritos: Set<String>) {
        val preferencias = context.getSharedPreferences(
            NOMBRE_PREFERENCIAS,
            Context.MODE_PRIVATE
        )

        preferencias.edit()
            .putStringSet(CLAVE_FAVORITOS, favoritos)
            .apply()
    }
}
