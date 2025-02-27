/*
 * =========================================================================================
 * Copyright (c) 2024 - 2025 to Maiereni Software and Consulting Inc
 * =========================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.maiereni.webapp.sample;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;

/**
 * @author Petre Maierean
 * @date 1/29/2025 6:46 AM
 **/
@Log4j2
public class DataSourceBuilder {
    private final String driver = System.getenv("DATABASE_DRIVER");
    private final String url = System.getenv("DATABASE_URL");
    private final String username = System.getenv("DATABASE_USERNAME");
    private final String password = System.getenv("DATABASE_PASSWORD");

    public static DataSourceBuilder getInstance() throws DatabaseConnectionException {
        DataSourceBuilder dataSourceBuilder = new DataSourceBuilder();
        assertNotNull(dataSourceBuilder.driver, "The driver to connect to the database is not set. To set it, use environment variable DATABASE_DRIVER");
        assertNotNull(dataSourceBuilder.url, "The password to connect to the database is not set. To set it, use environment variable DATABASE_URL");
        assertNotNull(dataSourceBuilder.username, "The password to connect to the database is not set. To set it, use environment variable DATABASE_USERNAME");
        assertNotNull(dataSourceBuilder.password, "The password to connect to the database is not set. To set it, use environment variable DATABASE_PASSWORD");
        log.debug("Connect to database at {} with user {}", dataSourceBuilder.url, dataSourceBuilder.username);
        System.getProperty("username", dataSourceBuilder.username);
        System.getProperty("password", dataSourceBuilder.password);

        return dataSourceBuilder;
    }

    public DataSource getDatasource() throws DatabaseConnectionException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setInitialSize(1);
        dataSource.setMaxIdle(60);
        dataSource.setMinIdle(60);
        log.debug("Datasource object has been created");
        return dataSource;
    }

    private static void assertNotNull(String value, String text) throws DatabaseConnectionException {
        if (StringUtils.isBlank(value)) {
            throw new DatabaseConnectionException(text);
        }
    }
}
