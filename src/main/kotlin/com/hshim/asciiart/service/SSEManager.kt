package com.hshim.asciiart.service

import com.hshim.asciiart.enums.SSEEvent
import com.hshim.asciiart.model.SSEEnvelope
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap

@Component
class SSEManager {
    val emitterMap: ConcurrentHashMap<String, SseEmitter> = ConcurrentHashMap()

    fun build(id: String): SseEmitter {
        val emitter = SseEmitter(30 * 60 * 1000)
        emitter.onCompletion { emitterMap.remove(id) }
        emitter.onTimeout { emitter.complete() }
        emitter.onError { e ->
            e.printStackTrace()
            emitter.complete()
        }
        emitter.sendEmitter(SSEEnvelope(SSEEvent.ID, id))
        emitterMap[id] = emitter
        return emitter
    }

    fun exists(id: String): Boolean {
        return emitterMap.containsKey(id)
    }

    fun send(id: String, event: SSEEvent, data: Any?) {
        val envelope = SSEEnvelope(event, data)
        emitterMap[id]?.sendEmitter(envelope)
    }

    fun complete(id: String) {
        emitterMap[id]?.complete()
    }

    fun heartbeat() {
        emitterMap.values.forEach { it.sendEmitter(SSEEnvelope(SSEEvent.HEARTBEAT, "alive")) }
    }

    fun SseEmitter.sendEmitter(data: SSEEnvelope<Any?>) {
        runCatching { this.send(SseEmitter.event().name(data.event.name).data(data)) }.onFailure { this.complete() }
    }
}