package com.btcgraph.service;

import com.btcgraph.dao.BlockDao;
import com.btcgraph.model.Block;
import com.btcgraph.dto.BlocksResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class BlockServiceImpl implements BlockService {

    Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);

    BlockDao blockDao;

    RestTemplate restTemplate;

    @Autowired
    public BlockServiceImpl(BlockDao blockDao, RestTemplate restTemplate) {
        this.blockDao = blockDao;
        this.restTemplate = restTemplate;
    }

    @Override
    public Block findByHeight(long height) {
        return blockDao.findByHeight(height);
    }

    @Override
    public List<Block> findTop(int count) {
        return blockDao.findTop(count);
    }

    @Override
    public Block findLast() {
        return blockDao.findLast();
    }

    @Override
    public List<Block> loadNewBlocks() {
        Block lastBlock = blockDao.findLast();
        long nextBlockHeight = lastBlock.getHeight() + 1;
        Block requestedNewestBlock = restTemplate.getForObject("https://blockchain.info/latestblock", Block.class);
        if (requestedNewestBlock.getHeight() < nextBlockHeight) {
            log.debug("no new blocks found. return empty list");
            return Collections.emptyList();
        }
        if (requestedNewestBlock.getHeight() == nextBlockHeight) {
            requestedNewestBlock.setSpeedRate(requestedNewestBlock.calculateSpeedRate(lastBlock));
            blockDao.save(requestedNewestBlock);
            log.debug("one new block found: {}", requestedNewestBlock);
            return Collections.singletonList(requestedNewestBlock);
        }
        //if more than one block was generated from previous call
        Block prev = lastBlock;
        List<Block> result = new ArrayList<>();
        while (nextBlockHeight <= requestedNewestBlock.getHeight()) {
            Block next = restTemplate.getForObject("https://blockchain.info/block-height/" + nextBlockHeight + "?format=json",
                    BlocksResponseWrapper.class).getBlocks().get(0);
            next.setSpeedRate(next.calculateSpeedRate(prev));
            blockDao.save(next);
            result.add(next);
            prev = next;
            nextBlockHeight++;
        }
        log.debug("new blocks found: {}", result);
        return result;
    }

    @Override
    public List<Block> initLoadBlocks() {
        Calendar cal = Calendar.getInstance();

        BlocksResponseWrapper blocksForToday = restTemplate.getForObject("https://blockchain.info/blocks/"
                + cal.getTimeInMillis()
                + "?format=json", BlocksResponseWrapper.class);

        List<Block> blocks = blocksForToday.getBlocks();
        Collections.reverse(blocks); //for some reason blockchain API returns first page in descending order and the rest in ascending

        if (blocksForToday.getBlocks().size() < 100) {
            cal.add(Calendar.DATE, -1);

            BlocksResponseWrapper blocksForUesterday = restTemplate.getForObject("https://blockchain.info/blocks/"
                    + cal.getTimeInMillis()
                    + "?format=json", BlocksResponseWrapper.class);

            blocksForUesterday.getBlocks().addAll(blocks);
            blocks = blocksForUesterday.getBlocks();
        }

        for (int i = 1; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            Block prev = blocks.get(i - 1);
            b.setSpeedRate(b.calculateSpeedRate(prev));
        }
        blocks.remove(0); //speedrate cannot be calculated for the first block because previous block time is needed
        log.debug("block list to be saved at initialization: {}", blocks);
        blockDao.saveAll(blocks);
        return blocks;
    }
}
