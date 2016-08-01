package messaging;

import messaging.serialization.Serializer;

public class SerializedCommunicator<T, U> implements Communicator<T, U> {
    private final Communicator<String, String> passTo;
    private final Serializer<T> serializer;
    private final Serializer<U> deserializer;
    public SerializedCommunicator(Communicator<String, String> passTo, Serializer<T> inputSerializer, Serializer<U> outputDeserializer){
        this.passTo = passTo;
        this.serializer = inputSerializer;
        this.deserializer = outputDeserializer;
    }

    @Override
    public U sendMessage(T message, String method, int timeout) {
        return deserializer.deserialize(passTo.sendMessage(serializer.serialize(message), method, timeout));
    }

}
