package com.btcgraph.dto;

import com.btcgraph.model.Block;

import java.util.List;

public class BlocksResponseWrapper {
    List<Block> blocks;

    public BlocksResponseWrapper(List<Block> blocks) {
        this.blocks = blocks;
    }

    public BlocksResponseWrapper() {
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
}
