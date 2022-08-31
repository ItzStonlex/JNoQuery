package com.itzstonlex.jnq.connection;

import com.itzstonlex.jnq.JnqException;
import com.itzstonlex.jnq.jdbc.JDBCConnection;
import com.itzstonlex.jnq.jdbc.JDBCHelper;
import lombok.NonNull;

public class PostgresqlConnection extends JDBCConnection {

    public PostgresqlConnection(@NonNull String username, @NonNull String password) throws JnqException {
        super(JDBCHelper.toPostgreSQL("localhost", JDBCHelper.POSTGRESQL_PORT), username, password);
    }
}
