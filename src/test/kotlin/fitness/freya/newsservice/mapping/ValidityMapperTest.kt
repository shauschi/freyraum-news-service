package fitness.freya.newsservice.mapping

import fitness.freya.newsservice.api.ValidityDto
import fitness.freya.newsservice.model.Validity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RunWith(MockitoJUnitRunner::class)
class ValidityMapperTest {

  @InjectMocks
  lateinit var testee: ValidityMapper

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  fun `should map Validity to ValidityDto`() {
    // given
    val from = LocalDateTime.of(
        LocalDate.of(2018, 11, 1),
        LocalTime.of(10, 30)
    )
    val to = LocalDateTime.of(
        LocalDate.of(2018, 12, 31),
        LocalTime.of(21, 45)
    )
    val validity = Validity(from, to)

    // when
    val result = testee.map(validity)

    // then
    assertThat(result.from, equalTo(from))
    assertThat(result.to, equalTo(to))
  }

  @Test
  fun `should map ValidityDto to Validity`() {
    // given
    val from = LocalDateTime.of(
        LocalDate.of(2018, 11, 1),
        LocalTime.of(10, 30)
    )
    val to = LocalDateTime.of(
        LocalDate.of(2018, 12, 31),
        LocalTime.of(21, 45)
    )
    val validity = ValidityDto(from, to)

    // when
    val result = testee.map(validity)

    // then
    assertThat(result.from, equalTo(from))
    assertThat(result.to, equalTo(to))
  }

}