package com.javarockstars.mpp.keyvaluestore.command;

import java.io.Serializable;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public interface MPPCommandProcessor {

    Serializable processCommand(final MPPCommand command);
}
