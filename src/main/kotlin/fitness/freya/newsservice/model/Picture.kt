package fitness.freya.newsservice.model

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
    name = "picture",
    schema = "public",
    uniqueConstraints = [
      (UniqueConstraint(columnNames = arrayOf("news_id", "size")))
    ]
)
data class Picture(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    val id: UUID?,
    @ManyToOne
    @JoinColumn(name = "news_id")
    val news: News? = null,
    @Enumerated(EnumType.STRING)
    val size: Size?,
    val data: ByteArray?
)
