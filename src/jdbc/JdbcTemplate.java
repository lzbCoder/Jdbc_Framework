package jdbc;

import jdbc.pool.ConnectionPool;

import java.sql.*;

public abstract class JdbcTemplate {

    /**
     * 属性
     */
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/test_mysql?characterEncoding=utf8&useSSL=false";
    private String username = "root";
    private String password = "041911";

    private Connection conn = null;
    protected PreparedStatement pstat = null;
    protected ResultSet rs = null;

    /**
     * 构造方法：在创建对象的同时，将这些属性赋值
     */
    public JdbcTemplate(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 公有方法(提供给外界使用)
     */
    public Object executeJdbc(String sql,Object[] param){
        try {
            //1 导包
            one();
            //2 加载驱动
            two();
            //3 获取连接
            three();
            //4 创建状态参数并给sql中的?赋值
            four(sql,param);
            //5 执行sql语句
            Object result = five();
            //若执行正确，则返回result
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                //6 关闭
                six();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //若执行出错，则返回null
        return null;
    }

    /**
     * 私有方法(仅内部使用)：对jdbc流程进行封装
     */
    //1 导包
    private void one(){}
    //2 加载驱动
    private void two() throws ClassNotFoundException {
//        Class.forName(driver);
    }
    //3 获取连接
    private void three() throws SQLException, InterruptedException {
//        conn = DriverManager.getConnection(url,username,password);
        conn = pool.getConnection();
    }
    //4 创建预处理状态参数,并给sql中的?赋值
    //这是使用一个Object数组，而没有用可变参数，是因为这个模板类是底层的东西，并不直接
    //提供给用户使用，等封装表层的功能时，就会用到可变参数，提供给用户使用
    //优点：这里定义一个具体数据类型的数组，要比定义为可变参数，jvm虚拟机执行效率更高
    //因为定义为可变参数，如果要传4个参数，那么可变参数会将这四个参数组成数组，然后再执行，
    //而在底层直接定义为数组，传递4个参数，就不需要再组装，执行运行。
    private void four(String sql,Object[] param) throws SQLException {
        pstat = conn.prepareStatement(sql);
        for(int i=0; i<param.length; i++){
            pstat.setObject(i+1,param[i]);
        }
    }
    //5 执行sql语句，并返回结果
    //注意：这里不能设置为私有的，private与abstract两个不能在一起使用
    //设置该方法为protected，表示该方法只能被其子类访问
    protected abstract Object five() throws SQLException;
    //6 关闭
    //注意：这里需要先判断是否为空，才能将其关闭，因为如果上面某个步骤中没有创建里面的对象，
    //     那么空对象执行close方法会报空指针异常。
    private void six() throws SQLException {
        if(rs != null){
            rs.close();
        }
        if(pstat != null){
            pstat.close();
        }
        if(conn != null){
            conn.close();
        }
    }

    private ConnectionPool pool ;
    public void setPool(ConnectionPool pool) {
        this.pool = pool;
    }
}
