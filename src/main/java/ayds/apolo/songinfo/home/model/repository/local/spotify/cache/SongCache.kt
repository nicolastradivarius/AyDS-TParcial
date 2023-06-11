package ayds.apolo.songinfo.home.model.repository.local.spotify.cache

import ayds.apolo.songinfo.home.model.entities.SpotifySong

interface SongCache {

    fun insertSong(term: String, song: SpotifySong)

    fun getSongByTerm(term: String): SpotifySong?
}

internal class SongCacheImpl: SongCache {

    private val songCache = mutableMapOf<String, SpotifySong>()

    override fun insertSong(term: String, song: SpotifySong) {
        songCache[term] = song
    }

    override fun getSongByTerm(term: String): SpotifySong? {
        return songCache[term]
    }
}