package exception;

/**
 * 分析：这里继承RuntimeException还是Exception
 *      1. 如果可能产生的异常非常重要，那么就继承Exception，提前做处理(编译时异常)
 *      2. 如果这个异常经常会出现，那么就继承Exception
 */
public class SqlFormatException extends RuntimeException{

    public SqlFormatException(){}

    public SqlFormatException(String message){
        super(message);
    }
}
