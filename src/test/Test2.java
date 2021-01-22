package test;

import dao.CarDao2;
import domain.Car;
import util.MySpring;

public class Test2 {

    public static void main(String[] args) {

        CarDao2 dao2 = MySpring.getBean("dao.CarDao2");
        Car car = new Car(null,"马自达12","red",40000);
//        dao2.save(car);
//        dao2.delete(2);
        dao2.findAll();
    }
}
