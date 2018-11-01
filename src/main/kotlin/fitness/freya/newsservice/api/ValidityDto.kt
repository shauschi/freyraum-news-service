package fitness.freya.newsservice.api

import java.time.LocalDateTime

data class ValidityDto(
    val from: LocalDateTime?,
    val to: LocalDateTime?)