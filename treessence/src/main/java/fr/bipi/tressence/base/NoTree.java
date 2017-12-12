package fr.bipi.tressence.base;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class NoTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {

    }
}
