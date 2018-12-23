package fitness.freya.newsservice.api

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fitness.freya.newsservice.mapping.NewsMapper
import fitness.freya.newsservice.mapping.ValidityMapper
import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Validity
import fitness.freya.newsservice.service.NewsService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.util.UUID

@RunWith(SpringRunner::class)
@WebMvcTest(NewsController::class)
class NewsControllerUpdateTest {

  private fun <T> any(): T = Mockito.any<T>()

  @Autowired
  lateinit var mvc: MockMvc

  @MockBean
  lateinit var newsService: NewsService

  @Autowired
  lateinit var newsMapper: NewsMapper

  lateinit var news: News

  @TestConfiguration
  class NewsControllerTestContextConfiguration {
    @Bean
    fun newsMapper(): NewsMapper = NewsMapper(validityMapper = ValidityMapper())
  }

  @Before
  fun setUp() {
    news = givenNews("Some news")
    Mockito
        .`when`(newsService.update(any(), any()))
        .thenReturn(news)
  }

  @Test
  fun `should pass the update structure to the service`() {
    // given
    val changes = newsMapper.map(news).copy(title = "New fancy title")
    val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
    val json = mapper.writeValueAsString(changes)

    // when + then
    mvc.perform(patch("/news/" + news.id!!)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk)
  }

  private fun givenNews(title: String): News = News(
      id = UUID.randomUUID(),
      title = title,
      teaser = "here is a teaser",
      text = "more text",
      validity = Validity(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1))
  )
}