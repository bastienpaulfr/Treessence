package fr.bipi.tressence.common.os;

public class OsInfoProviderDefault implements OsInfoProvider {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long getCurrentThreadId() {
        return Thread.currentThread().getId();
    }
}
