package com.hshim.asciiart.model

import com.hshim.asciiart.enums.SSEEvent
import java.time.LocalDateTime

data class SSEEnvelope<T>(
    val event: SSEEvent,
    val timestamp: String,
    val payload: T,
) {
    constructor(type: SSEEvent, payload: T) : this(
        event = type,
        timestamp = LocalDateTime.now().toString(),
        payload = payload,
    )
}