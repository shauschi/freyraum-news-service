package fitness.freya.newsservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewsServiceApplication

fun main(args: Array<String>) {
  runApplication<NewsServiceApplication>(*args)
}

