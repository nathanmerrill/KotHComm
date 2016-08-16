package KoTHComm.messaging.serialization;

import java.util.Arrays;

public interface Serializer<T>{
    char SEPARATOR = ';';
    default String getSeparator(){
        char[] arr = new char[separationLevel()];
        Arrays.fill(arr,SEPARATOR);
        return new String(arr);
    }
    String serialize(T value);
    T deserialize(String representation);
    default int separationLevel(){
        return 0;
    }
}