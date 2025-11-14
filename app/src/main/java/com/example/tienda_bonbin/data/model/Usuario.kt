package com.example.tienda_bonbin.data.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("nombre")
    val nombre: String,


    @SerializedName("apellido")
    val apellido: String,


    @SerializedName("correo")
    val correo: String,

    // El nombre en el JSON de Spring es "contrasena". Lo mapeamos a la variable "contrasena" en Kotlin.
    @SerializedName("contrasena")
    val contrasena: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("rol")
    val rol: String


)
