package ayds.apolo.songinfo.home.model.repository.external

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaTrackService
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test

class SongBrokerTest {

    private val spotifyService: SpotifyTrackService = mockk(relaxUnitFun = true)
    private val wikipediaService: WikipediaTrackService = mockk(relaxUnitFun = true)

    private val songBroker by lazy {
        SongBrokerImpl(spotifyService, wikipediaService)
    }

    @Test
    fun `given non existing song should return null`() {
        every { spotifyService.getSong("term") } returns null
        every { wikipediaService.getSong("term") } returns null

        val result = songBroker.getSong("term")

        assertEquals(null, result)
    }

    @Test
    fun `given existing song in spotify should return it`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image")
        every { spotifyService.getSong("term") } returns song
        every { wikipediaService.getSong("term") } returns null

        val result = songBroker.getSong("term")

        assertEquals(song, result)
    }

    @Test
    fun `given non existing song in spotify but in wikipedia should return the last`() {
        val song = SpotifySong("id", "name", "artist", "album", "date", "url", "image")
        every { spotifyService.getSong("term") } returns null
        every { wikipediaService.getSong("term") } returns song

        val result = songBroker.getSong("term")

        assertEquals(song, result)
    }
}