package util;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class MySpring {

    private static HashMap<String,Object> beanMap = new HashMap<>();

    public static <T>T getBean(String className){
        T obj = (T) beanMap.get(className);
        if(obj == null){
            try {
                Class clazz = Class.forName(className);
                Constructor constructor = clazz.getConstructor();
                obj = (T)constructor.newInstance();
                beanMap.put(className,obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

}
