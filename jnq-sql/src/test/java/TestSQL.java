import com.itzstonlex.jnq.DataConnection;
import com.itzstonlex.jnq.content.DataTableContent;
import com.itzstonlex.jnq.field.DataFieldType;
import com.itzstonlex.jnq.field.impl.IndexDataField;
import com.itzstonlex.jnq.field.impl.ValueDataField;
import com.itzstonlex.jnq.field.request.FieldWhereRequest;
import com.itzstonlex.jnq.request.option.RequestConcurrency;
import com.itzstonlex.jnq.request.option.RequestFetchDirection;
import com.itzstonlex.jnq.request.option.RequestHoldability;
import com.itzstonlex.jnq.request.option.RequestType;
import com.itzstonlex.jnq.response.DataResponse;
import com.itzstonlex.jnq.response.DataResponseRow;
import com.itzstonlex.jnq.sql.SqlConnection;
import com.itzstonlex.jnq.sql.util.SQLUtility;
import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;

public class TestSQL {

    @SneakyThrows
    public static void main(String[] args) {

        // create a sql-connection channel.
        String jdbcUrl = SQLUtility.toMysqlJDBC("localhost", 3306);
        String driverCls = SQLUtility.MYSQL_DRIVER_CLASS;

        DataConnection connection = new SqlConnection(driverCls, jdbcUrl, "root", "");

        // management with a table.
        DataTableContent tableContent = connection.getTableContent("registered_users");

        if (!tableContent.exists()) {
            tableContent.create(
                    IndexDataField.createPrimaryNotNullAutoIncrement("id"),

                    IndexDataField.createNotNull(DataFieldType.VAR_CHAR, "nickname"),
                    IndexDataField.createNotNull(DataFieldType.INT, "age")

            ).thenAccept(unused -> {

                System.out.printf("Table `%s` was success created!%n", tableContent.getName());
            });
        }

        // execution a queries by remote sql-connection.
        CompletableFuture<DataResponse> responseFuture = connection.createRequest(tableContent)
                .with(RequestType.TYPE_FORWARD_ONLY)
                .with(RequestFetchDirection.FETCH_REVERSE)
                .with(RequestConcurrency.CONCUR_UPDATABLE)
                .with(RequestHoldability.CLOSE_CURSORS_AT_COMMIT)

                .factory()
                .newFind()

                .where(FieldWhereRequest.Operator.LIKE, ValueDataField.create("nickname", "itzstonlex"))
                .withSorting(FieldWhereRequest.Operator.MORE, ValueDataField.create("age", 14))

                .complete()
                .fetchAsync();

        responseFuture.thenAccept(response -> {

            for (DataResponseRow row : response) {

                String nickname = row.getString("nickname");

                int age = row.getInt("age");

                // todo
            }
        });

        // common manipulations with a table.
        tableContent.clear().thenAccept(unused -> System.out.printf("Table `%s` was success clear!%n", tableContent.getName()));
        tableContent.drop().thenAccept(unused -> System.out.printf("Table `%s` was success dropped!%n", tableContent.getName()));
    }

}