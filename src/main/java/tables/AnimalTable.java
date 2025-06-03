package tables;

import database.IDatabase;
import database.MySqlConnectionDatabase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnimalTable extends  AbsTable {

    private IDatabase iDatabase = null;

    public AnimalTable() {
        super("animal");
        iDatabase = new MySqlConnectionDatabase();
    }

    private boolean isTableExist() throws SQLException, IOException {
        String sqlReq = "show tables";
        ResultSet tables = iDatabase.requestExecuteWithReturn(sqlReq);

        while(tables.next()) {
            String tableName = tables.getString(1);
            if(tableName.equals(name)) {
                return true;
            }
        }
        return false;

    }

    public void  createTable() throws SQLException, IOException {
        if(!isTableExist()) {
            String sqlRequest = String.format("create table %s (id int, name VARCHAR(20), age int, weight int, color VARCHAR(20))");
            iDatabase.requestExecute(sqlRequest);
        }

    }
}
