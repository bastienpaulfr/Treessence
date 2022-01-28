package fr.bipi.treessence.common.os

class OsInfoProviderDefault : OsInfoProvider {
    override val currentTimeMillis: Long
        get() {
            return System.currentTimeMillis()
        }

    override val currentThreadId: Long
        get() = Thread.currentThread().id
}
