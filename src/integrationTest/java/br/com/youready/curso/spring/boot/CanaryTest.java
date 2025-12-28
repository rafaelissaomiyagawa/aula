package br.com.youready.curso.spring.boot;

import static org.assertj.core.api.Assertions.assertThatNoException;

import br.com.youready.curso.spring.boot.framework.TestDataInitializer;
import br.com.youready.curso.spring.boot.framework.TestDatabaseManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.DockerClientFactory;

@DisplayName("Test Environment Canary Tests")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class CanaryTest {

  @Nested
  @Order(1)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayName("A. Database and Docker Configuration")
  class DatabaseCanaryTests {

    @Test
    @DisplayName("Verifies Docker environment is accessible and configured")
    @Order(10)
    void dockerMustBeWellConfigured() {
      DockerClientFactory instance = DockerClientFactory.instance();

      assertThatNoException().isThrownBy(() -> instance.client().infoCmd().exec());
    }

    @Test
    @DisplayName("Verifies Testcontainers reuse is enabled in properties file")
    @Order(15)
    void testReuseIsEnabledInPropertiesFile() {
      String expectedProperty = "testcontainers.reuse.enable";
      String expectedValue = "true";

      Path homeDir = Paths.get(System.getProperty("user.home"));
      Path propertiesFilePath = homeDir.resolve(".testcontainers.properties");

      Properties prop = new Properties();

      try (InputStream input = new FileInputStream(propertiesFilePath.toFile())) {
        prop.load(input);

        String actualValue = prop.getProperty(expectedProperty);

        Assertions.assertThat(actualValue)
            .isEqualTo(expectedValue)
            .withFailMessage(
                "The property 'testcontainers.reuse.enable' should be 'true' in the properties file.");

      } catch (IOException ex) {
        Assertions.fail(
            "Could not read the ~/.testcontainers.properties file or it does not exist: "
                + ex.getMessage());
      }
    }

    @Test
    @DisplayName("Verifies PostgreSQL Testcontainer is startable")
    @Order(20)
    void postgresContainerMustBeStartable() {
      assertThatNoException().isThrownBy(TestDatabaseManager::isDbRunning);
    }

    @Test
    @DisplayName("Drops the test database if it exists")
    @Order(25)
    void dropTestDB() {
      assertThatNoException().isThrownBy(TestDatabaseManager::dropTestDB);
    }

    @Test
    @DisplayName("Creates the test database")
    @Order(30)
    void createTestDB() {
      assertThatNoException().isThrownBy(TestDatabaseManager::createTestDB);
    }

    @Test
    @DisplayName("Runs Flyway migrations on the test database")
    @Order(35)
    void runMigration() {
      assertThatNoException().isThrownBy(TestDatabaseManager::runMigrationOnTestDB);
    }

    @Test
    @DisplayName("Creates a template database from the test database")
    @Order(40)
    void createTemplate() {
      assertThatNoException().isThrownBy(TestDatabaseManager::createTemplateDBFromTestDB);
    }
  }

  @Nested
  @Order(2)
  @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
  @DisplayName("B. Spring Context and Data Initialization")
  class SpringCanaryTests extends BaseIntegrationTest {

    @Autowired TestDataInitializer testDataInitializer;

    @Test
    @DisplayName("Initializes the database with default test data")
    @Order(10)
    void initializeWithTestData() {
      assertThatNoException().isThrownBy(testDataInitializer::initializeDBWithDefaultProducts);
    }

    @Test
    @DisplayName("Creates a template database from the test database with data")
    @Order(20)
    void createTemplateDBWithData() {
      assertThatNoException().isThrownBy(TestDatabaseManager::createTemplateDBFromTestDB);
    }

    @Test
    @DisplayName("Recreates the test database from the template")
    @Order(30)
    void recreateTestDB() {
      assertThatNoException().isThrownBy(TestDatabaseManager::restartTestDB);
    }
  }
}
