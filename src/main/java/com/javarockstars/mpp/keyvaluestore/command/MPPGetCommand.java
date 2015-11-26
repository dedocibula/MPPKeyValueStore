package com.javarockstars.mpp.keyvaluestore.command;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class MPPGetCommand<K> extends MPPCommand {

    public MPPGetCommand(K key) {
        super(Type.GET, key);
    }
}
