package com.example.tienda_bonbin.data
import com.example.tienda_bonbin.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (private val sessionRepository: SessionRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. Obtener el token guardado desde DataStore.
        // Usamos runBlocking porque la interfaz de Interceptor no es 'suspend'.
        // Esto coge el valor más reciente del Flow y espera hasta que esté disponible.
        val token = runBlocking {
            sessionRepository.authTokenFlow.first()
        }

        // 2. Obtener la petición original que se va a realizar.
        val request = chain.request()

        // 3. Si no hay token (el usuario no ha iniciado sesión),
        // simplemente continuamos con la petición original sin modificarla.
        if (token == null) {
            return chain.proceed(request)
        }

        // 4. Si SÍ hay token, construimos una NUEVA petición.
        // Copiamos la original y le añadimos la cabecera "Authorization".
        val newRequest = request.newBuilder()
            .header("Authorization", "Bearer $token") // <-- LA LÍNEA MÁGICA
            .build()

        // 5. Procedemos con la nueva petición que ahora incluye el token.
        return chain.proceed(newRequest)
    }
}