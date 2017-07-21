package com.btcgraph.dao.jdbc;

import com.btcgraph.dao.BlockDao;
import com.btcgraph.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BlockDaoJDBC implements BlockDao {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public BlockDaoJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String FIND_BY_HEIGHT_QUERY = "select height, time, speedrate from block_series where heigth = ?";
    private static final String FIND_TOP_QUERY = "select top ? height, time, speedrate from block_series order by height desc";
    private static final String FIND_LAST_QUERY = "select height, time, speedrate from block_series " +
            "where height = (select max(height) from block_series)";
    private static final String INSERT_QUERY = "insert into block_series(height, time, speedrate) values(?, ?, ?)";

    RowMapper<Block> blockRowMapper = (rs, rowNum) -> {
        Block block = new Block();
        block.setHeight(rs.getLong("height"));
        block.setTime(rs.getLong("time"));
        block.setSpeedRate(rs.getDouble("speedrate"));
        return block;
    };

    @Override
    public Block findByHeight(long height) {
        return jdbcTemplate.queryForObject(FIND_BY_HEIGHT_QUERY, blockRowMapper, height);
    }

    @Override
    public List<Block> findTop(int count) {
        return jdbcTemplate.query(FIND_TOP_QUERY, new Object[]{count}, blockRowMapper);
    }

    @Override
    public Block findLast() {
        return jdbcTemplate.queryForObject(FIND_LAST_QUERY, blockRowMapper);
    }

    @Override
    public Block save(Block block) {
        jdbcTemplate.update(INSERT_QUERY, block.getHeight(), block.getTime(), block.getSpeedRate());
        return block;
    }

    @Override
    public void saveAll(List<Block> blocks) {
        jdbcTemplate.batchUpdate(INSERT_QUERY,
                blocks.stream().map(
                        block -> new Object[]{block.getHeight(), block.getTime(), block.getSpeedRate()}
                ).collect(Collectors.toList())
        );
    }
}
