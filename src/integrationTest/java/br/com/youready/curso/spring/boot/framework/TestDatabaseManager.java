package br.com.youready.curso.spring.boot.framework;

import java.io.IOException;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestDatabaseManager {

  private static final Logger log = LoggerFactory.getLogger(TestDatabaseManager.class);

  public static final PostgreSQLContainer<?> PGSQL_CONTAINER;
  private static final String DOCKER_IMAGE = "postgres:17.5";
  private static final String TEMPLATE_DB_NAME = "aula_db_template";
  private static final String DB_NAME = "aula_db";
  private static final String USERNAME = "aula";
  private static final String PASSWORD = "aula";

  static {
    PGSQL_CONTAINER =
        new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withReuse(true)
            .withCreateContainerCmdModifier(
                createContainerCmd -> createContainerCmd.withName("test-db"));

    PGSQL_CONTAINER.start();
    log.info("PostgreSQL container started: {}", PGSQL_CONTAINER.getJdbcUrl());
  }

  public static void isDbRunning() {
    PGSQL_CONTAINER.isRunning();
  }

  public static void dropTestDB() {
    dropDatabase(DB_NAME);
  }

  public static void dropTemplateDB() {
    dropDatabase(TEMPLATE_DB_NAME);
  }

  public static void createTestDB() {
    log.info("Attempting to create database: {}", TestDatabaseManager.DB_NAME);
    executePsqlCommand(
        "CREATE DATABASE " + TestDatabaseManager.DB_NAME,
        "Database " + TestDatabaseManager.DB_NAME + " created successfully.",
        "Error creating database " + TestDatabaseManager.DB_NAME);
  }

  public static void runMigrationOnTestDB() {
    log.info("Running Flyway migrations on database: {}", DB_NAME);
    try {
      Flyway flyway =
          Flyway.configure()
              .dataSource(
                  PGSQL_CONTAINER.getJdbcUrl(),
                  PGSQL_CONTAINER.getUsername(),
                  PGSQL_CONTAINER.getPassword())
              .locations("classpath:db/migrations")
              .load();

      flyway.migrate();
      log.info("Flyway migrations completed successfully.");
    } catch (Exception e) {
      log.error("Error running Flyway migrations: {}", e.getMessage(), e);
      throw new TestDatabaseManagerException("Error running Flyway migrations", e);
    }
  }

  public static void createTemplateDBFromTestDB() {
    dropDatabase(TEMPLATE_DB_NAME);
    createDatabaseFromTemplate(TEMPLATE_DB_NAME, DB_NAME);
  }

  private static void createTestDBFromTemplate() {
    dropDatabase(DB_NAME);
    createDatabaseFromTemplate(DB_NAME, TEMPLATE_DB_NAME);
  }

  private static void dropDatabase(String dbName) {
    log.info("Attempting to drop database: {}", dbName);
    killAllPGSQLConnections(dbName);
    executePsqlCommand(
        "DROP DATABASE IF EXISTS " + dbName,
        "Database " + dbName + " dropped successfully.",
        "Error dropping database " + dbName);
  }

  private static void createDatabaseFromTemplate(String newDbName, String templateDbName) {
    log.info("Attempting to create database {} from template {}", newDbName, templateDbName);
    killAllPGSQLConnections(newDbName);
    killAllPGSQLConnections(templateDbName);
    executePsqlCommand(
        "CREATE DATABASE " + newDbName + " TEMPLATE " + templateDbName,
        "Database " + newDbName + " created successfully from template " + templateDbName + ".",
        "Error creating database " + newDbName + " from template " + templateDbName);
  }

  private static void killAllPGSQLConnections(String dbName) {
    String query =
        String.format(
            """
                SELECT pg_terminate_backend(pg_stat_activity.pid)
                FROM pg_stat_activity
                WHERE datname = '%s'
                    AND pid <> pg_backend_pid()
                """,
            dbName);

    log.info("Killing all connections to database: {}", dbName);
    executePsqlCommand(
        query,
        "Terminated connections to " + dbName + " successfully.",
        "Error killing connections to database " + dbName);
  }

  private static void executePsqlCommand(
      String command, String successMessage, String errorMessage) {
    try {
      Container.ExecResult execResult =
          PGSQL_CONTAINER.execInContainer("psql", "-U", USERNAME, "-d", "postgres", "-c", command);

      if (execResult.getExitCode() != 0) {
        log.error("{}: {} (Command: {})", errorMessage, execResult.getStderr(), command);
        throw new TestDatabaseManagerException(errorMessage + ": " + execResult.getStderr());
      } else {
        log.info(successMessage);
      }
    } catch (IOException | InterruptedException e) {
      log.error("{}: {}", errorMessage, e.getMessage(), e);
      throw new TestDatabaseManagerException(errorMessage, e);
    }
  }

  public static void restartTestDB() {
    createTestDBFromTemplate();
  }
}
