package com.btcgraph.controller.rest;

import com.btcgraph.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockSeriesController {

    BlockService blockService;

    @Autowired
    public BlockSeriesController(BlockService blockService) {
        this.blockService = blockService;
    }

    @RequestMapping("/block-series")
    @CrossOrigin
    public Object[][] blockSeries() {
        return blockService.findTop(100)
                .stream()
                .map(block -> new Object[] {block.getHeight(), block.getSpeedRate()})
                .toArray(Object[][]::new);
    }
}

