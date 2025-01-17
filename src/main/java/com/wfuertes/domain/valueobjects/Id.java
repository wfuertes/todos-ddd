package com.wfuertes.domain.valueobjects;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

public abstract class Id {
    private final Ulid value;

    protected Id(Ulid value) {
        this.value = value;
    }

    public final String toString() {
        return value.toString();
    }

    @FunctionalInterface
    public interface IdFactory<T extends Id> {
        T create(Ulid value);
    }

    protected static <T extends Id> T generate(IdFactory<T> factory) {
        return factory.create(UlidCreator.getUlid());
    }

    protected static <T extends Id> T from(String value, IdFactory<T> factory) {
        return factory.create(Ulid.from(value));
    }
}
