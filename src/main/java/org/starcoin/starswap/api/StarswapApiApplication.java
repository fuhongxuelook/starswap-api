package org.starcoin.starswap.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.starcoin.starswap.api.service.HandleEventService;
import org.starcoin.starswap.subscribe.StarcoinEventSubscribeHandler;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableScheduling
public class StarswapApiApplication {

    private static final Logger LOG = LoggerFactory.getLogger(StarswapApiApplication.class);

    @Value("${starcoin.seeds}")
    private String[] seeds;

    @Value("${starcoin.network}")
    private String network;

    @Autowired
    private HandleEventService handleEventService;

    public static void main(String[] args) {
        SpringApplication.run(StarswapApiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runEventSubscribeHandler() {
        LOG.info("EXECUTING : EventsSubscribeHandler");
        //LOG.info("es url is " + esUrl);
        for (String seed : seeds) {
            Thread handlerThread = new Thread(new StarcoinEventSubscribeHandler(seed, network,
                    handleEventService));
            //Thread handlerThread = new Thread(new SubscribeHandler(seed, network, elasticSearchHandler));
            handlerThread.start();
        }
    }

}
