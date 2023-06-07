package dev.keego.volume.booster.services.volumeboost

data class AudioSessionInfo(val sessionId: Int, val packageName: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AudioSessionInfo) return false
        return sessionId == other.sessionId && packageName == other.packageName
    }

    override fun hashCode(): Int {
        return sessionId.hashCode() + packageName.hashCode()
    }

    override fun toString(): String {
        return "{$packageName: $sessionId}"
    }
}
