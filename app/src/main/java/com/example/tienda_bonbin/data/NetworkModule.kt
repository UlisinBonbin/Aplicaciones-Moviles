package com.example.tienda_bonbin.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkModule {
    // CAMBIA ESTA IP POR LA DE TU COMPUTADORA EN LA RED WIFI
    // Y ASEGÚRATE DE QUE EL PUERTO (ej: 8081) ES CORRECTO.
    // ¡DEBE TERMINAR CON UNA BARRA "/"!
    private const val BASE_URL = "http://10.172.55.46:8081/"


    // Interceptor para ver las llamadas en Logcat (muy útil para depurar)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente OkHttp que usa el interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Constructor de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // Usamos el cliente con el logger
        .addConverterFactory(GsonConverterFactory.create()) // Usa Gson para convertir JSON
        .build()

    /**
     * Esta es la instancia que usaremos en el resto de la app
     * para hacer las llamadas a la API.
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}