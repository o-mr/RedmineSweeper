package com.kz.redminesweeper.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

class RedmineJsonConverter extends GsonHttpMessageConverter {

    public RedmineJsonConverter() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement dateElement, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String date = dateElement.getAsString();
                try {
                    return sdf.parse(date);
                } catch (ParseException e) {
                    throw new JsonParseException(e);
                }
            }
        });
        gb.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        setGson(gb.create());
    }
}
