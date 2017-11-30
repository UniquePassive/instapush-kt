package test.instapush

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
    }

    @Test
    fun syncTest() {
        val error = ip.push("update",
                Pair("name", "Test Name"),
                Pair("status", "This is a test status."))

        assertNull(error)
    }
}
