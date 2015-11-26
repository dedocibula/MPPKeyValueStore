package com.javarockstars.mpp.keyvaluestore.command;

import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public abstract class MPPCommand {
    private Type type;
    private Object[] arguments;

    private MPPCommand() {
        // deserialization
    }

    MPPCommand(final Type type, final Object... arguments) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(arguments);
        this.type = type;
        this.arguments = arguments;
    }

    public Type getType() {
        return type;
    }

    public Object[] getArguments() {
        return arguments;
    }

    enum Type {
        GET, PUT, REMOVE
    }
}
