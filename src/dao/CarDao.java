package dao;

import domain.Car;
import jdbc.*;
import util.MySpring;
import util.MysqlFactoryUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class CarDao {

    /**
     * 原生jdbc流程
     *   插入一条记录
     */
    public int insertOne(Car car){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8";
        String username = "root";
        String password = "041911";
        String sql = "insert into t_car values(null,?,?,?)";
        int result = 0;
        try {
            //1 导包
            //2.加载驱动
            Class.forName(driver);
            //3.获取连接
            Connection conn = DriverManager.getConnection(url,username,password);
            //4.创建预处理状态参数
            PreparedStatement pstat = conn.prepareStatement(sql);
            //赋值
            pstat.setString(1,car.getCname());
            pstat.setString(2,car.getColor());
            pstat.setInt(3,car.getPrice());
            //5.执行
            result = pstat.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 使用JdbcUpdateTemplate类模板来实现插入操作
     */
    public int insert1(Car car){
        String sql = "insert into t_car values(null,?,?,?)";
        Object[] param = new Object[]{car.getCname(),car.getColor(),car.getPrice()};
        JdbcUpdateTemplate update = new JdbcUpdateTemplate(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        int obj = (int) update.executeJdbc(sql,param);
        return obj;
    }

    /**
     * 使用JdbcQueryTemplate类模板来实现查询操作
     * 查询所有记录
     */
    public List<Map<String,Object>> findAll(){
        String sql = "select * from t_car";
        JdbcQueryTemplate query = new JdbcQueryTemplate(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        //缺点：在执行查询操作中，没有?参数时，由于底层是一个Object数组，若写为null，
        //     则会报空指针异常，只能new一个空数组来传递new Object[]{}
        return (List<Map<String, Object>>) query.executeJdbc(sql,new Object[]{});
    }

    /**
     * 使用JdbcUtil类来实现插入操作
     */
    public int save(Car car){
        String sql = "insert into t_car values(null,?,?,?)";
        JdbcUtil util = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        int result = util.insert(sql,car.getCname(),car.getColor(),car.getPrice());
        return result;
    }

    /**
     * 使用JdbcUtil类模板来实现查询操作
     * 查询所有记录
     */
    public List<Map<String,Object>> findAllUtil(){
        String sql = "select * from t_car";
        JdbcUtil util = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        return util.selectListMap(sql);
    }

    /**
     * 使用JdbcUtil类模板来实现查询操作
     * 查询单条记录
     */
    public Map<String,Object> findOneUtil(String cname){
        String sql = "select * from t_car where cname = ?";
        JdbcUtil util = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        return util.selectMap(sql,cname);
    }

    /**
     * 使用JdbcUtil类
     *      查询表中所有记录（单表查询）使用策略模式
     */
    public void findAll1(){
        String sql = "select * from t_car";
        JdbcUtil util = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        List<Car> cars = util.selectList(sql,new CarMapper());
        for(Car car : cars){
            System.out.println(car);
        }
    }

    /**
     * 使用反射：查询表中所有记录
     */
    public void findAll2(){
        String sql = "select * from t_car";
        JdbcUtil util = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        List<Car> cars = util.selectList(sql,Car.class);
        for(Car car : cars){
            System.out.println(car);
        }
    }

    /**
     * 使用反射
     *    将查询结果组成整型
     */
    public void findCount(){
        String sql = "select count(*) from t_car" ;
        JdbcUtil util = new JdbcUtil(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/test_mysql?useSSL=false&characterEncoding=utf8",
                "root",
                "041911"
        );
        List<Integer> list = util.selectList(sql,Integer.class) ;
        System.out.println(list);
    }

    /**
     * 读取db.properties配置文件
     *      使用JdbcFactory工厂类来生产JdbcUtil对象，向数据库中存储一条记录
     */
    public void dbSave1(Car car){
        String sql = "insert into t_car(cname,color,price) values(?,?,?)";
        //创建一个JdbcFactory工厂对象
        JdbcFactory factory = new JdbcFactory(); //默认读取了db.properties
        //创建一个JdbcUtil类对象
        JdbcUtil util = factory.getUtil();
        util.insert(sql,car.getCname(),car.getColor(),car.getPrice());
    }


    /**
     * 读取db.properties配置文件
     *       使用JdbcFactory工厂类来生产JdbcUtil对象，查询数据库信息
     */
    public void findAll3(){
        String sql = "select * from t_car";
        //创建一个JdbcFactory工厂对象
        //由于每次和数据库交互，都需要创建一个工厂类，因此将工厂做成单实例
//        JdbcFactory factory = new JdbcFactory(); //默认读取了db.properties
        JdbcFactory factory = MysqlFactoryUtil.getFactory();
        //创建一个JdbcUtil类对象
        JdbcUtil util = factory.getUtil();
        List<Car> cars = util.selectList(sql,Car.class);
        for(Car car : cars){
            System.out.println(car);
        }
    }

    /**
     *  使用JdbcFactory工厂类来生产SqlSession对象，使用SqlSession对象来保存一条记录
     */
    public void save3(Car car){
        String sql = "insert into t_car(cname,color,price) values(#{cname},#{color},#{price})" ;
        JdbcFactory factory = new JdbcFactory();
        SqlSession sqlSession = factory.getSession();
        sqlSession.insert(sql,car);
    }

    /**
     *  使用JdbcFactory工厂类来生产SqlSession对象，使用SqlSession对象来查询一条记录
     */
    public void findAll4(){
        String sql = "select cno,Cname,Color,PRICE from t_car where cno = #{cno}" ;

        JdbcFactory factory = new JdbcFactory();
        SqlSession session = factory.getSession() ;
        List<Car> cars = session.selectList(sql,1,Car.class);

        for(Car car:cars){
            System.out.println(car);
        }
    }


    /**
     * 实现策略
     */
    class CarMapper implements RowMapper<Car>{
        @Override
        public Car mapping(Map<String, Object> row) {
            Integer cno = (Integer) row.get("cno");
            String cname = (String) row.get("cname");
            String color = (String) row.get("color");
            Integer price = (Integer) row.get("price");
            return new Car(cno,cname,color,price);
        }
    }
}
