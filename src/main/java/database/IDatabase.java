package database;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabase {
    void requestExecute(String sqlRequest) throws SQLException, IOException;

    ResultSet requestExecuteWithReturn(String sqlRequest) throws SQLException, IOException;

    void close() throws SQLException;
}
