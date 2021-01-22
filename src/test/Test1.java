package test;

import dao.CarDao;
import domain.Car;
import util.MySpring;

import java.util.*;

public class Test1 {

    public static void main(String[] args) {
//        Car car = new Car(null,"马自达","red",40000);
//        CarDao dao = MySpring.getBean("dao.CarDao");
////        int result = dao.insertOne(car);
//        int reslut = dao.insert1(car);
//        System.out.println(reslut);

//        CarDao dao = MySpring.getBean("dao.CarDao");
//        List<Map<String,Object>> list = dao.findAll();
//        for(int i=0; i<list.size(); i++){
//            Map<String,Object> map = list.get(i);
//            Set<String> keys = map.keySet();
//            Iterator<String> it = keys.iterator();
//            while(it.hasNext()){
//                String key = it.next();
//                Object value = map.get(key);
//                System.out.println(key+"--"+value);
//            }
//        }

        /**
         * 使用JdbcUtil来进行插入操作
         */
//        CarDao dao = MySpring.getBean("dao.CarDao");
//        Car car = new Car(null,"马自达1","red",40000);
//        int result = dao.save(car);
//        System.out.println(result);

        /**
         * 使用JdbcUtil来进行查询操作：查询所有记录
         */
//        CarDao dao = MySpring.getBean("dao.CarDao");
//        List<Map<String,Object>> rows = dao.findAllUtil();
//        for(int i=0; i<rows.size(); i++){
//            HashMap<String,Object> row = (HashMap<String, Object>) rows.get(i);
//            Set<String> set = row.keySet();
//            Iterator<String> it = set.iterator();
//            while(it.hasNext()){
//                String key = it.next();
//                Object value = row.get(key);
//                System.out.println(key+"--"+value);
//            }
//        }

        /**
         * 使用JdbcUtil来进行查询操作：查询单条记录
         */
//        CarDao dao = MySpring.getBean("dao.CarDao");
//        Map<String,Object> map = dao.findOneUtil("马自达1");
//        Set<String> set = map.keySet();
//        Iterator it = set.iterator();
//        while(it.hasNext()){
//            String key = (String) it.next();
//            Object value = map.get(key);
//            System.out.println(key +"--"+ value);
//        }

        /**
         * 使用JdbcUtil来进行查询操作：查询所有记录，使用策略模式
         */
//         CarDao dao = MySpring.getBean("dao.CarDao");
//         dao.findAll1();
//         dao.findAll2();

        /**
         * 使用JdbcFactory工厂类来生产JdbcUtil对象，向数据库中存储一条记录
         */
        Car car = new Car(null,"马自达10","red",40000);
        CarDao dao = MySpring.getBean("dao.CarDao");
//        dao.dbSave1(car);
//        dao.findAll3();
//        dao.save3(car);
        dao.findAll4();
    }
}
