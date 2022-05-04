package ca.gc.hc.siteDemo.services;

import java.util.Arrays;
import java.util.List;

import ca.gc.hc.siteDemo.dtos.DataRoot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

@Service
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

    public String serializeObjectToJsonString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public <T> T deserializeJsonStringToObject(String jsonStr, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, clazz);
    }

    public <T> List<T> deserializeJsonStringToObjectList(String jsonStr, Class<T[]> clazz) {
        Gson gson = new Gson();
        T[] array = gson.fromJson(jsonStr, clazz);
        return Arrays.asList(array);
    }

    public <T> DataRoot<T> deserializeJsonStringToDataCollection(String jsonStr, Class<T> type) {
        Gson gson = new Gson();
        Type collectionType = TypeToken.getParameterized(DataRoot.class, type).getType();
        DataRoot<T> dataCollection = gson.fromJson(jsonStr, collectionType);

        return dataCollection;
    }

}
