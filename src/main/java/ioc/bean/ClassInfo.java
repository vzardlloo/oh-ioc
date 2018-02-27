package ioc.bean;


import lombok.Data;

@Data
public class ClassInfo {

    private String className;

    private Class<?> clazz;

    public ClassInfo(String className){
        this.className = className;
    }

    public ClassInfo(Class<?> clazz){
        this.className = clazz.getName();
        this.clazz = clazz;
    }

    public ClassInfo(Class<?> clazz,String className){
        this.clazz = clazz;
        this.className = className;
    }

    public String getClassName(){
        return className;
    }

    public Class<?> getClazz(){
        return clazz;
    }

    public Object newInstance(){
        try {
            return clazz.newInstance();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        return clazz.toString();
    }







}
