package fitness.freya.newsservice.mapping

import fitness.freya.newsservice.api.NewsDto
import fitness.freya.newsservice.api.ValidityDto
import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Validity
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
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
class NewsMapperTest {

  @InjectMocks
  lateinit var testee: NewsMapper

  @Mock
  lateinit var validityMapper: ValidityMapper

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
    ReflectionTestUtils.setField(testee, "validityMapper", validityMapper)
  }

  @Test
  fun `should map News to NewsDto`() {
    // given
    val validity = Mockito.mock(Validity::class.java)
    val validityDto = Mockito.mock(ValidityDto::class.java)
    Mockito.`when`(validityMapper.map(validity)).thenReturn(validityDto)
    val news = News(
        UUID.randomUUID(),
        "My test title",
        "Teaser",
        "Text",
        "picture",
        validity
    )

    // when
    val result = testee.map(news)

    // then
    assertThat(result, notNullValue())
    assertThat(result.id, equalTo(news.id))
    assertThat(result.title, equalTo(news.title))
    assertThat(result.teaser, equalTo(news.teaser))
    assertThat(result.text, equalTo(news.text))
    assertThat(result.pictureId, equalTo(news.pictureId))
    assertThat(result.validity, equalTo(validityDto))
    verify(validityMapper).map(validity)
    verifyNoMoreInteractions(validityMapper)
  }

  @Test
  fun `should map NewsDto to News`() {
    // given
    val validityDto = Mockito.mock(ValidityDto::class.java)
    val validity = Mockito.mock(Validity::class.java)
    Mockito.`when`(validityMapper.map(validityDto)).thenReturn(validity)
    val news = NewsDto(
        UUID.randomUUID(),
        "My test title",
        "Teaser",
        "Text",
        "picture",
        validityDto
    )

    // when
    val result = testee.map(news)

    // then
    assertThat(result, notNullValue())
    assertThat(result.id, equalTo(news.id))
    assertThat(result.title, equalTo(news.title))
    assertThat(result.teaser, equalTo(news.teaser))
    assertThat(result.text, equalTo(news.text))
    assertThat(result.pictureId, equalTo(news.pictureId))
    assertThat(result.validity, equalTo(validity))
    verify(validityMapper).map(validityDto)
    verifyNoMoreInteractions(validityMapper)
  }

}