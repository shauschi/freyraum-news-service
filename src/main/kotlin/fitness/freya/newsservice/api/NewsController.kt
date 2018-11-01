package fitness.freya.newsservice.api

import fitness.freya.newsservice.mapping.NewsMapper
import fitness.freya.newsservice.service.NewsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import java.util.stream.Collectors

@RestController
class NewsController(
    val newsService: NewsService,
    val newsMapper: NewsMapper) {

  @GetMapping("/")
  fun currentNews(): List<NewsDto> = newsService.currentNews().stream()
      .map(newsMapper::map)
      .collect(Collectors.toList())

  @GetMapping("/{id}")
  fun getNews(@PathVariable("id") id: UUID): NewsDto = newsService.getNews(id)
      .map(newsMapper::map)
      .orElseThrow { RuntimeException("") }

  @PostMapping("/")
  fun saveNewCourse(@RequestBody newsDto: NewsDto): NewsDto {
    val news = newsMapper.map(newsDto)
    val created = newsService.create(news)
    return newsMapper.map(created)
  }

}