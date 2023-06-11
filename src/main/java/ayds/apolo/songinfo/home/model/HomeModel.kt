package ayds.apolo.songinfo.home.model

import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.repository.SongRepositoryImpl

interface HomeModel {

    fun searchSong(term: String): Song
}

internal class HomeModelImpl(private val repository: SongRepositoryImpl) : HomeModel {
    override fun searchSong(term: String) = repository.getSongByTerm(term)
}