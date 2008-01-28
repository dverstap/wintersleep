package org.wintersleep.springtryout;

import org.springframework.jdbc.datasource.DelegatingDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class TestDataSource extends DelegatingDataSource {

    public TestDataSource(DataSource targetDataSource) throws SQLException {
        super(targetDataSource);

        dropAllTables();
    }

    private void dropAllTables() throws SQLException {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("SET FOREIGN_KEY_CHECKS = 0;");
            statement.execute("SET UNIQUE_CHECKS = 0;");

            List<String> tableNames = new ArrayList<String>();
            ResultSet resultSet = statement.executeQuery("show tables");
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }
            resultSet.close();
            for (String tableName : tableNames) {
                statement.execute("drop table " + tableName);
            }

            statement.execute("SET FOREIGN_KEY_CHECKS = 1;");
            statement.execute("SET UNIQUE_CHECKS = 1;");
            statement.close();
        } finally {
            connection.close();
        }
    }

}
