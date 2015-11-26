package com.javarockstars.mpp.keyvaluestore.command;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public class MPPPutCommand<K, V> extends MPPCommand {

    public MPPPutCommand(K key, V value) {
        super(Type.PUT, key, value);
    }
}
