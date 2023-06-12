package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.repository.external.SongBroker
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.apolo.songinfo.home.model.repository.local.spotify.cache.SongCache

interface SongRepository {
    fun getSongByTerm(term: String): Song
}

internal class SongRepositoryImpl (
    private var spotifyCache: SongCache,
    private var spotifyLocalStorage: SpotifyLocalStorage,
    private var songBroker: SongBroker
): SongRepository {

    override fun getSongByTerm(term: String): Song {

        // check in the cache
        var song = spotifyCache.getSongByTerm(term)
        if (song != null) {
            song.setIsCacheStored()
        } else {
            // check in the DB
            song = spotifyLocalStorage.getSongByTerm(term)
            if (song != null) {
                song.setIsLocallyStored()
                // update the cache
                spotifyCache.insertSong(term, song)
            } else {
                // the remote search is now implemented with broker patron
                song = songBroker.getSong(term)
                if (song != null) {
                    //no las marco con setIsCacheStored y setIsLocallyStored, porque sino se marcan con asterisco siempre
                    //así, se marcaran con el asterisco cuando se busquen por segunda vez, porque entra en los IF anteriores
                    spotifyCache.insertSong(term, song)
                    spotifyLocalStorage.insertSong(term, song)
                }
            }
        }
        return song?: EmptySong
    }
}