package fitness.freya.newsservice.repository

import fitness.freya.newsservice.model.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface NewsRepository : JpaRepository<News, UUID> {

  fun findByValidityFromLessThanEqualAndValidityToGreaterThanEqual(
      from: LocalDateTime, to: LocalDateTime): List<News>

}