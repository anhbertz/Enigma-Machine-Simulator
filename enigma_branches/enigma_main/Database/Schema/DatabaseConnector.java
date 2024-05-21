package Schema;

import java.sql.Connection;

public class DatabaseConnector {
    JDBCConnectionMaker ConMaker = null;
    Connection connection = null;
    public DatabaseConnector() {
        ConMaker = new JDBCConnectionMaker(
                "enigma",
                "enigma.c53ykan6isaf.us-east-1.rds.amazonaws.com",
                "3306",
                "admin",
                "team21_db");
        connection = ConMaker.getConnection();
    }
}
