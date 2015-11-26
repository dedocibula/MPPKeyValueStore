package com.javarockstars.mpp.keyvaluestore.implementation;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommand;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommandProcessor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public class MPPLFMapCommandProcessor implements MPPCommandProcessor {
    private final LockFreeMap<String, Serializable> lockFreeMap;

    public MPPLFMapCommandProcessor(final LockFreeMap<String, Serializable> lockFreeMap) {
        this.lockFreeMap = lockFreeMap;
    }

    @Override
    public Serializable processCommand(final MPPCommand command) {
        Objects.requireNonNull(command);
        switch (command.getType()) {
            case GET:
                return lockFreeMap.get(command.getKey());
            case PUT:
                return lockFreeMap.put(command.getKey(), command.getValue());
            case REMOVE:
                return lockFreeMap.remove(command.getKey());
            default:
                throw new IllegalArgumentException("Couldn't process type " + command.getType());
        }
    }
}
