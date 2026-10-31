package com.example.appaprende.model

data class Recurso(
    val id: String? = null,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val enlace: String,
    val autor: String
)