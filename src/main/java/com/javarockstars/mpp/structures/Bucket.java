package com.javarockstars.mpp.structures;

import java.util.Map;

/**
 * Author: dedocibula
 * Created on: 5.10.2015.
 */
interface Bucket<T extends Node> {
    boolean insert(final T entry);

    boolean delete(final T entry);

    boolean contains(final T entry);
}
