package be.dewolf.recipes.recipeservice.config.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Component("mysql")
@Slf4j
@ConditionalOnProperty(value = "app.data.inmemory", havingValue = "false", matchIfMissing = true)
public class MySqlHealthIndicator implements HealthIndicator {

    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try {
            log.info("checking health");
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            jdbcTemplate.queryForObject("SELECT 1", Integer.class); // Execute a simple query
            return Health.up().withDetail("hello world", "UP").build(); // Database is healthy
        } catch (Exception e) {
            return Health.down().withException(e).build(); // Database is not healthy
        }
    }
}
