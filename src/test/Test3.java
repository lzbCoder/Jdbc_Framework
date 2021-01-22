package test;

import dao.CarDao3;
import domain.Car;
import util.MysqlFactoryUtil;

import java.util.List;

public class Test3 {

    public static void main(String[] args) {
        CarDao3 dao3 = MysqlFactoryUtil.getFactory().getSession().createDaoImpl(CarDao3.class);
        Car car = new Car(null,"马自达20","red",40000);
//        dao3.save(car);
//        dao3.delete(27);
        for(int i=1; i<=20; i++) {
            List<Car> lists = dao3.findAll();
            for (Car list : lists) {
                System.out.println(list);
            }
            System.out.println(i+"-----------------------------------------");
        }
    }
}
