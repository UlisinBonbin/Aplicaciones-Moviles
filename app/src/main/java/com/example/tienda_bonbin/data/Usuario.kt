package com.example.tienda_bonbin.data
import androidx.room.Entity
import androidx.room.PrimaryKey
    @Entity(tableName = "usuarios")
    data class Usuario(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val nombre: String,
        val apellido: String,
        val correo: String,
        val clave: String,
        val direccion: String,
        val rol: String
    )
