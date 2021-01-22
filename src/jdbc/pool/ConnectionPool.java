package jdbc.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    /**
     * 属性
     */
    private List<ConnectionProxy> connections;  //装载连接池中的所有的连接
    private ConnectionGenerator generator;
    private String driver;
    private String url;
    private String username;
    private String password;
    private Integer total = 100;//连接总数
    private Integer maxWait = 2000 ;//最大等待时间(毫秒)
    private Integer minIdle = 2 ;//最小空闲数，当连接池空闲连接数少于数量时，准备补充新连接

    /**
     * 构造函数
     */
    public ConnectionPool(String driver,String url,String username,String password) throws ClassNotFoundException, SQLException {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password =  password;

        //创建连接池时，就自动创建一组初始连接对象
        Class.forName(driver);
        connections = new ArrayList(10);
        for(int i=0; i<10; i++){
            Connection conn = DriverManager.getConnection(url,username,password);
            ConnectionProxy cp = new ConnectionProxy(conn);
            connections.add(cp);
        }

        //创建连接生成器，即创建一个线程
        this.generator = new ConnectionGenerator();
        //将该线程设置为精灵线程或守护线程。 线程分为：1 用户线程，2 守护线程
        //精灵线程特点：随着主线程关闭，精灵线程也随着关闭
        //用户线程特点：主线程关闭时，如果用户线程没有关闭，主线程需要等待用户线程，然后再关闭。默认为用户线程
        generator.setDaemon(true);
        //开启线程
        generator.start();
    }

    /**
     *获取连接
     */
    public Connection getConnection() throws InterruptedException {
       int wait_time = 0 ;
       wait:while (true) {
           //找到空闲连接，改变使用状态，返回连接对象
           find:for (ConnectionProxy cp : connections) {
               if (!cp.useFlag) {  // cp.useFlag == false
                   synchronized (cp) {
                       if(!cp.useFlag) {
                           //当前连接是空闲
                           cp.useFlag = true;
                           synchronized ("dmc"){
                               "dmc".notify(); //唤醒线程
                           }
                           return cp;
                       }else{
                           continue wait;
                       }
                   }
               }
           }
           //没有空闲连接,等一会
           wait_time += 100;
           Thread.sleep(100);
           if(wait_time == 2000){
               //不再继续了
               throw new ConnectionPoolException("connect time out ") ;
           }else{
               continue wait;
           }
       }
    }


    /**
     * 内部类：连接生成器，连接不足时造连接。继承Thread，相当于一个线程类。
     *  检测连接与创建连接是同时进行的，因此使用多线程，实现多线程。
     */
    private class ConnectionGenerator extends Thread{
        @Override
        public void run() {
            //当程序运行时就不停的检测，即检测连接与创建连接是同时进行的，因此使用多线程
            try {
                checkAndCreate();
            } catch (SQLException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        //方法：检测连接，发现不足创建补充连接
        public void checkAndCreate() throws SQLException, InterruptedException {
            while (true){
                int count = 0;  //记录剩余空闲连接的数量
                for(ConnectionProxy cp : connections){ //内部类特点：可以使用外部类的私有成员
                    if(!cp.useFlag){
                        //当前连接是一个空闲连接
                        count++ ;
                    }
                }
                if(count <= minIdle){
                    //连接不充足了，补充，默认每次补充10个，暂定上限100
                    int add_count = 10;
                    if(connections.size() + 10 > total){
                        //此次补充过后，超上限了，那么就不按照默认10个补充，
                        //而是按照超过的上限数量进行补充
                        add_count = total-connections.size() ;
                    }
                    //创建连接
                    for(int i=0;i<add_count;i++){
                        Connection conn = DriverManager.getConnection(url,username,password) ;
                        ConnectionProxy ncp = new ConnectionProxy(conn) ;
                        connections.add(ncp);
                    }
                }
                //每次检测完后，让该线程等待一会，不能一直进行上面创建连接的循环
                synchronized ("dmc"){
                    "dmc".wait();
                }
            }
        }
    }

    //-----------------------------------------------------


    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

}



