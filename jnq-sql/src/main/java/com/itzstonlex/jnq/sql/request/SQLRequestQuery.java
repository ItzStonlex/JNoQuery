package com.itzstonlex.jnq.sql.request;

import com.itzstonlex.jnq.request.RequestExecutor;
import com.itzstonlex.jnq.request.query.RequestQuery;
import com.itzstonlex.jnq.sql.SQLRequest;
import com.itzstonlex.jnq.sql.SQLRequestExecutor;
import com.itzstonlex.jnq.sql.SQLWrapperStatement;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class SQLRequestQuery implements RequestQuery {

    SQLRequest request;

    protected abstract String toSQL();

    protected abstract Object[] toFieldValues();

    @Override
    public @NonNull RequestExecutor compile() {

        SQLWrapperStatement statement = new SQLWrapperStatement(request, toFieldValues());
        return new SQLRequestExecutor(toSQL(), statement);
    }

    @Override
    public String toString() {
        return toSQL();
    }
}
