package pl.psi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

@SpringBootApplication(scanBasePackages = "pl.psi")
public class GameServerApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GameServerApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GameServerApplication.class, args);
    }
    @Bean
    public GuavaModule guavaModule() {
        return new GuavaModule();
    }
}
