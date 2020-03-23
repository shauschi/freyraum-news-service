package fitness.freya.newsservice.api

import fitness.freya.newsservice.mapping.NewsMapper
import fitness.freya.newsservice.service.NewsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@CrossOrigin(origins = ["localhost:3333", "www.freya.fitness"], allowedHeaders = ["*"])
@RestController
@RequestMapping("/news")
class NewsController(
    val newsService: NewsService,
    val newsMapper: NewsMapper) {

  @ExceptionHandler(NewsNotFoundException::class)
  fun catchNewsNotFoundException(exception: NewsNotFoundException): ResponseEntity<ErrorDto> =
      ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body<ErrorDto>(ErrorDto(exception.message!!))

  @ExceptionHandler(Exception::class)
  fun catchException(exception: Exception): ResponseEntity<ErrorDto> =
      ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body<ErrorDto>(ErrorDto(exception.message!!))

  @GetMapping
  fun currentNews(): List<NewsDto> = newsService.getCurrentNews()
      .map(newsMapper::map)

  @PostMapping
  fun createNews(@RequestBody newsDto: NewsDto): NewsDto {
    val news = newsMapper.map(newsDto)
    val created = newsService.create(news)
    return newsMapper.map(created)
  }

  @GetMapping("/{id}")
  fun getNews(@PathVariable("id") id: UUID): NewsDto = newsService.getNews(id)
      .map(newsMapper::map)
      .orElseThrow {
        throw NewsNotFoundException(String.format("No news found for id '%s'", id))
      }

  @PatchMapping("/{id}")
  fun updateNews(@PathVariable("id") id: UUID, @RequestBody news: NewsDto): NewsDto {
    val updated = newsService.update(id, news)
    return newsMapper.map(updated)
  }

}
