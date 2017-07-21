package com.btcgraph;

import com.btcgraph.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class StateInitializer implements CommandLineRunner {

    JdbcTemplate jdbcTemplate;

    BlockService blockService;

    @Autowired
    public StateInitializer(JdbcTemplate jdbcTemplate, BlockService blockService) {
        this.jdbcTemplate = jdbcTemplate;
        this.blockService = blockService;
    }

    @Override
    public void run(String... args) throws Exception {
        jdbcTemplate.execute("create table block_series (height bigint primary key, time bigint, speedrate double)");
        blockService.initLoadBlocks();
    }
}
