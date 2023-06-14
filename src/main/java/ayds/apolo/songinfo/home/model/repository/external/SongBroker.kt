package ayds.apolo.songinfo.home.model.repository.external

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaTrackService

interface SongBroker {
    fun getSong(term: String): SpotifySong?
}

internal class SongBrokerImpl(
    private val spotifyService: SpotifyTrackService,
    private val wikipediaService: WikipediaTrackService
) : SongBroker {

    override fun getSong(term: String): SpotifySong? {
        var song = spotifyService.getSong(term)
        //no controlo si es != null porque la tengo que devolver igual
        if (song == null) {
            /// Last chance, get anything from the wiki
            song = wikipediaService.getSong(term)
        }

        return song
    }

    /*
    Este método es la implementación de la función de búsqueda del parcial del 12-06-23 que debería ir en el broker.
    */
    @Suppress("unused")
    private fun getArticleDoubleSource(term: String): String {
        val spotifyArticle = spotifyService.getSong(term)
        val wikipediaArticle = wikipediaService.getSong(term)

        val hasSpotifyInfo = spotifyArticle != null
        val hasWikipediaInfo = wikipediaArticle != null

        return when {
            hasSpotifyInfo && hasWikipediaInfo -> {
                "First source: $spotifyArticle\nSecond source: $wikipediaArticle"
            }
            hasSpotifyInfo -> {
                "Single source: $spotifyArticle (Spotify)"
            }
            hasWikipediaInfo -> {
                "Single source: $wikipediaArticle (Wikipedia)"
            }
            else -> {
                ""
            }
        }
    }
}