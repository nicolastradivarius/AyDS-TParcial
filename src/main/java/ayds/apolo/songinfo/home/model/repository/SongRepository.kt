package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.repository.external.SongBroker
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.apolo.songinfo.home.model.repository.local.spotify.cache.SongCache

internal class SongRepository (
    private var cache: SongCache,
    private var localStorage: SpotifyLocalStorage,
    private var songBroker: SongBroker
) {

    fun getSongByTerm(term: String): Song {

        // check in the cache
        var song = localStorage.getSongByTerm(term)
        if (song != null) {
            song.setIsCacheStored()
            return song
        } else {
            // check in the DB
            song = localStorage.getSongByTerm(term)
            if (song != null) {
                song.setIsLocallyStored()
                // update the cache
                cache.insertSong(term, song)
                song.setIsCacheStored()
                return song
            } else {
                // the service is now implemented with broker patron
                song = songBroker.getSong(term)
                if (song != null) {
                    //no las marco con setIsCacheStored y el otro, porque sino se marcan con asterisco siempre
                    //así, se marcaran con el asterisco cuando se busquen por segunda vez, porque entra en los IF anteriores
                    cache.insertSong(term, song)
                    localStorage.insertSong(term, song)
                }
            }
        }

        return song?: EmptySong
    }
}