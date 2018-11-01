package fitness.freya.newsservice.repository

import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Validity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import java.util.stream.Collectors

@RunWith(SpringRunner::class)
@DataJpaTest
class NewsRepositoryTest {

  @Autowired
  lateinit var entityManager: TestEntityManager

  @Autowired
  lateinit var testee: NewsRepository

  @Test
  fun `should find all valid news`() {
    // given
    val now = LocalDateTime.now()
    givenNews("Valid news", now, -1, 1)
    givenNews("Valid news 2", now, -1, 1)
    givenNews("expired news", now, -2, -1)
    givenNews("future news", now, 1, 2)

    // when
    val result = testee.findByValidityFromLessThanEqualAndValidityToGreaterThanEqual(now, now)

    // then
    assertThat(result, notNullValue())
    assertThat(result.size, equalTo(2))
    val titles = result.stream().map(News::title).collect(Collectors.toList())
    assertThat(titles, hasItems("Valid news", "Valid news 2"))
  }

  fun givenNews(title: String, now: LocalDateTime, from: Long, to: Long) {
    val valid = Validity(
        now.plusDays(from),
        now.plusDays(to)
    )
    val news = News(
        null,
        title,
        "here is a teaser",
        "more text",
        null,
        valid)
    entityManager.persist(news)
    entityManager.flush()
  }

}