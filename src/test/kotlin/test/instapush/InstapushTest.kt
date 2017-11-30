package test.instapush

import com.github.kittinunf.fuel.Fuel
import instapush.Instapush
import org.junit.Test
import kotlin.test.assertNull


class InstapushTest {
    companion object {
        // Get at https://instapush.im/
        private const val APP_ID = ""
        private const val APP_SECRET = ""
    }

    private val ip: Instapush

    init {
        ip = Instapush(APP_ID, APP_SECRET)

        Fuel.testMode()
    }

    @Test
    fun asyncTest() {
        var error: String? = null

        ip.push("update",
                Pair("name", "Test Name"), Pair("status", "This is a test status."),
                callback = {
                    err ->
                    if (err != null) {
                        error = err
                    }
                })

        assertNull(error)
    }

    @Test
    fun syncTest() {
        val error = ip.pushSync("update",
                Pair("name", "Test Name"),
                Pair("status", "This is a test status."))

        assertNull(error)
    }
}
