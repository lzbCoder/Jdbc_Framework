package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JdbcQueryTemplate extends JdbcTemplate{
    /**
     * 构造方法：在创建对象的同时，将这些属性赋值
     */
    public JdbcQueryTemplate(String driver, String url, String username, String password) {
        super(driver, url, username, password);
    }

    @Override
    protected Object five() throws SQLException {
        ResultSet rs = pstat.executeQuery();
        //创建一个List<Map<String,Object>>集合，用来存储查询的结果
        List<HashMap<String,Object>> rows = new ArrayList<>();
        while(rs.next()){
            //将每一行记录存入一个map集合中
            HashMap<String,Object> row = new HashMap<>();
            //遍历循环，循环次数是查询出来的字段数rs.getMetaData().getColumnCount()
            for(int i=1; i<=rs.getMetaData().getColumnCount(); i++){
                String name = rs.getMetaData().getColumnName(i);
                Object value = rs.getObject(i);
                row.put(name,value);
            }
            rows.add(row);
        }
        return rows;
    }
}
