package com.wfuertes.domain.valueobjects;

import com.github.f4b6a3.ulid.Ulid;

public final class UserId extends Id {
    private static final IdFactory<UserId> FACTORY = UserId::new;

    private UserId(Ulid value) {
        super(value);
    }

    public static UserId generate() {
        return Id.generate(FACTORY);
    }

    public static UserId from(String value) {
        return Id.from(value, FACTORY);
    }
}
