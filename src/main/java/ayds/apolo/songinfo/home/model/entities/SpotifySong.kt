package ayds.apolo.songinfo.home.model.entities

interface Song
class SpotifySong(
  val id: String,
  val songName: String,
  val artistName: String,
  val albumName: String,
  val releaseDate: String,
  val spotifyUrl: String,
  val imageUrl: String,
  var isLocallyStored: Boolean = false,
  var isCacheStored: Boolean = false
) : Song {

  val year: String = releaseDate.split("-").first()

  fun setIsCacheStored() {
    this.isCacheStored = true
  }

  fun setIsLocallyStored() {
    this.isLocallyStored = true
  }
}

object EmptySong : Song