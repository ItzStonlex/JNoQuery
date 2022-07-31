package com.itzstonlex.jnq.sql;

import com.itzstonlex.jnq.DataConnection;
import com.itzstonlex.jnq.DataMapProvider;
import com.itzstonlex.jnq.DataValidator;
import com.itzstonlex.jnq.content.DataTableContent;
import com.itzstonlex.jnq.impl.decorator.DataMapProviderDecorator;
import com.itzstonlex.jnq.impl.decorator.DataValidateDecorator;
import com.itzstonlex.jnq.request.Request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SqlConnection implements DataConnection {

    private static final DataValidator PARENT_DATA_VALIDATOR = new DataValidateDecorator();
    private static final DataMapProvider PARENT_DATA_MAP_PROVIDER = new DataMapProviderDecorator();

    @Getter
    private final DataValidator validator;

    @Getter
    private final SqlConnectionMeta meta;

    @Getter
    private final DataMapProvider dataMapProvider;

    private final Connection connection;

    public SqlConnection(@NonNull DataValidator validator, @NonNull DataMapProvider dataMapProvider,
                         @NonNull String driverCls, @NonNull String url,
                         @NonNull String user, @NonNull String password) throws SQLException, ClassNotFoundException {

        Class.forName(driverCls);

        this.validator = validator;
        this.dataMapProvider = dataMapProvider;

        this.connection = DriverManager.getConnection(url, user, password);

        this.meta = new SqlConnectionMeta(connection);

        this._applyMetaProperties(connection);
    }

    public SqlConnection(@NonNull String driverCls, @NonNull String url,
                         @NonNull String user, @NonNull String password) throws SQLException, ClassNotFoundException {

        this(PARENT_DATA_VALIDATOR, PARENT_DATA_MAP_PROVIDER, driverCls, url, user, password);
    }

    private void _applyMetaProperties(Connection connection) {
        // TODO - apply @meta for @connection properties
    }

    @Override
    public boolean checkConnection(int timeout) {
        return connection != null && checkConnection(timeout);
    }

    @Override
    public @NonNull Set<DataTableContent> getTablesContents() {
        return null;
    }

    @Override
    public @NonNull DataTableContent getTableContent(String name) {
        return null;
    }

    @Override
    public @NonNull Request createRequest(@NonNull DataTableContent content) {
        return null;
    }

    @Override
    public CompletableFuture<Void> close() throws SQLException {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();

        if (checkConnection()) {
            connection.close();

            completableFuture.complete(null);
        }

        return completableFuture;
    }

}
