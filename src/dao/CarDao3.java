package dao;

import domain.Car;
import jdbc.annocations.Delete;
import jdbc.annocations.Insert;
import jdbc.annocations.Select;

import java.util.List;

public interface CarDao3 {

    @Insert("insert into t_car values(null,#{cname},#{color},#{price})")
    public int save(Car car) ;

    @Delete("delete from t_car where cno = #{cno}")
    public int delete(int cno);

    @Select("select * from t_car")
    public List<Car> findAll();

    @Select("select * from t_car where cno = #{cno}")
    public Car findById(int cno) ;

}
