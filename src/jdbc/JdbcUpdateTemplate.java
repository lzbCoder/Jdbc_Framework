package jdbc;

import java.sql.SQLException;

public class JdbcUpdateTemplate extends JdbcTemplate{

    /**
     * 构造方法：在创建对象的同时，将这些属性赋值
     */
    public JdbcUpdateTemplate(String driver, String url, String username, String password) {
        super(driver, url, username, password);
    }

    @Override
    protected Object five() throws SQLException {
        return pstat.executeUpdate();
    }
}
