package fitness.freya.newsservice.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fitness.freya.newsservice.mapping.NewsMapper
import fitness.freya.newsservice.mapping.ValidityMapper
import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Validity
import fitness.freya.newsservice.service.NewsService
import org.hamcrest.Matchers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional
import java.util.UUID

@RunWith(SpringRunner::class)
@WebMvcTest(NewsController::class)
class NewsControllerTest {

  private fun <T> any(): T = Mockito.any<T>()

  @Autowired
  lateinit var mvc: MockMvc

  @MockBean
  lateinit var newsService: NewsService

  @Autowired
  lateinit var newsMapper: NewsMapper

  @TestConfiguration
  class NewsControllerTestContextConfiguration {
    @Bean
    fun newsMapper(): NewsMapper = NewsMapper(validityMapper = ValidityMapper())
  }

  @Test
  fun `should return all current news`() {
    // given
    val news1 = givenNews("News 1")
    val news2 = givenNews("News 2")
    Mockito
        .`when`(newsService.getCurrentNews())
        .thenReturn(listOf(news1, news2))

    // when + then
    mvc.perform(get("/news")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk)
        .andExpect(jsonPath("\$.[0].title").value("News 1"))
        .andExpect(jsonPath("\$.[0].teaser").value("here is a teaser"))
        .andExpect(jsonPath("\$.[0].text").value("more text"))
        .andExpect(jsonPath("\$.[1].title").value("News 2"))
    verify(newsService).getCurrentNews()
    verifyNoMoreInteractions(newsService)
  }

  @Test
  fun `should return news with given id`() {
    // given
    val news = givenNews("News 1")
    val id = news.id!!
    Mockito
        .`when`(newsService.getNews(id))
        .thenReturn(Optional.of(news))

    // when + then
    mvc.perform(get("/news/" + id)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk)
        .andExpect(jsonPath("\$.title").value("News 1"))
        .andExpect(jsonPath("\$.teaser").value("here is a teaser"))
        .andExpect(jsonPath("\$.text").value("more text"))
    verify(newsService).getNews(id)
    verifyNoMoreInteractions(newsService)
  }

  @Test
  fun `should return 404 with error message if no news were found`() {
    // given
    val id = UUID.randomUUID()
    Mockito
        .`when`(newsService.getNews(id))
        .thenReturn(Optional.empty())

    // when + then
    mvc.perform(get("/news/" + id)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest)
        .andExpect(jsonPath("\$.message").value(Matchers.startsWith("No news found for id")))
    verify(newsService).getNews(id)
    verifyNoMoreInteractions(newsService)
  }

  @Test
  fun `should create news and return created news`() {
    // given
    val news = givenNews("My News")
    val newsDto = newsMapper.map(news)
    val mapper = jacksonObjectMapper()
    val json = mapper.writeValueAsString(newsDto)
    Mockito
        .`when`(newsService.create(any()))
        .thenAnswer { it.arguments[0] }

    // when + then
    mvc.perform(post("/news")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk)
        .andExpect(jsonPath("\$.title").value("My News"))
        .andExpect(jsonPath("\$.teaser").value("here is a teaser"))
        .andExpect(jsonPath("\$.text").value("more text"))
    verify(newsService).create(any())
    verifyNoMoreInteractions(newsService)
  }

  fun givenNews(title: String): News = News(
      UUID.randomUUID(),
      title,
      "here is a teaser",
      "more text",
      null,
      Mockito.mock(Validity::class.java))

}