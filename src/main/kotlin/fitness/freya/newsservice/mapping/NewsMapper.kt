package fitness.freya.newsservice.mapping

import fitness.freya.newsservice.api.NewsDto
import fitness.freya.newsservice.model.News
import org.springframework.stereotype.Component

@Component
class NewsMapper(val validityMapper: ValidityMapper) {

  fun map(news: News): NewsDto = NewsDto(
      news.id,
      news.title,
      news.teaser,
      news.text,
      news.pictureId,
      validityMapper.map(news.validity)
  )

  fun map(news: NewsDto): News = News(
      news.id,
      news.title,
      news.teaser,
      news.text,
      news.pictureId,
      validityMapper.map(news.validity)
  )

}
