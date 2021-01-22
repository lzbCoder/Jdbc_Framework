package util;

import jdbc.JdbcFactory;

/**
 * 工厂的单实例管理
 */
public class MysqlFactoryUtil {

    private static JdbcFactory factory;

    static {
        factory = new JdbcFactory("mysql.properties");
    }

    public static JdbcFactory getFactory(){
        return factory;
    }
}
