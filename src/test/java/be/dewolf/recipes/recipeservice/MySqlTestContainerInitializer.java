package be.dewolf.recipes.recipeservice;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySqlTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:oracle").asCompatibleSubstituteFor("mysql"));

    static {
        mySQLContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertyValues.of(
                "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                "spring.datasource.driverClassName=" + mySQLContainer.getDriverClassName(),
                "spring.datasource.username=" + mySQLContainer.getUsername(),
                "spring.datasource.password=" + mySQLContainer.getPassword()
        ).applyTo(applicationContext.getEnvironment());
    }
}
