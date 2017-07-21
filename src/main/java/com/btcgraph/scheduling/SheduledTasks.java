package com.btcgraph.scheduling;

import com.btcgraph.service.BlockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SheduledTasks {
    Logger log = LoggerFactory.getLogger(SheduledTasks.class);

    BlockService blockService;

    @Autowired
    public SheduledTasks(BlockService blockService) {
        this.blockService = blockService;
    }

    //each minute load new blocks to database
    @Scheduled(initialDelay = 60000, fixedRate = 60000)
    public void queryNewBlocks() {
        log.debug("queryNewBlocks call");
        blockService.loadNewBlocks();
    }
}
