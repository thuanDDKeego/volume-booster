package dev.keego.volume.booster.services.messages

import dev.keego.volume.booster.services.volumeboost.AudioSessionInfo

data class AudioSessionEvent(val open: Boolean, val session: AudioSessionInfo) {
    override fun toString(): String {
        return "AudioSessionEvent{" +
            "open=$open" +
            ", session=$session" +
            '}'
    }
}
