package org.shenjitang.common.jdbcTemplate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcTemplateUtils {

    /**
     * 获取数据
     *
     * @param sql sql
     * @return list
     */
    public static List<Map<String, Object>> find(JdbcTemplate jdbcTemplate, String sql) throws Exception {
        return jdbcTemplate.query(sql, new RowMapper() {
            @Override
            public Object mapRow(ResultSet resultSet, int j) throws SQLException {
                Map<String, String> hm = new HashMap<>();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int count = metaData.getColumnCount();
                for (int i = 1; i <= count; i++) {
                    String key = metaData.getColumnLabel(i);
                    String value = null;
                    try {
                        value = resultSet.getString(i);
                    } catch (Exception e) {
                    }
                    hm.put(key, value);
                }
                return hm;
            }
        });
    }

    public static void insert(JdbcTemplate jdbcTemplate, String sql) throws Exception {
        jdbcTemplate.execute(sql);
    }
}
