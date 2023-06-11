package ayds.apolo.songinfo.home.model

import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.apolo.songinfo.home.model.repository.external.SongBrokerImpl
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyModule
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaInjector
import ayds.apolo.songinfo.home.model.repository.local.spotify.cache.SongCacheImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.ResultSetToSpotifySongMapperImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlDBImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlQueriesImpl

object HomeModelInjector {

  private val localStorage = SpotifySqlDBImpl(
    SpotifySqlQueriesImpl(),
    ResultSetToSpotifySongMapperImpl()
  )

  private val cache = SongCacheImpl()

  private val spotifyTrackService = SpotifyModule.spotifyTrackService
  private val wikipediaTrackService = WikipediaInjector.wikipediaTrackService

  private val songBroker = SongBrokerImpl(spotifyTrackService, wikipediaTrackService)

  private val repository: SongRepository = SongRepository(cache, localStorage, songBroker)

  val homeModel: HomeModel = HomeModelImpl(repository)
}