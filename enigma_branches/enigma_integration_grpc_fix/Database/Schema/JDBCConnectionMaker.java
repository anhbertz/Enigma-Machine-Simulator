package Schema;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Schema.JDBCConnectionMaker is a utility class for managing the details of
 *   instantiating a JDBC Connection object.
 * Although it resembles an Object Pool class, it is not quite that.
 * Mainly, its purpose is to centralize the information required
 *   to connect to a DB, so that the information is not duplicated
 *   across any other classes/methods that would need to instantiate
 *   a JDBC Connection.
 *
 * @author Scott Swanson
 * @version 2023.04.01
*/
public class JDBCConnectionMaker {
  private String dbName;
  private String hostName;
  private String portNumber;
  private String userName;
  private String password;

/**
 * Constructor requires all information needed to instantiate a
 *   functional Connection object.
 *   Note that the constructor makes no attempt to validate
 *     arguments.
 * @param   dbName_new      also called the schema
 * @param   hostName_new
 * @param   portNumber_new  
 * @param   userName_new  
 * @param   password_new  
 * @return  a valid, initialized JDBCConnector object
 */
  public JDBCConnectionMaker (
    String dbName_new,
    String hostName_new,
    String portNumber_new,
    String userName_new,
    String password_new
  ) {
    this.dbName = dbName_new;
    this.hostName = hostName_new;
    this.portNumber = portNumber_new;
    this.userName = userName_new;
    this.password = password_new;

    // Load the MySQL JDBC driver
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

/**
 * Attempts to instantiate a JDBC Connection object.
 * @return  a valid Connection object for the DB configuration
 *            the Schema.JDBCConnectionMaker was constructed with.
 *          null if the connection failed for any reason.
 */
  public Connection getConnection () {
    Connection dbCxn = null;
    String jdbcURL = 
        "jdbc:mysql://"
          + hostName
          + ":"
          + portNumber+ "/"
          + dbName;
    try {
      dbCxn = DriverManager.getConnection(
        jdbcURL,
        userName,
        password
      );
    }
    catch(SQLException e){
      System.out.println(e);
    }  

    return dbCxn;
  }
}


