package com.itzstonlex.jnq.orm.mapper;

import com.google.gson.Gson;
import com.itzstonlex.jnq.orm.ObjectMapper;
import com.itzstonlex.jnq.orm.ObjectMapperProperties;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class JsonMapper<T> implements ObjectMapper<T> {

    private static final Gson PARSER = new Gson();

    @Override
    public void mapping(@NonNull T src, @NonNull ObjectMapperProperties properties) {
        properties.set("json", PARSER.toJson(src));
    }

    @Override
    public @NonNull T fetch(@NonNull Class<T> cls, @NonNull ObjectMapperProperties properties) {
        return PARSER.fromJson(properties.<String>ofNullable("json"), cls);
    }

}
