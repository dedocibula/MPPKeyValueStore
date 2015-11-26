package com.javarockstars.mpp.keyvaluestore.command;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public interface MPPCommandProcessor {

    <T> T processCommand(final MPPCommand command);
}
