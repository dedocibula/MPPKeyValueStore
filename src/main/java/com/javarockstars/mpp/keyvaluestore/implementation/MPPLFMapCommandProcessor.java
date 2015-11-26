package com.javarockstars.mpp.keyvaluestore.implementation;

import com.javarockstars.mpp.datastructures.api.LockFreeMap;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommand;
import com.javarockstars.mpp.keyvaluestore.command.MPPCommandProcessor;

import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public class MPPLFMapCommandProcessor implements MPPCommandProcessor {
    private final LockFreeMap<String, Object> lockFreeMap;

    public MPPLFMapCommandProcessor(LockFreeMap<String, Object> lockFreeMap) {
        this.lockFreeMap = lockFreeMap;
    }

    @Override
    public Object processCommand(final MPPCommand command) {
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
