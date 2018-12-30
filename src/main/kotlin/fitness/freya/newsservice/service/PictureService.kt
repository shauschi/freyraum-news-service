package fitness.freya.newsservice.service

import fitness.freya.newsservice.api.NewsNotFoundException
import fitness.freya.newsservice.api.PictureNotFoundException
import fitness.freya.newsservice.model.News
import fitness.freya.newsservice.model.Picture
import fitness.freya.newsservice.model.Size
import fitness.freya.newsservice.repository.NewsRepository
import fitness.freya.newsservice.repository.PictureRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.AlphaComposite
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.UUID
import javax.imageio.ImageIO
import javax.transaction.Transactional

@Service
class PictureService(val newsRepository: NewsRepository, val pictureRepository: PictureRepository) {

  fun getPictureData(newsId: UUID, size: Size): ByteArray? {
    return pictureRepository.findByNewsIdAndSize(newsId, size)
        .map(Picture::data)
        .orElseThrow {
          throw PictureNotFoundException(
              String.format("No picture found for id %s and size %s", newsId, size))
        }
  }

  @Transactional
  @Throws(IOException::class)
  fun changePicture(newsId: UUID, multipartFile: MultipartFile) {
    val news = newsRepository.findById(newsId)
        .orElseThrow {
          throw NewsNotFoundException(String.format("No news found for id '%s'", newsId))
        }
    // delete all pictures for that id
    pictureRepository.deleteByNewsId(newsId)
    pictureRepository.flush()
    save(multipartFile, news)
  }

  @Throws(IOException::class)
  private fun save(multipartFile: MultipartFile, news: News) {
    val inputStream = multipartFile.inputStream
    val image = ImageIO.read(inputStream)
    for (size in Size.values()) {
      val resizedImage = getResizedImage(image, size)
      val data = getImageAsBytes(resizedImage)

      val picture = Picture(
          id = null,
          news = news,
          size = size,
          data = data
      )
      pictureRepository.save(picture)
    }
  }

  @Throws(IOException::class)
  private fun getImageAsBytes(image: BufferedImage): ByteArray {
    val os = ByteArrayOutputStream()
    ImageIO.write(image, "png", os)
    return os.toByteArray()
  }

  private fun getResizedImage(original: BufferedImage, targetSize: Size): BufferedImage {
    if (Size.ORIGINAL === targetSize) {
      return original
    }
    val preferredWidth = targetSize.preferedSize
    val preferredHeight = original.height * preferredWidth / original.width
    return createResizedCopy(original, preferredWidth, preferredHeight)
  }

  private fun createResizedCopy(originalImage: Image, scaledWidth: Int, scaledHeight: Int): BufferedImage {
    val tmp = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH)
    val resized = BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB)
    val graphics2D = resized.createGraphics()
    graphics2D.composite = AlphaComposite.Src
    graphics2D.drawImage(tmp, 0, 0, scaledWidth, scaledHeight, null)
    graphics2D.dispose()
    return resized
  }

}