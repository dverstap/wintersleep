/*
 * Copyright 2008 Davy Verstappen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wintersleep.repository;

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

    public void dropAllTables() throws SQLException {
        executeForEachTable("drop table ");
    }

    public void deleteAllData() throws SQLException {
        executeForEachTable("delete from ");
    }

    private void executeForEachTable(final String command) throws SQLException {
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
                statement.execute(command + tableName);
            }

            statement.execute("SET FOREIGN_KEY_CHECKS = 1;");
            statement.execute("SET UNIQUE_CHECKS = 1;");
            statement.close();
        } finally {
            connection.close();
        }
    }

}
