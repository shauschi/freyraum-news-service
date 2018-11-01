package fitness.freya.newsservice.mapping

import fitness.freya.newsservice.api.ValidityDto
import fitness.freya.newsservice.model.Validity
import org.springframework.stereotype.Component

@Component
class ValidityMapper {

  fun map(validity: Validity): ValidityDto = ValidityDto(validity.from, validity.to)

  fun map(validity: ValidityDto): Validity = Validity(validity.from, validity.to)

}
