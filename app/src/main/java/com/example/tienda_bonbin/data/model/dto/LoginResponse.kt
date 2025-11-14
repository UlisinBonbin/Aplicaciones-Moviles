package com.example.tienda_bonbin.data.model.dto

import com.example.tienda_bonbin.data.model.Usuario

data class LoginResponse(val token: String,
                         val usuario: Usuario
)
