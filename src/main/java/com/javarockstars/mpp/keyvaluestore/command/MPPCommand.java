package com.javarockstars.mpp.keyvaluestore.command;

import java.io.Serializable;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPCommand implements Serializable {
    private Type type;
    private String key;
    private Object value;

    @SuppressWarnings("unused")
    private MPPCommand() {
        // deserialization
    }

    MPPCommand(final Type type, final String key) {
        this(type, key, null);
    }

    MPPCommand(final Type type, final String key, final Object value) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(key);
        this.type = type;
        this.key = key;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public enum Type {
        GET, PUT, REMOVE
    }
}
