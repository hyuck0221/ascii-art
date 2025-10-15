package com.hshim.asciiart.service

import com.hshim.asciiart.enums.ArtType
import com.hshim.asciiart.enums.ThreadType
import com.hshim.asciiart.model.LineBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.min

class AsciiArtGenerator(file: MultipartFile) {

    private val image = resizeImageIfNeeded(ImageIO.read(file.inputStream))
    private val width = image.width
    private val height = image.height

    companion object {
        private const val MAX_WIDTH = 4000
        private const val MAX_HEIGHT = 4000

        private fun resizeImageIfNeeded(originalImage: BufferedImage): BufferedImage {
            val originalWidth = originalImage.width
            val originalHeight = originalImage.height

            if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
                return originalImage
            }

            val widthRatio = MAX_WIDTH.toDouble() / originalWidth
            val heightRatio = MAX_HEIGHT.toDouble() / originalHeight
            val ratio = min(widthRatio, heightRatio)

            val newWidth = (originalWidth * ratio).toInt()
            val newHeight = (originalHeight * ratio).toInt()

            val resizedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
            val graphics = resizedImage.createGraphics()
            graphics.drawImage(
                originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),
                0, 0, null
            )
            graphics.dispose()

            return resizedImage
        }
    }


    var artType: ArtType = ArtType.DETAIL
    var reverse: Boolean = false
    var threadType: ThreadType = ThreadType.SINGLE

    private val chars: String
        get() = if (reverse) artType.chars.reversed() else artType.chars

    fun generate(): List<LineBody> {
        return runBlocking { generateAsync().toList() }
    }

    fun generateAsync(): Flow<LineBody> = channelFlow {
        when (threadType) {
            ThreadType.SINGLE -> {
                for (y in 0 until height) {
                    send(generateLine(y))
                }
            }

            ThreadType.MULTI -> {
                for (y in 0 until height) {
                    launch {
                        val line = generateLine(y)
                        send(line)
                    }
                }
            }
        }
    }

    fun generateLine(y: Int): LineBody {
        return (0 until width)
            .map { x -> generatePixel(x, y) }
            .joinToString("")
            .let { LineBody(y, it) }
    }

    fun generatePixel(x: Int, y: Int): Char {
        val brightness = calculateBrightness(x, y)
        val index = (brightness * (chars.length - 1)).toInt()
        return chars[index]
    }

    fun calculateBrightness(x: Int, y: Int): Double {
        val rgb = image.getRGB(x, y)
        val r = (rgb shr 16) and 0xFF
        val g = (rgb shr 8) and 0xFF
        val b = rgb and 0xFF
        return ((r + g + b) / 3) / 255.0
    }
}