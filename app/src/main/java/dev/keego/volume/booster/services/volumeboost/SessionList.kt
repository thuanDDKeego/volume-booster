package dev.keego.volume.booster.services.volumeboost

import android.media.audiofx.LoudnessEnhancer

class SessionList {
    private val defaultGain = 10
    private val sessions = HashMap<AudioSessionInfo, LoudnessEnhancer>()

    fun add(info: AudioSessionInfo) {
        val enhancer = LoudnessEnhancer(info.sessionId)
        enhancer.setTargetGain(defaultGain)
        enhancer.enabled = true
        sessions[info] = enhancer
    }

    fun remove(info: AudioSessionInfo) {
        sessions[info]?.let { enhancer ->
            enhancer.release()
        }
        sessions.remove(info)
    }

    fun clear() {
        sessions.values.forEach { enhancer ->
            enhancer.release()
        }
        sessions.clear()
    }

    override fun toString(): String {
        return sessions.keys.toString()
    }
}
