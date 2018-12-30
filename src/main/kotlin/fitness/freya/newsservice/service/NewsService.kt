package fitness.freya.newsservice.service

import fitness.freya.newsservice.api.NewsDto
import fitness.freya.newsservice.api.NewsNotFoundException
import fitness.freya.newsservice.mapping.NewsMapper
import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.repository.NewsRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Service
class NewsService(val newsRepository: NewsRepository, val newsMapper: NewsMapper) {

  fun getCurrentNews(): List<News> {
    val now = LocalDateTime.now()
    return newsRepository.findByValidityFromLessThanEqualAndValidityToGreaterThanEqual(now, now)
  }

  fun getNews(id: UUID): Optional<News> = newsRepository.findById(id)

  fun create(news: News): News = newsRepository.save(news)

  fun update(id: UUID, changes: NewsDto): News = getNews(id)
      .map { news -> newsMapper.apply(news, changes) }
      .map { newsRepository.save(it) }
      .orElseThrow {
        throw NewsNotFoundException(String.format("No news found for id '%s'", id))
      }

}