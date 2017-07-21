package com.btcgraph.dao;

import com.btcgraph.model.Block;

import java.util.List;

public interface BlockDao {
    Block findByHeight(long height);
    List<Block> findTop(int count);
    Block findLast();
    Block save(Block block);
    void saveAll(List<Block> blocks);
}
