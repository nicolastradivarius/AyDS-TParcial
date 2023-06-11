package ayds.apolo.songinfo.home.model.repository.external.wikipedia

import ayds.apolo.songinfo.home.model.repository.WikipediaAPI
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object WikipediaInjector {

    private const val WIKIPEDIA_URL = "https://en.wikipedia.org/w/"

    ///// Wiki
    var wikipediaAPIRetrofit: Retrofit? = Retrofit.Builder()
        .baseUrl(WIKIPEDIA_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private var wikipediaAPI = wikipediaAPIRetrofit!!.create(WikipediaAPI::class.java)

    val wikipediaTrackService: WikipediaTrackService = WikipediaTrackServiceImpl(wikipediaAPI)
}