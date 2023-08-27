package be.dewolf.recipes.recipeservice.config.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component("google")
@Slf4j
public class GoogleHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        log.info("checking health");
        try {
            InetAddress byName = InetAddress.getByName("8.8.8.8");
            return Health.up().build();
        } catch (UnknownHostException e) {
            log.info("google down?", e);
            return Health.down().withException(e).build();
        }
    }

}
