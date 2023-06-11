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
            if (song != null) {
                return song
            }
        }

        return song
    }
}