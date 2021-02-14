package fr.bipi.tressence.context

import fr.bipi.tressence.dsl.TimberApplication
import fr.bipi.tressence.dsl.TimberDeclaration
import timber.log.Timber

object GlobalContext : TimberContext {

    fun startTimber(
        timberDeclaration: TimberDeclaration
    ): TimberApplication = synchronized(this) {
        val application = TimberApplication
        timberDeclaration(application)
        application
    }

    fun stopTimber() {
        Timber.uprootAll()
    }
}
