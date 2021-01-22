package jdbc.pool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接对象的代理
 */
public class ConnectionProxy extends AbstractConnection{

    boolean closeFlag = false;  //false表示不关闭，即释放；true表示关闭
    boolean useFlag = false;    //false表示空闲；true表示被使用

    public ConnectionProxy(Connection conn){
        super.conn = conn;
    }

    @Override
    public void close() throws SQLException {
        if(closeFlag == true){   //表示关闭连接  closeFlag==true 与 true相等。因此可以直接写成if(true)
            conn.close();
        }else{  //表示释放连接，释放连接即表示空闲
            useFlag = false;
        }
    }


}
