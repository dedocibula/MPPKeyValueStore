package com.javarockstars.mpp.keyvaluestore.command;

import com.javarockstars.mpp.keyvaluestore.command.MPPCommand.Type;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPCommandFactory {
    public static MPPCommand newGetCommand(String key) {
        return new MPPCommand(Type.GET, key);
    }

    public static <V> MPPCommand newPutCommand(String key, V value) {
        return new MPPCommand(Type.PUT, key, value);
    }

    public static MPPCommand newRemoveCommand(String key) {
        return new MPPCommand(Type.REMOVE, key);
    }
}
