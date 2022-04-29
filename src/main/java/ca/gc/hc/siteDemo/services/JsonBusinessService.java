package ca.gc.hc.siteDemo.services;

import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonBusinessService {

    public <T> List<T> convertJsonToData(String json, TypeReference<List<T>> typeReference)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, typeReference);
    }

    public <T> T convertJsonToData(String json, Class<T> dtoClass)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(json, dtoClass);
    }

    public <T> String getDtoToJson(T dto) {
        return getGson().toJson(dto);
    }

    private Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.setDateFormat("yyyy-MM-dd").create();
    }

}
