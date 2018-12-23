package fitness.freya.newsservice.repository

import fitness.freya.newsservice.model.Picture
import fitness.freya.newsservice.model.Size
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface PictureRepository : JpaRepository<Picture, UUID> {

  fun findByNewsIdAndSize(id: UUID, size: Size): Optional<Picture>

  /**
   * Will delete all entries for that given UUID.
   * @param id given id
   */
  fun deleteByNewsId(id: UUID)

}