package com.javarockstars.mpp.keyvaluestore.util;

import java.io.*;
import java.util.Objects;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class SerializationHelper {
    public static byte[] serialize(Object obj) {
        Objects.requireNonNull(obj);
        try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut)) {
            objectOut.writeObject(obj);
            return byteArrayOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static <T> T deserialize(byte[] objectBytes, Class<T> type) {
        Objects.requireNonNull(objectBytes);
        try (ByteArrayInputStream byteArrayIn = new ByteArrayInputStream(objectBytes);
             ObjectInputStream objectIn = new ObjectInputStream(byteArrayIn)) {
            //noinspection unchecked
            return (T) objectIn.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}