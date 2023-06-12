package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.SongBroker
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.apolo.songinfo.home.model.repository.local.spotify.cache.SongCache
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Test


class SongRepositoryTest {

    private val spotifyCache: SongCache = mockk(relaxUnitFun = true)
    private val spotifyLocalStorage: SpotifyLocalStorage = mockk(relaxUnitFun=true)
    private val songBroker: SongBroker = mockk(relaxUnitFun=true)

    private val songRepository by lazy {
        SongRepositoryImpl(spotifyCache, spotifyLocalStorage, songBroker)
    }

    @Test
    fun `given cached song by name should return the cached song`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image")
        every { spotifyCache.getSongByTerm("term") } returns song
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { songBroker.getSong("term") } returns null

        val result = songRepository.getSongByTerm("term")

        assertEquals(song, result)
        //comprobamos que la cancion este marcada como cacheada
        assertTrue(song.isCacheStored)
    }

    @Test
    fun `given non cached but locally stored song by name should return the stored song`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image")
        every { spotifyCache.getSongByTerm("term") } returns null
        every { spotifyLocalStorage.getSongByTerm("term") } returns song
        every { songBroker.getSong("term") } returns null

        val result = songRepository.getSongByTerm("term")

        assertEquals(song, result)
        //comprobamos que la canon esté marcada como localmente guardada
        assertTrue(song.isLocallyStored)
    }

    @Test
    fun `given non existing song by name should return empty song`() {
        every { spotifyCache.getSongByTerm("term") } returns null
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { songBroker.getSong("term") } returns null

        val result = songRepository.getSongByTerm("term")

        assertEquals(EmptySong, result)
    }

    @Test
    fun `given existing but not stored song by term should search the song and store it`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image")
        every { spotifyCache.getSongByTerm("term") } returns null
        every { spotifyLocalStorage.getSongByTerm("term") } returns null
        every { songBroker.getSong("term") } returns song

        val result = songRepository.getSongByTerm("term")
        //comprobamos que el repositorio nos haya devuelto la cancion que debe devolver
        assertEquals(song, result)
        //comprobamos que la cancion NO se haya marcado como guardada
        assertFalse(song.isCacheStored)
        assertFalse(song.isLocallyStored)
        //verificamos que se haya llamado a insertar la cancion en la cache y LS
        verify { spotifyCache.insertSong("term", song) }
        verify { spotifyLocalStorage.insertSong("term", song) }
    }
}