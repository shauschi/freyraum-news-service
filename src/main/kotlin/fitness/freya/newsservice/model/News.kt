package fitness.freya.newsservice.model

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "news", schema = "public")
data class News(
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    val id: UUID?,
    val title: String,
    val teaser: String,
    val text: String,
    val pictureId: String?,
    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "from", column = Column(name = "validity_from")),
        AttributeOverride(name = "to", column = Column(name = "validity_to"))
    )
    val validity: Validity)