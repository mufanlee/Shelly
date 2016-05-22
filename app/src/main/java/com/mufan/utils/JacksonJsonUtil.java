package com.mufan.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a util to deserialize data to json.
 * Created by ZLM on 2016/5/22.
 */
public class JacksonJsonUtil {

    private static ObjectMapper mapper;

    public static synchronized ObjectMapper getObjectMapper(boolean createNew){

        if (createNew) {
            return new ObjectMapper();
        } else if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    public static String objectToJson(Object object)throws Exception{

        try {
            ObjectMapper objectMapper = getObjectMapper(false);
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static String objectToJson(Object object,boolean createNew)throws Exception{

        try {
            ObjectMapper objectMapper = getObjectMapper(createNew);
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    public static Object jsonToObject(String json,Class<?> cla)throws Exception{

        try {
            ObjectMapper objectMapper = getObjectMapper(false);
            return objectMapper.readValue(json, cla);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public static Object jsonToObject(String json,Class<?> cla,boolean createNew)throws Exception{

        try {
            ObjectMapper objectMapper = getObjectMapper(createNew);
            return objectMapper.readValue(json, cla);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}
