package com.example.tienda_bonbin.data
import com.example.tienda_bonbin.repository.SessionRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "http://10.39.168.46:8081/"
    private var apiService: ApiService? = null

    fun getApiService(sessionRepository: SessionRepository): ApiService {
        if (apiService == null) {


            // Creamos nuestro interceptor de autorización, pasándole el repositorio que recibimos.
            val authInterceptor = AuthInterceptor(sessionRepository)

            //Esto Activa el Logcat cuando la aplicacion se esta emulando en el dispositivo
            val loggingInterceptor = HttpLoggingInterceptor().apply  {
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
