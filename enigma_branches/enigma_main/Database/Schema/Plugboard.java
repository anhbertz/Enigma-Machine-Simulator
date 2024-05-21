package Schema;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 *
 *
 */
public class Plugboard extends DatabaseConnector {
    private static final String dbTableName = "Plugboard";
    private static final String primaryKeyColumnName = "plugboard_id";
    private static final int primaryKeyColumnIdx = 1;

    // these member fields map directly to the Plugboard DB table columns
    // with the same names, but prefixed with "plugboard_" -- e.g., plugboard_id.
    private int id;
    private String wiring;

    /**
     * Provides protected access to the name of the column containing the Primary Key.
     * @return the name of the column containing the Primary Key
     */
    protected String primaryKeyColumnName() {
        return primaryKeyColumnName;
    };

    /**
     * Provides protected access to the index of the column containing the Primary Key.
     * @return the index of the column containing the Primary Key
     */
    protected int primaryKeyColumnIndex() {
        return primaryKeyColumnIdx;
    }

    /**
     * Provides protected access to the value of the Primary Key
     * @return the integer value corresponding to the Primary Key
     */
    protected int primaryKey() {
        return id;
    }

    /**
     * Copies values from a JDBC ResultSet object into the
     *   Plugboard internal data structures.
     * Does NOT set the primary key
     * @param resSet   ResultSet of a Select * from the Plugboard table.
     *             Precondition: the ResultSet iterator *must* point at a valid row.
     *               i.e., resSet.next() must have been called, and must
     *                 have returned true.
     * @return true
     */
    protected boolean populateFromResultSet(ResultSet resSet) {
// iterate over the fields of the resSet, picking up each database value by its name
//   and then putting it down with the appropriate public Setter.
        boolean success = false;

        try {
            this.wiring = resSet.getString("plugboard_wiring");
            success = true;

        } catch (SQLException e) {
            success = false;
        }

        return success;
    }

