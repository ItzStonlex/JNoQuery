package com.itzstonlex.jnq.content;

import com.itzstonlex.jnq.field.impl.IndexDataField;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.util.concurrent.CompletableFuture;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataTableContent {

    String name;

    @NonFinal
    @Setter
    DataSchemeContent scheme;

    public CompletableFuture<Void> clear() {
        return scheme.getConnection().createRequest(this).factory()
                .newDelete()
                .complete()
                .updateAsync();
    }

    public CompletableFuture<Void> drop() {
        return scheme.getConnection().createRequest(this).factory()
                .newDelete()
                .complete()
                .updateAsync();
    }

    public CompletableFuture<Void> create(@NonNull IndexDataField... fields) {
        return scheme.getConnection().createRequest(this).factory()
                .newCreateTable()
                .withExistsChecking()
                .set(fields)
                .complete()
                .updateAsync();
    }

    public boolean exists() {
        return scheme.getConnection().getTableContent(name) != null;
    }
}
