package Models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class GraphDeserialize extends StdDeserializer<Graph<String>> {

    public GraphDeserialize() {
        this(null);
    }

    public GraphDeserialize(Class<?> vc) {
        super(vc);
    }

    @Override
    public Graph<String> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        ObjectMapper objectMapper = new ObjectMapper();
        boolean isDirected = node.get("directed").asBoolean();
        boolean isWeighted = node.get("weighted").asBoolean();
        Iterator<JsonNode> nodes = node.get("baseMap").elements();
        HashMap<String, Node<String>> baseMap = new HashMap<>();
        while (nodes.hasNext()) {
            JsonNode innerNode = nodes.next();
            String value = innerNode.get("value").textValue();
            HashMap<String, Double> adjacency = objectMapper.readValue(innerNode.get("adjacency").toString(), HashMap.class);
            baseMap.put(value, new Node<>(value, adjacency));
        }
        return new Graph<>(baseMap, isDirected, isWeighted);
    }
}
