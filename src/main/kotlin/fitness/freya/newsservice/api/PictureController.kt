package fitness.freya.newsservice.api

import fitness.freya.newsservice.model.Size
import fitness.freya.newsservice.service.PictureService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/news/{id}/pictures")
class PictureController(val pictureService: PictureService) {

  @PostMapping
  fun uploadPicture(
      @PathVariable("id") id: UUID,
      @RequestParam("image") image: MultipartFile): MessageDto {
    pictureService.changePicture(id, image)
    return MessageDto("Picture updated")
  }

  @GetMapping("/{size}")
  fun getPicture(
      @PathVariable("id") id: UUID,
      @PathVariable("size") size: Size): ResponseEntity<Resource> {
    val data = pictureService.getPictureData(id, size)
    return if (data != null) {
      ResponseEntity.ok().body(ByteArrayResource(data))
    } else {
      ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }
  }


}