    /**
     * Inserts a single record into the Plugboard db table, populating it with the values
     *   stored in the current object instance.
     * Precondition: the dbConnectionMaker must have been set
     * @return true IFF the record was successfully inserted into the DB
     */
    public boolean create() {
        // assume success -- set result = true
        // build a SQL Insert statement string
        // get a Connection object from the dbConnectionMaker
        // get a Statement object from the Connection
        // execute the insert statement via the Statement object
        // get from the Statement the primary key that was generated
        //   and store it as the primary key value of this object instance
        // if the insert threw an exception, set result = false
        // return result
        boolean result = true;

        String insertSQL = "INSERT INTO " + dbTableName + " ("
                + "plugboard_wiring"
                + ") VALUES ('"
                + this.wiring
                + "')";

        try {
            Statement insertStatement = connection.createStatement();
            insertStatement.executeUpdate(insertSQL, Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = insertStatement.getGeneratedKeys();
            if (keys.next()) {
                this.id = keys.getInt(1);
            }
            keys.close();

        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * Reads a single record from the sql table, and populates this object instance with
     *   the values returned. read populates ONLY those fields contained in one row of
     *   the table mapped by the JDBCEntity subclass. It does NOT "drill" recursively
     *   into other sql records with related Foreign Keys. EVER.
     * Precondition: the dbConnector must have been set
     * @param queryPrimaryKey    the value of the primary key field for the target row.
     *                      Precondition: this must be either a valid primary key for
     *                        an existing row in the DB
     * @return true IFF the record was successfully read from the DB and the object instance
     *                      was successfully populated.
     */
    public boolean read(int queryPrimaryKey) {
// assume success -- set result = true
// build a SQL Select statement string
// get a Connection object from the dbConnectionMaker
// get a Statement object from the Connection
// execute the Select statement via the Statement object
// if the select threw an exception, set result = false
// otherwise:
//   set the primary key of this object to the queryPrimaryKey
//   call setDataFromResultSet with resultSet of the Select
//   set result to whatever is returned by setDataFromResultSet
// OR if the select threw an exception, set result = false
// return result

        boolean result = true;

        String selectSQL = "SELECT * FROM " + dbTableName
                + " WHERE " + primaryKeyColumnName + " = " + queryPrimaryKey;

        try {
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selectSQL);

            if (resultSet.next()) {
                this.id = queryPrimaryKey;
                this.wiring = resultSet.getString("plugboard_wiring");
            } else {
                result = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * Updates a single record of the Plugboard db table, populating it with the values
     *   stored in the current object instance.
     * Precondition: the dbConnectionMaker must have been set
     *               the primary key must be a valid key in the table
     * @return true IFF the record was successfully updated into the DB
     */
    protected boolean update() {
// assume success -- set result = true
// build a SQL Update statement string
// get a Connection object from the dbConnectionMaker
// get a Statement object from the Connection
// execute the update statement via the Statement object
// if the update throws an exception, set result = false
// return result
        boolean result = true;

        String updateSQL = "UPDATE " + dbTableName
                + " SET plugboard_wiring = " + this.wiring
                + "WHERE " + primaryKeyColumnName + " = " + this.id;

        try {
            Statement updateStatement = connection.createStatement();
            updateStatement.executeUpdate(updateSQL);

        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        }

        return result;
    }

    /**
     * Deletes a single record from the Plugboard db table, corresponding to the
     *   primary key value of the current object instance
     * Precondition: the dbConnectionMaker must have been set
     *               the primary key must be a valid key in the table
     * @return true IFF the record was successfully deleted from the DB
     */
    protected boolean delete () {
// assume success -- set result = true
// build a SQL Delete statement string
// get a Connection object from the dbConnectionMaker
// get a Statement object from the Connection
// execute the delete statement via the Statement object
// if delete throws an exception, set result = false
// return result
        boolean result = true;

        String deleteSQL = "DELETE FROM " + dbTableName
                + " WHERE " + primaryKeyColumnName + " = " + this.id;

        try {
            Statement deleteStatement = connection.createStatement();
            deleteStatement.executeUpdate(deleteSQL);

        } catch (SQLException e) {
            result = false;
        }

        return result;
    }

    /**
     * Either inserts or updates a single record of the Plugboard db table, depending on
     *   whether the primary key of the current object instance has been set.
     * Precondition: the dbConnectionMaker must have been set
     *               the primary key must be a valid key in the table, or 0
     * @return true IFF the record was successfully inserted/updated into the DB
     */
    public boolean save() {
// assume failure, set result = false
// if the current primary key is 0, call create
// if the current primary key is not 0, call update
// set the result to whatever was returned by the create/update call
// return the result
        boolean result = false;

        if(this.id == 0) {
            result = create();
        } else {
            result = update();
        }

        return result;
    };

    /**
     * Reads a single record of the Plugboard db table
     * Precondition: the dbConnectionMaker must have been set
     * @param queryPrimaryKey primary key of the desired record
     * @return true IFF the record was successfully read from the DB
     *                  and the current object instance successfully
     *                  populated with the returned values
     */
    public boolean load (int queryPrimaryKey) {
// assume failure, set result = false
// call read() with the queryPrimaryKey
// set result to whatever was returned by read()
// return result
        boolean result = false;

        if(read(queryPrimaryKey)) {
            result = true;
        }

        return result;
    };

    /**
     * Deletes the record of the Plugboard db table corresponding to the current object instance
     * Precondition: the dbConnectionMaker must have been set
     * @return true IFF the record was successfully deleted
     */
    public boolean remove() {
// assume failure, set result = false
// if the primary key is > 0
//   call delete()
//   set result to whatever was returned by delete()
// return result
        boolean result = false;
        if(this.id > 0){
            result = delete();
        }

        return result;
    };

    /**
     * Setter for plugboard_wiring table field
     * @param wiring_new
     * @return void
     */
    public void setWiring (String wiring_new){
        this.wiring = wiring_new;
    }


    /**
     * Getter for plugboard_wiring table field
     * @return String
     */
    public String getWiring (){
        return this.wiring;
    }


    /**
     * Constructor creates an empty Plugboard object
     * @return  a valid Plugboard object that does not map to a record in the DB
     */
    public Plugboard () {
        super();
        // Initialize the fields to default or empty values
        this.id = 0;
        this.wiring = "";
    }

    /**
     * Constructor creates a Plugboard object, and attempts to load a record from the DB
     * @return IFF the queryPrimaryKey is valid, a Plugboard object populated with the
     *         corresponding values for that record.
     *         otherwise, returns a valid but empty Plugboard object
     */
    public Plugboard (int queryPrimaryKey) {
        super();
        this.load(queryPrimaryKey);
    }
}

