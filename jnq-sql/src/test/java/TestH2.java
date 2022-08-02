import com.itzstonlex.jnq.DataConnection;
import com.itzstonlex.jnq.exception.JnqException;
import com.itzstonlex.jnq.field.FieldOperator;
import com.itzstonlex.jnq.field.FieldType;
import com.itzstonlex.jnq.impl.content.SchemaContent;
import com.itzstonlex.jnq.impl.field.IndexDataField;
import com.itzstonlex.jnq.impl.field.ValueDataField;
import com.itzstonlex.jnq.response.ResponseLine;
import com.itzstonlex.jnq.sql.SQLConnection;

public class TestH2 {

    public static void main(String[] args)
    throws JnqException {

        DataConnection connection = new SQLConnection("org.h2.Driver", "jdbc:h2:mem:", "root", "password");
        SchemaContent schemaContent = connection.getSchemaContent("public");

        if (schemaContent == null) {
            System.out.println("wtf ?");
            return;
        }
        
        connection.createRequest(schemaContent)
                .toFactory()
                .<IndexDataField>fromQuery("CREATE TABLE IF NOT EXISTS `reg_users` ({0}, {1}, {2}, {3})")

                .sessionAppender()
                    .append(IndexDataField.createPrimaryNotNullAutoIncrement("id"))
                    .append(IndexDataField.createNotNull(FieldType.VARCHAR, "name"))
                    .append(IndexDataField.createNotNull(FieldType.LONG, "register_date"))
                    .append(IndexDataField.createNotNull(FieldType.LONG, "last_update_date"))
                    .backward()

                .compile()
                .updateTransaction();

        connection.updateContents();

        connection.createRequest(schemaContent)
                .toFactory()
                .<ValueDataField>fromQuery("INSERT INTO `{content}`.`reg_users` ({name0}, {name1}, {name2}) VALUES (?, ?, ?)")

                .sessionAppender()
                    .append(ValueDataField.create("name", "itzstonlex"))
                    .append(ValueDataField.create("register_date", System.currentTimeMillis()))
                    .append(ValueDataField.create("last_update_date", System.currentTimeMillis()))
                    .backward()

                .compile()
                .updateTransactionAsync()

                .whenComplete((updateResponse, error) ->  {

                    System.out.println("INSERT RESPONSE:");
                    System.out.println(" Inserted User ID: " + updateResponse.getGeneratedKey());
                    System.out.println(" - " + updateResponse.getAffectedRows() + " affected rows");
                    System.out.println();
                });

        connection.createRequest(schemaContent.getTableContent("reg_users"))
                .toFactory()
                .newFinder()

                .sessionFilter()
                    .and(ValueDataField.create("name", "itzstonlex"))
                    .backward()

                .compile()
                .fetchFirstLineAsync()

                .whenComplete((response, throwable) -> {

                    System.out.println("USER FETCH RESPONSE:");
                    System.out.println(" ID: " + response.getNullableInt("id"));
                    System.out.println(" Name: " + response.getNullableString("name"));
                    System.out.println(" Register Time Millis: " + response.getNullableLong("register_date"));
                    System.out.println(" Last Update Time Millis: " + response.getNullableLong("last_update_date"));
                    System.out.println();
                });

        connection.createRequest(schemaContent.getTableContent("reg_users"))
                .toFactory()
                .newUpdate()

                .sessionUpdater()
                    .and(ValueDataField.create("last_update_date", System.currentTimeMillis()))
                    .backward()

                .sessionFilter()
                    .and(FieldOperator.MORE_WITH_EQUAL, ValueDataField.create("id", 1))
                    .and(ValueDataField.create("name", "itzstonlex"))
                    .backward()

                .compile()
                .updateTransactionAsync()

                .whenComplete((updateResponse, throwable) -> {

                    System.out.println("UPDATE RESPONSE:");
                    System.out.println(" - " + updateResponse.getAffectedRows() + " affected rows");
                    System.out.println();
                });
    }

}
