package fitness.freya.newsservice.service

import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.repository.NewsRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Service
class NewsService(val newsRepository: NewsRepository) {

  fun currentNews(): List<News> {
    val now = LocalDateTime.now()
    return newsRepository.findByValidityFromLessThanEqualAndValidityToGreaterThanEqual(now, now)
  }

  fun getNews(id: UUID): Optional<News> = newsRepository.findById(id)

  fun create(news: News): News = newsRepository.save(news)

}