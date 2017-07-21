package com.btcgraph.service;

import com.btcgraph.model.Block;

import java.util.List;

public interface BlockService {
    Block findByHeight(long height);
    List<Block> findTop(int count);
    Block findLast();
    List<Block> loadNewBlocks();
    List<Block> initLoadBlocks();
}
