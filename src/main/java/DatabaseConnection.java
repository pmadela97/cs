import java.sql.Connection;
        import java.sql.DriverManager;

public class DatabaseConnection {
    Connection con = null;
    DatabaseConnection() {
        try {
        //Registering the HSQLDB JDBC driver
        //Class.forName("org.hsqldb.jdbc.JDBCDriver");
        //Creating the connection with HSQLDB
        con = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/testdb", "sa", "");
        if (con != null) {
            System.out.println("Connection created successfully");
        } else {
            System.out.println("Problem with creating connection");
        }

    }  catch(
    Exception e)

    {
        e.printStackTrace(System.out);
    }
}

    public Connection getCon() {
        return con;
    }
}
