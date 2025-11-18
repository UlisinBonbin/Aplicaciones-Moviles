package com.example.tienda_bonbin.data
import com.example.tienda_bonbin.repository.SessionRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "http://192.168.1.5:8081/"
    private var apiService: ApiService? = null

    /**
     * ✅ FUNCIÓN PÚBLICA SIMPLIFICADA
     * Ahora recibe el SessionRepository directamente.
     * Ya no necesita el 'Context'.
     *
     * @param sessionRepository La instancia del repositorio de sesión.
     */
    // ✅ 1. CAMBIA LA FIRMA DE LA FUNCIÓN
    fun getApiService(sessionRepository: SessionRepository): ApiService {
        if (apiService == null) {


            // B. Creamos nuestro interceptor de autorización, pasándole el repositorio que recibimos.
            val authInterceptor = AuthInterceptor(sessionRepository) // <--- ¡Usa el que viene como parámetro!

            // C, D, E y F se quedan exactamente igual...
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }
        return apiService!!
    }
}
