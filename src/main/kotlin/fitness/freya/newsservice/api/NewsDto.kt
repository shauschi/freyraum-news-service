package fitness.freya.newsservice.api

import java.util.UUID

data class NewsDto(
    val id: UUID?,
    val title: String?,
    val teaser: String?,
    val text: String?,
    val validity: ValidityDto)