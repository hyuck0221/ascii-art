package com.hshim.asciiart.api

import com.hshim.asciiart.enums.ArtType
import com.hshim.asciiart.enums.SSEEvent
import com.hshim.asciiart.enums.ThreadType
import com.hshim.asciiart.model.LineBody
import com.hshim.asciiart.service.AsciiArtGenerator
import com.hshim.asciiart.service.SSEManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.*

@RestController
@RequestMapping("/ascii-art")
class AsciiArtController(private val sseManager: SSEManager) {
    @GetMapping("/generate/async/connect", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun connect(): SseEmitter {
        val id = UUID.randomUUID().toString()
        return sseManager.build(id)
    }

    @PostMapping("/generate/async/upload")
    fun upload(
        @RequestParam id: String,
        @RequestPart image: MultipartFile,
        @RequestParam(required = false) artType: ArtType = ArtType.DETAIL,
        @RequestParam(required = false) reverse: Boolean = false,
        @RequestParam(required = false) threadType: ThreadType = ThreadType.SINGLE,
    ) {
        if (!sseManager.exists(id)) return
        val flow = AsciiArtGenerator(image)
            .apply {
                this.artType = artType
                this.reverse = reverse
                this.threadType = threadType
            }
            .generateAsync()

        CoroutineScope(Dispatchers.IO).launch {
            flow.collect { item -> sseManager.send(id, SSEEvent.PRINT_LINE, item) }
            sseManager.complete(id)
        }
    }

    @PostMapping("/generate")
    fun generate(
        @RequestPart image: MultipartFile,
        @RequestParam(required = false) artType: ArtType = ArtType.DETAIL,
        @RequestParam(required = false) reverse: Boolean = false,
        @RequestParam(required = false) threadType: ThreadType = ThreadType.SINGLE,
    ): List<LineBody> {
        return AsciiArtGenerator(image)
            .apply {
                this.artType = artType
                this.reverse = reverse
                this.threadType = threadType
            }
            .generate()
    }
}