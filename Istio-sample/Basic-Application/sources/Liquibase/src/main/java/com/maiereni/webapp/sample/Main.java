package com.maiereni.webapp.sample;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.log4j.Log4j2;

import javax.sql.DataSource;

/**
 * @author Petre Maierean
 * @date 1/29/2025 6:29 AM
 **/
@Log4j2
public class Main {
    /**
     * Apply the changes to the database
     * @throws Exception
     */
    public void applyChanges() throws Exception {
        log.debug("Open database connection");
        DataSource dataSource = DataSourceBuilder.getInstance().getDatasource();
        log.debug("Apply changes");
        try (java.sql.Connection connection = dataSource.getConnection();) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
            log.info("The changes have been applied");
        }
    }

    public static void main(String[] args) {
        log.debug("Apply database changes");
        try {
            new Main().applyChanges();
        }
        catch (Exception e) {
            log.error("Failed to apply changes", e);
        }
    }

}