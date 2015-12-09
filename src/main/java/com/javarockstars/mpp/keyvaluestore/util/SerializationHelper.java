package com.javarockstars.mpp.keyvaluestore.util;

import java.io.*;

/**
 * Author: dedocibula
 * Created on: 25.11.2015.
 */
public final class SerializationHelper {
    public static byte[] serialize(Serializable obj) {
        try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
             ObjectOutputStream objectOut = new ObjectOutputStream(byteArrayOut)) {
            objectOut.writeObject(obj);
            return byteArrayOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public static <T extends Serializable> T deserialize(byte[] objectBytes, Class<T> type) {
        if (objectBytes == null) return null;
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
