package org.starcoin.starswap.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
public class StarswapApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarswapApiApplication.class, args);
    }

}
