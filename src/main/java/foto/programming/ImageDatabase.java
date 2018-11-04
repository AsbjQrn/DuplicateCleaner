/*
package foto.programming;

import java.sql.*;
import java.time.LocalDateTime;

public class ImageDatabase {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/test";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private String dropMediaStatement = "DROP TABLE MEDIA";
    private String createMediaStatement = "CREATE TABLE MEDIA(" +
            "id bigint primary key, " +
            "path varchar(255)," +
            "optageDato timestamp," +
            "size long," +
            "first1000Checksum long," +
            "fullChecksum long," +
            "        )";
    private String insertPrepared = "INSERT INTO MEDIA" + "(id, path, optageDato, size, first1000Checksum, fullChecksum) values" + "(?,?,?,?,?,?)";
    Connection connection = null;
    private Statement statement = null;
    PreparedStatement preparedStatement = null;

    public ImageDatabase() throws SQLException {
        this.connection = getDBConnection();
        createStatement();
        executeSqlStatement(createMediaStatement);
        createPreparedStatement(insertPrepared);
    }

    private boolean executeSqlStatement(String sql) {

        Statement stmt = null;
        try {
            stmt = createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Statement createStatement() throws SQLException {
        if (this.statement == null) {
            this.statement = connection.createStatement();
        }
        return this.statement;
    }

    private Statement createPreparedStatement(String sql) throws SQLException {
        if (this.preparedStatement == null) {
            this.preparedStatement = connection.prepareStatement(sql);
        }
        return this.preparedStatement;
    }

    public void addStatementToBeExecuted(){}

    private static void insertWithPreparedStatement(id, String path, LocalDateTime optageDato, long size, long first1000Checksum, long fullChecksum) throws SQLException {




        try {


            insertPreparedStatement =
            insertPreparedStatement.setInt(1, 1);
            insertPreparedStatement.setString(2, "Jose");
            insertPreparedStatement.addBatch();
            insertPreparedStatement.close();

            selectPreparedStatement = connection.prepareStatement(SelectQuery);
            ResultSet rs = selectPreparedStatement.executeQuery();
            System.out.println("H2 Database inserted through PreparedStatement");
            while (rs.next()) {
                System.out.println("Id " + rs.getInt("id") + " Name " + rs.getString("name"));
            }
            selectPreparedStatement.close();

            connection.commit();
        } catch (SQLException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
                    DB_PASSWORD);
            dbConnection.setAutoCommit(false);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return dbConnection;
    }

}
*/
