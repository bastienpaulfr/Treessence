package fr.bipi.treessence.context

import fr.bipi.treessence.dsl.TimberApplication
import fr.bipi.treessence.dsl.TimberDeclaration
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
