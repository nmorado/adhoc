package dbfit.fixture;

import java.sql.*;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;

import dbfit.util.Log;

public class StatementExecution {
    private Savepoint savepoint;
    private PreparedStatement statement;

    public StatementExecution(PreparedStatement statement) {
        this(statement, true);
    }

    public StatementExecution(PreparedStatement statement, boolean clearParameters) {
        this.statement = statement;
        if (clearParameters) {
            try {
                statement.clearParameters();
            } catch (SQLException e) {
                throw new RuntimeException("Exception while clearing parameters on PreparedStatement", e);
            }
        }
    }

    public static class Savepoint {
        private Connection connection;
        private java.sql.Savepoint savepoint;

        public Savepoint(Connection connection) {
            this.connection = connection;
            create();
        }

        protected void create() {
            String savepointName = "eee" + this.hashCode();
            if (savepointName.length() > 10) savepointName = savepointName.substring(1, 9);
            savepoint = null;

            try {
                savepoint = connection.setSavepoint(savepointName);
            } catch (SQLException e) {
                throw new RuntimeException("Exception while setting savepoint", e);
            }
        }

        public void release() {
            try {
                connection.releaseSavepoint(savepoint);
            } catch (SQLException e) {
                Log.log("Exception here releasing savepoint... " + e.getMessage());
                System.out.println("Exception here... " + e.getMessage());
                /*
                Now, the correct thing would be to rethrow the exception here.

                However, *some* databases (yes, I'm looking at you, Oracle) don't support savepoint releasing
                (http://docs.oracle.com/cd/B10500_01/java.920/a96654/jdbc30ov.htm#1006294) but at the same time
                don't throw an SQLFeatureNotSupportedException
                (http://stackoverflow.com/questions/10667292/jdbc-check-for-capability-savepoint-release)
                like they're supposed to
                (http://docs.oracle.com/javase/7/docs/api/java/sql/Connection.html#releaseSavepoint(java.sql.Savepoint) ).
                 */
            }
        }

        public void restore() {
            try {
                connection.rollback(savepoint);
            } catch (SQLException e) {
                throw new RuntimeException("Exception while restoring savepoint", e);
            }
        }
    }

    public void run() throws SQLException {
        // nmorado: for testing of -883
        //createSavepoint();

        try {
            logStatement();
            statement.execute();
            //savepoint.release();
        } catch (SQLException e) {
            Log.log("Exception here... " + e.getMessage());
            System.out.println("Exception here... " + e.getMessage());
            //savepoint.restore();
            throw e;
        }
    }

    private void createSavepoint() {
        try {
            savepoint = new Savepoint(statement.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting connection for setting savepoint", e);
        }
    }

    public void registerOutParameter(int index, int sqlType) throws SQLException {
        convertStatementToCallable().registerOutParameter(index, sqlType);
    }

    public void setObject(int index, Object value) throws SQLException {
        statement.setObject(index, value);
    }

    public Object getObject(int index) throws SQLException {
        return convertStatementToCallable().getObject(index);
    }

    //really ugly, but a hack to support mysql, because it will not execute inserts with a callable statement
    private CallableStatement convertStatementToCallable() throws SQLException {
        if (statement instanceof CallableStatement) return (CallableStatement) statement;
        throw new SQLException("This operation requires a callable statement instead of "+ statement.getClass().getName());
    }

    public Object getGeneratedKey(Class<?> type) throws SQLException, IllegalAccessException {
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {//todo: first try to find by name (mysql does not support name-based return keys)
            Object value;
            if (type == Integer.class) {
                value = rs.getInt(1);
            } else if (type == Long.class) {
                value = rs.getLong(1);
            } else {
                value = rs.getObject(1);
            }
            return value;
        }
        throw new IllegalAccessException("statement has not generated any keys");
    }

    public PreparedStatement getStatement() {
        return this.statement;
    }

    private void logStatement() {
        System.out.println(statement.toString());
        Log.log(statement.toString());
    }
}
