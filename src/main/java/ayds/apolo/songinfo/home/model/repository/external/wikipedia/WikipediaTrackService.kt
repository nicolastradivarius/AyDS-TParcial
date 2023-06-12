package ayds.apolo.songinfo.home.model.repository.external.wikipedia

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

interface WikipediaTrackService {
    fun getSong(term: String): SpotifySong?
}

internal class WikipediaTrackServiceImpl(
    private val wikipediaAPI: WikipediaAPI
): WikipediaTrackService {
    override fun getSong(term: String): SpotifySong? {
        val callResponse: Response<String>
        try {
            callResponse = wikipediaAPI.getInfo(term).execute()
            val gson = Gson()
            val jobj: JsonObject = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val query = jobj["query"].asJsonObject
            val snippetObj = query["search"].asJsonArray.firstOrNull()
            if (snippetObj != null) {
                val snippet = snippetObj.asJsonObject["snippet"]
                return SpotifySong("", snippet.asString, " - ", " - ", " - ", "", "")
            }
        } catch (error: IOException) {
            error.printStackTrace()
        }

        return null
    }

}