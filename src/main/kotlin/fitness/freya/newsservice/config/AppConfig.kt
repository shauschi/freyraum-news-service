package fitness.freya.newsservice.config

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class AppConfig(
    @Value("\${spring.datasource.driverClassName:org.postgresql.Driver}")
    private val driverClassName: String,
    @Value("\${DB_URL:jdbc:postgresql://localhost/freyafitness}")
    private val dataSourceUrl: String,
    @Value("\${DB_USR:postgres}")
    private val dataSourceUsername: String,
    @Value("\${DB_PSW:postgres}")
    private val dataSourcePassword: String) {

  private val LOGGER = LogManager.getLogger(AppConfig::class.java)

  @Bean
  fun dataSource(): DataSource {
    LOGGER.info("Connecting to: {}, {}:{}", dataSourceUrl, dataSourceUsername, dataSourcePassword)

    val dataSource = DriverManagerDataSource()
    dataSource.setDriverClassName(driverClassName)
    dataSource.url = dataSourceUrl
    dataSource.username = dataSourceUsername
    dataSource.password = dataSourcePassword
    return dataSource
  }

}