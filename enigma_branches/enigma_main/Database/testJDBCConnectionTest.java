import Schema.DatabaseConnector;
import Schema.JDBCConnectionMaker;
import Schema.Users;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class testJDBCConnectionTest {

    @Test
    void testJDBCConnect() {
        DatabaseConnector dbc = new DatabaseConnector();
        assertNotNull(dbc);
    }
    @Test
    void testCreate(){
        Users u1 = new Users();
        u1.setName("A");
        u1.setPassword("B");
        assertTrue(u1.create());
    }

    @Test
    void testRead(){
        Users u1 = new Users();
        u1.setName("B");
        u1.setPassword("C");
        assertTrue(u1.read(1));
    }
    @Test
    void testUpdate(){
        Users u1 = new Users();
        u1.setName("B");
        u1.setPassword("C");
        assertTrue(u1.read(1));
    }
}