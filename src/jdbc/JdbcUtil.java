package jdbc;

import exception.SqlFormatException;
import jdbc.pool.ConnectionPool;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 对外使用的JDBC工具
 */
public class JdbcUtil {

    /**
     * 属性
     */
    private String driver;
    private String url;
    private String username;
    private String password;

    /**
     * 构造方法
     */
    public JdbcUtil(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 方法：写操作（增删改）
     */
    //插入
    //由于这里是对外提供使用的，因此使用了可变参数，可变参数的本质也是一个数组
    /**
     * 使用数组：                        对应     使用可变参数：
     * insert(sql,new Object[]{});      ==       insert(sql);
     * insert(sql,new Object[]{1});     ==       insert(sql,1);
     * insert(sql,new Object[]{1,2,3}); ==       insert(sql,1,2,3);
     * @param sql
     * @param param
     * @return
     */
    public int insert(String sql,Object... param){
        if(sql.trim().substring(0,6).equalsIgnoreCase("insert")){
            JdbcUpdateTemplate update = new JdbcUpdateTemplate(
                    driver,
                    url,
                    username,
                    password
            );
            update.setPool(pool);
            return (int) update.executeJdbc(sql,param);
        }else{
            throw new SqlFormatException("not is a insert sql {"+sql+"}");
        }
    }
    //删除
    public int delete(String sql,Object... param){
        if(sql.trim().substring(0,6).equalsIgnoreCase("delete")){
            JdbcUpdateTemplate update = new JdbcUpdateTemplate(
                    driver,
                    url,
                    username,
                    password
            );
            update.setPool(pool);
           return (int) update.executeJdbc(sql,param);
        }else{
            throw new SqlFormatException("not is a delete sql{"+sql+"}");
        }
    }
    //修改
    public int update(String sql,Object... param){
        if(sql.trim().substring(0,6).equalsIgnoreCase("update")){
            JdbcUpdateTemplate update = new JdbcUpdateTemplate(
                    driver,
                    url,
                    username,
                    password
            );
            update.setPool(pool);
            return (int) update.executeJdbc(sql,param);
        }else{
            throw new SqlFormatException("not is a update sql{"+sql+"}");
        }
    }

    /**
     * 方法：读操作（查询）
     */

    /**
     * 查询结果不能组成对象、
     * 查询所有记录：查询结果不能组成对象时，将查询结果放入map集合中
     *         如：多表关联时
     */
    public List<Map<String,Object>> selectListMap(String sql,Object... param){
        if(sql.trim().substring(0,6).equalsIgnoreCase("select")){
            JdbcQueryTemplate query = new JdbcQueryTemplate(
                    driver,
                    url,
                    username,
                    password
            );
            query.setPool(pool);
            List<Map<String,Object>> rs = (List<Map<String, Object>>) query.executeJdbc(sql,param);
            return rs;
        }else {
            throw new SqlFormatException("not is a update sql{"+sql+"}");
        }
    }

    /**
     * 查询结果不能组成对象
     *       查询单条记录：查询结果不能组成对象时，将查询结果放入map集合中
     */
    public Map<String,Object> selectMap(String sql,Object... param){
        List<Map<String,Object>> rows = selectListMap(sql,param);
        if(rows == null || rows.size() == 0){
            return null;
        }else{
            return rows.get(0);
        }
    }

    /**
     * 查询结果能组成对象
     *  策略模式实现orm：查询表中所有记录，将记录组成对象
     *  将查询出来的结果按照什么策略进行组装成java对象
     */
    public <T> List<T> selectList(String sql, RowMapper<T> strategy, Object... param){
        if(sql.trim().substring(0,6).equalsIgnoreCase("select")){
            JdbcQueryTemplate query = new JdbcQueryTemplate(
                    driver,
                    url,
                    username,
                    password
            );
            query.setPool(pool);
            List<Map<String,Object>> rs = (List<Map<String, Object>>) query.executeJdbc(sql,param);
            List<T> rows = new ArrayList<T>();
            for(Map<String,Object> r:rs){
                T row = strategy.mapping(r);
                rows.add(row);
            }
            return rows;
        }else{
            throw new SqlFormatException("not a select sql {"+sql+"}");
        }
    }

    /**
     * 查询结果能组成对象
     *策略模式实现orm：查询表中的一条记录，将该条记录组成对象
     */
    public <T> T selectOne(String sql, RowMapper<T> strategy, Object... param){
        List<T> rows = selectList(sql,strategy,param);
        if(rows == null || rows.size() == 0){
            return null;
        }else{
            return rows.get(0);
        }
    }

    /**
     * 反射实现orm
     *   eg:selectList("select * form t_car",Car.class),表示将结果组成Car类型的对象
     *      selectList("select count(*) from t_car",Integer.class),表示将结果组成Integer类型的对象
     *      selectList("select cname from t_car",String.class),表示将结果组成String类型的对象
     */

    public <T> List<T> selectList(String sql,Class<T> type,Object... param){
        if(sql.trim().substring(0,6).equalsIgnoreCase("select")){
            JdbcQueryTemplate query = new JdbcQueryTemplate(
                    driver,
                    url,
                    username,
                    password
            );
            query.setPool(pool);
            List<Map<String,Object>> rs = (List<Map<String, Object>>) query.executeJdbc(sql,param);
            List<T> rows = new ArrayList<>();
            try{
                //orm 将查询的表数据装载到java实体类中
                for(Map<String,Object> r : rs){
                    Object row = null;
                    if(type == int.class || type == Integer.class){
                        Collection cs = r.values();
                        for(Object c:cs){
                            //只做一次
                            row = ((Number)c).intValue(); //Integer row = (Integer)c;
                        }
                    }else if(type == long.class || type == Long.class){
                        Collection cs = r.values();
                        for(Object c:cs){
                            //只做一次
                            row = ((Number)c).longValue();
                        }
                    }else if(type == double.class || type == Double.class){
                        Collection cs = r.values();
                        for(Object c : cs){
                            //只做一次
                            row = ((Number)c).doubleValue();
                        }
                    }else if(type == String.class){
                        Collection cs = r.values();
                        for(Object c : cs){
                            row = (String)c;
                        }
                    }else{  //组成domain实体对象
                        Constructor con = type.getConstructor();
                        row = con.newInstance();
                        //通过反射获得实体中所有的属性名，map中找到与之同名的表数据，为其赋值。
                        // cno  == setCno
                        //从封装特性的角度而言，更推荐通过set方法，找到对应属性，通过set方法为属性赋值
                        Method[] ms = type.getMethods();
                        for(Method m : ms){
                            String mname = m.getName();
                            if(mname.startsWith("set")){
                                mname = mname.substring(3);  //去掉set
                                mname = mname.toLowerCase();
                                Object value= r.get(mname);
                                if(value == null){
                                    //当前对象属性没有对应的表数据
                                    continue; //继续判断下一个属性
                                }else{
                                    //当前属性有对应的表数据，使用set方法赋值
                                    //使用反射调用方法，并赋值。
                                    //获得方法的参数类型，获得所有的参数，
                                    // 但我们知道set方法中只有一个参数，因此只需要获得下标为0的参数就可以了
                                    Class p = m.getParameterTypes()[0];
                                    if(p == int.class || p == Integer.class){
                                        m.invoke(row,((Number)value).intValue());
                                    }else if(p == long.class || p == Long.class){
                                        m.invoke(row,((Number)value).longValue());
                                    }else if(p == double.class || p == Double.class){
                                        m.invoke(row,((Number)value).doubleValue());
                                    }else if(p == String.class){
                                        m.invoke(row,(String)value);
                                    }
                                }
                            }
                        }
                    }
                    rows.add((T) row);
                }
                return rows;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rows;
        }else{
            throw new SqlFormatException("not a select sql {"+sql+"}");
        }
    }

    /**
     *使用反射：查询单条记录
     */
    public <T> T selectOne(String sql, Class<T> type, Object... param){
        List<T> rows = selectList(sql,type,param);
        if(rows == null || rows.size() == 0){
            return null;
        }else{
            return rows.get(0);
        }
    }

    private ConnectionPool pool ;
    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }
}
