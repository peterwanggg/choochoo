//package com.pwang.kings.serde;
//
//
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
//import org.postgresql.jdbc.PgArray;
//
//import java.io.IOException;
//
///**
// * @author pwang on 12/28/17.
// */
//public class ItemDeserializer extends StdDeserializer<PgArray> {
//
//    public ItemDeserializer() {
//        this(null);
//    }
//
//    public ItemDeserializer(Class<?> vc) {
//        super(vc);
//    }
//
//    @Override
//    public PgArray deserialize(JsonParser jp, DeserializationContext ctxt)
//            throws IOException, JsonProcessingException {
//
//        JsonNode node = jp.getCodec().readTree(jp);
//
//
//        int id = (Integer) ((IntNode) node.get("id")).numberValue();
//        String itemName = node.get("itemName").asText();
//        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();
//
//        return new Item(id, itemName, new User(userId, null));
//    }
//}
