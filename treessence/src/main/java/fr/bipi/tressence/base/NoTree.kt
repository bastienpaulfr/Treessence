package fr.bipi.tressence.base

import timber.log.Timber

class NoTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
}
