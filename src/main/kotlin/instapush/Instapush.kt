package instapush

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.http4k.client.OkHttp
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status


class Instapush(private val id: String, private val secret: String) {
    companion object {
        const val POST_URL = "https://api.instapush.im/post"
        const val HEADER_APP_ID = "x-instapush-appid"
        const val HEADER_APP_SECRET = "x-instapush-appsecret"
    }

    private var http = OkHttp()
    private val gson = Gson()

    fun push(event: String, vararg trackers: Pair<String, String>): String? {
        val request = Request(Method.POST, POST_URL)
                .header(HEADER_APP_ID, id)
                .header(HEADER_APP_SECRET, secret)
                .header("Content-Type", "application/json")
                .body(serializeEvent(event, trackers))
        val response = http(request)

        return getErrorFromResult(response)
    }

    private fun serializeEvent(event: String, trackers: Array<out Pair<String, String>>): String {
        val rootObj = JsonObject()
        rootObj.addProperty("event", event)

        val trackerObj = JsonObject()
        trackers.forEach {
            trackerObj.addProperty(it.first, it.second)
        }
        rootObj.add("trackers", trackerObj)

        return Gson().toJson(rootObj)
    }

    private fun getErrorFromResult(response: Response): String? {
        val responseModel = gson.fromJson(response.bodyString(), InstapushPostResponseModel::class.java)

        if (response.status != Status.OK) {
            return "Response status was " + response.status + ", expected 200"
        }

        if (responseModel == null) {
            return "Response was null"
        }

        if (responseModel.error) {
            return responseModel.msg
        }

        return null
    }
}
