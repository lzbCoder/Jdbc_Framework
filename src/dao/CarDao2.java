package dao;

import domain.Car;
import jdbc.JdbcFactory;
import jdbc.SqlSession;
import util.MysqlFactoryUtil;

import java.util.List;

public class CarDao2 {

    public int save(Car car){
        String sql = "insert into t_car values(null,#{cname},#{color},#{price})";
        SqlSession sqlSession = MysqlFactoryUtil.getFactory().getSession();
        int count = sqlSession.insert(sql,car);
        return count;
    }

    public int delete(int cno){
        String sql = "delete from t_car where cno = #{cno} " ;
        SqlSession sqlSession = MysqlFactoryUtil.getFactory().getSession();
        int count = sqlSession.delete(sql,cno);
        return count;
    }

    public void findAll(){
        String sql = "select * from t_car" ;
        SqlSession sqlSession = MysqlFactoryUtil.getFactory().getSession();
        List<Car> list = sqlSession.selectList(sql,Car.class);
        for(Car car:list){
            System.out.println(car);
        }
    }
}
