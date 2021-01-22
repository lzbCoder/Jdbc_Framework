package jdbc;

import java.util.Map;

/**
 * 指定查询的每条记录组成对应的对象的策略规则（接口和抽象类就是规则）
 */
public interface RowMapper<T> {
    //将结果集对象中的一条记录组成对应的domain对象
    //切记不要循环结果集
    public T mapping(Map<String,Object> row);
}
