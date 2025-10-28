// INICIO DEL CÓDIGO CORRECTO para settings.gradle.kts

pluginManagement {
    repositories {
        // Primero, busca en el portal de plugins de Gradle
        gradlePluginPortal()
        // Luego, en el repositorio de Google
        google()
        // Finalmente, en Maven Central
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // El mismo orden para las librerías
        google()
        mavenCentral()
    }
}

rootProject.name = "Tienda-Bonbin"
include(":app")

// FIN DEL CÓDIGO
