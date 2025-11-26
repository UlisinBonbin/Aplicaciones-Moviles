package com.example.tienda_bonbin.rules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
     val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {

    // Se ejecuta ANTES de cada test
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    // Se ejecuta DESPUÉS de cada test
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}