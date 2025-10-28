package com.example.tienda_bonbin

import android.app.Application
import com.example.tienda_bonbin.di.AppContainer
import com.example.tienda_bonbin.di.DefaultAppContainer

class BonbinApplication : Application() {
    /**
     * El contenedor de dependencias se crea una sola vez cuando la app se inicia.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
