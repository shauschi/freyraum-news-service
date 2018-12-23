package fitness.freya.newsservice.service

import fitness.freya.newsservice.api.NewsNotFoundException
import fitness.freya.newsservice.mapping.NewsMapper
import fitness.freya.newsservice.mapping.ValidityMapper
import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Validity
import fitness.freya.newsservice.repository.NewsRepository
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.util.Optional
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class NewsServiceUpdateTest {

  private lateinit var testee: NewsService

  @Mock
  private lateinit var newsRepository: NewsRepository

  private lateinit var newsMapper: NewsMapper

  private lateinit var news: News

  @Before
  fun setUp() {
    newsMapper = NewsMapper(validityMapper = ValidityMapper())
    testee = NewsService(newsRepository = newsRepository, newsMapper = newsMapper)

    news = News(
        UUID.randomUUID(),
        "News title",
        "Here is a little teaser",
        "A lot of more text. Have fun reading the test cases.",
        Validity(
            LocalDateTime.of(LocalDate.of(2018, Month.AUGUST, 10), LocalTime.MIDNIGHT),
            null
        )
    )
    Mockito
        .`when`(newsRepository.findById(news.id!!))
        .thenReturn(Optional.of(news))
    Mockito
        .`when`(newsRepository.save(Mockito.any(News::class.java)))
        .thenAnswer { it.arguments[0] }
  }

  @Test
  fun `should update title`() {
    // given
    val changes = newsMapper.map(news).copy(title = "Fancy new title")

    // when
    val updated = testee.update(news.id!!, changes)

    // then
    assertThat(updated.title, CoreMatchers.`is`("Fancy new title"))
    assertThat(updated.teaser, CoreMatchers.`is`(news.teaser))
    assertThat(updated.text, CoreMatchers.`is`(news.text))
  }

  @Test
  fun `should update teaser`() {
    // given
    val changes = newsMapper.map(news).copy(teaser = "A teaser")

    // when
    val updated = testee.update(news.id!!, changes)

    // then
    assertThat(updated.title, CoreMatchers.`is`(news.title))
    assertThat(updated.teaser, CoreMatchers.`is`("A teaser"))
    assertThat(updated.text, CoreMatchers.`is`(news.text))
  }

  @Test
  fun `should update text`() {
    // given
    val changes = newsMapper.map(news).copy(text = "A short text")

    // when
    val updated = testee.update(news.id!!, changes)

    // then
    assertThat(updated.title, CoreMatchers.`is`(news.title))
    assertThat(updated.teaser, CoreMatchers.`is`(news.teaser))
    assertThat(updated.text, CoreMatchers.`is`("A short text"))
  }

  @Test
  fun `should work without any changes`() {
    // when
    val updated = testee.update(news.id!!, newsMapper.map(news))

    // then
    assertThat(updated, CoreMatchers.`is`(news))
  }

  @Test(expected = NewsNotFoundException::class)
  fun `should throw an exception for an invalid id`() {
    testee.update(UUID.randomUUID(), newsMapper.map(news))
  }


}