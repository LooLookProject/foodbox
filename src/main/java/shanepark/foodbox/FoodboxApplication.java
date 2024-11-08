package shanepark.foodbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FoodboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodboxApplication.class, args);
    }

}
