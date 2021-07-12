package org.starcoin.starswap.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.starcoin.starswap.api.service.LiquidityAccountService;
import springfox.documentation.oas.annotations.EnableOpenApi;
import org.starcoin.starswap.subscribe.handler.EventsSubscribeHandler;

@SpringBootApplication
@EnableOpenApi
public class StarswapApiApplication {


    private static Logger LOG = LoggerFactory.getLogger(StarswapApiApplication.class);

    @Value("${starcoin.seeds}")
    private String[] seeds;

    @Value("${starcoin.network}")
    private String network;

    @Autowired
    private LiquidityAccountService liquidityAccountService;

    public static void main(String[] args) {
        SpringApplication.run(StarswapApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runEventSubscribeHandler() {
        LOG.info("EXECUTING : EventsSubscribeHandler");
        //LOG.info("es url is " + esUrl);
        for (String seed : seeds) {
            Thread handlerThread = new Thread(new EventsSubscribeHandler(seed, network, liquidityAccountService));
            //Thread handlerThread = new Thread(new SubscribeHandler(seed, network, elasticSearchHandler));
            handlerThread.start();
        }
    }

}
