package com.javarockstars.mpp.keyvaluestore.command;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public class MPPRemoveCommand<K> extends MPPCommand {

    public MPPRemoveCommand(K key) {
        super(Type.REMOVE, key);
    }
}
