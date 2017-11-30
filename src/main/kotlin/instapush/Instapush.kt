package instapush

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonObject


class Instapush(private val id: String, private val secret: String) {
    companion object {
        const val POST_URL = "https://api.instapush.im/post"
        const val HEADER_APP_ID = "x-instapush-appid"
        const val HEADER_APP_SECRET = "x-instapush-appsecret"
    }

    fun push(event: String,
             vararg trackers: Pair<String, String>,
             callback: (String?) -> Unit = { _ -> }) {

        POST_URL.httpPost()
                .header(Pair(HEADER_APP_ID, id),
                        Pair(HEADER_APP_SECRET, secret),
                        Pair("Content-Type", "application/json"))
                .body(serializeEvent(event, trackers))
                .responseObject<InstapushPostResponseModel> { request, _, result ->
                    callback(getErrorFromResult(result))
                }
    }

    fun pushSync(event: String, vararg trackers: Pair<String, String>): String? {
        val (_, _, result) = POST_URL
                .httpPost()
                .header(Pair(HEADER_APP_ID, id),
                        Pair(HEADER_APP_SECRET, secret),
                        Pair("Content-Type", "application/json"))
                .body(serializeEvent(event, trackers))
                .responseObject<InstapushPostResponseModel>()

        return getErrorFromResult(result)
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

    private fun getErrorFromResult(result: Result<InstapushPostResponseModel, FuelError>): String? {
        val (response, error) = result

        if (error != null && error.exception is Result.Failure<*, *>) {
            return error.toString()
        } else if (response == null) {
            return "component1 was null"
        } else if (response.error) {
            return response.msg
        } else if (response.status != 200) {
            return "response status was " + response.status + ", expected 200"
        }

        return null
    }
}
