package game.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumSerializer implements Serializer<String>{
    private final Map<String, String> possibleValues;
    private final Map<String, String> reverseValues;
    public EnumSerializer(HashMap<String, String> possibleValues){
        this.possibleValues = possibleValues;
        reverseValues = possibleValues.keySet().stream()
                .collect(Collectors.toMap(
                        possibleValues::get,
                        Function.identity()));
    }

    @Override
    public int separationLevel() {
        return 0;
    }

    @Override
    public String deserialize(String representation) {
        return reverseValues.get(representation);
    }

    @Override
    public String define() {
        return "String("+String.join(",", possibleValues.values())+")";
    }

    @Override
    public String serialize(String value) {
        return possibleValues.get(value);
    }
}
