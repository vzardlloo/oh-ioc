package ioc;


import ioc.bean.BeanDefine;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SimpleIoc implements Ioc {

    private final java.util.Map<String,BeanDefine> pool = new HashMap<>(32);



    @Override
    public void addBean(Object bean) {

    }

    @Override
    public void addBean(String name, Object bean) {
        BeanDefine beanDefine = new BeanDefine(bean);
        addBean(name, bean);

        Class<?>[] interfaces = beanDefine.getType().getInterfaces();
        if (interfaces.length > 0){
            for (Class<?> interfaceClazz : interfaces){
                addBean(interfaceClazz.getName(),interfaceClazz);
            }
        }
    }

    @Override
    public <T> T addBean(Class<T> type) {
        return null;
    }

    @Override
    public void setBean(Class<?> type, Object proxyBean) {

    }

    @Override
    public Object getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> type) {
        return null;
    }

    @Override
    public List<BeanDefine> getBeanDefines() {
        return null;
    }

    @Override
    public BeanDefine getBeanDefine(Class<?> type) {
        return null;
    }

    @Override
    public List<Object> getBeans() {
        return null;
    }

    @Override
    public Set<String> getBeansNames() {
        return null;
    }

    @Override
    public void remove(Class<?> type) {

    }

    @Override
    public void remove(String beanName) {

    }

    @Override
    public void clearAll() {

    }
}
