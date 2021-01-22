package jdbc;

import jdbc.pool.ConnectionPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 负责创建JdbcUtil工具
 */
public class JdbcFactory {

    /**
     * 属性
     */
    private String driver;
    private String url;
    private String username;
    private String password;

    private Integer total ;
    private Integer maxWait ;
    private Integer minIdel ;

    /**
     * 构造方法：无参数的构造方法
     */
    public JdbcFactory() {
        //默认调用带fileName参数的构造方法
        this("db.properties");
    }

    /**
     * 构造方法：带fileName参数
     * 人为规定的读取的文件都放置在src下
     * new JdbcFactory("db.properties");
     * @param fileName
     */
    public JdbcFactory(String fileName){
        //简单理解为获得src目录下的文件路径，其实获得的是classpath目录下的文件
        String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getPath();
        File file = new File(path);
        readFile(file);
    }

    /**
     * 构造方法：带file参数
     * 使用者根据程序，自己获得配置文件。交给工厂读取
     * File file = new File("d:/z/db.propeties");
     * new JdbcFactory(file);
     * @param file
     */
    public JdbcFactory(File file){
        readFile(file);
    }

    /**
     * 读取配置文件的方法
     */
    private void readFile(File file){
        try {
            InputStream in = new FileInputStream(file);
            Properties pro = new Properties();
            pro.load(in);
            driver = pro.getProperty("driver");
            url = pro.getProperty("url");
            username = pro.getProperty("username");
            password = pro.getProperty("password");

            String total=pro.getProperty("total") ;
            String maxWait=pro.getProperty("maxWait") ;
            String minIdel=pro.getProperty("minIdel") ;

            if(total != null && !"".equalsIgnoreCase(total)){
                this.total = Integer.parseInt(total);
            }
            if(maxWait != null && !"".equalsIgnoreCase(maxWait)){
                this.maxWait = Integer.parseInt(maxWait);
            }
            if(minIdel != null && !"".equalsIgnoreCase(minIdel)){
                this.minIdel = Integer.parseInt(minIdel);
            }

            //创建连接池
            this.createConnectionPool();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建JdbcUtil对象
     */
    public JdbcUtil getUtil(){
        JdbcUtil util =  new JdbcUtil(driver,url,username,password);
        util.setPool(pool);
        return util ;
    }

    /**
     * 创建SqlSession对象
     */
    public SqlSession getSession(){
        SqlSession session =  new SqlSession(driver,url,username,password,pool) ;
        return session ;
    }

    /**
     * 创建连接池
     */
    private ConnectionPool pool;
    private void createConnectionPool() throws SQLException, ClassNotFoundException {
        pool = new ConnectionPool(driver,url,username,password);
        pool.setTotal(total);
        pool.setMaxWait(maxWait);
        pool.setMinIdle(minIdel);
    }

}
