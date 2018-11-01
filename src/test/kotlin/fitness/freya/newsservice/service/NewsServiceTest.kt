package fitness.freya.newsservice.service

import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Validity
import fitness.freya.newsservice.repository.NewsRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.util.ReflectionTestUtils
import java.util.Optional
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class NewsServiceTest {

  private fun <T> any(): T = Mockito.any<T>()

  @InjectMocks
  lateinit var testee: NewsService

  @Mock
  lateinit var newsRepository: NewsRepository

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    ReflectionTestUtils.setField(testee, "newsRepository", newsRepository)
  }

  @Test
  fun `should return all valid news`() {
    // given
    val allNews = listOf(mockNews(), mockNews(), mockNews())
    Mockito
        .`when`(newsRepository.findByValidityFromLessThanEqualAndValidityToGreaterThanEqual(any(), any()))
        .thenReturn(allNews)

    // when
    val result = testee.currentNews()

    //then
    assertThat(result, notNullValue())
    assertThat(result.size, equalTo(3))
    verify(newsRepository)
        .findByValidityFromLessThanEqualAndValidityToGreaterThanEqual(any(), any())
    verifyNoMoreInteractions(newsRepository)
  }

  @Test
  fun `should return Optional empty() if no News is found for a given uuid`() {
    // given
    val id = UUID.randomUUID()

    // when
    val result = testee.getNews(id)

    // then
    assertThat(result, notNullValue())
    assertThat(result, equalTo(Optional.empty()))
    verify(newsRepository).findById(id)
    verifyNoMoreInteractions(newsRepository)
  }

  @Test
  fun `should return news with given uuid`() {
    // given
    val id = UUID.randomUUID()
    val givenNews = Optional.of(mockNews(id))
    Mockito
        .`when`(newsRepository.findById(id))
        .thenReturn(givenNews)

    // when
    val result = testee.getNews(id)

    // then
    assertThat(result, notNullValue())
    assertThat(result.isPresent, equalTo(true))
    assertThat(result.get().id, equalTo(id))
    verify(newsRepository).findById(id)
    verifyNoMoreInteractions(newsRepository)
  }

  @Test
  fun `should return saved News`() {
    // given
    val id = UUID.randomUUID()
    val news = News(
        id,
        "Tilte of awesome news",
        "Here is a little teaser",
        "The whole story",
        "007",
        Mockito.mock(Validity::class.java)
    )
    Mockito
        .`when`(newsRepository.save(news))
        .thenReturn(news)

    // when
    val result = testee.create(news)

    //then
    assertThat(result, notNullValue())
    assertThat(result, equalTo(news))
    verify(newsRepository).save(news)
    verifyNoMoreInteractions(newsRepository)
  }

  fun mockNews(): News = Mockito.mock(News::class.java)

  fun mockNews(id: UUID): News {
    val mock = Mockito.mock(News::class.java)
    Mockito.`when`(mock.id).thenReturn(id)
    return mock
  }

}
