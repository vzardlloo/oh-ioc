package ioc;


import ioc.bean.BeanDefine;

import java.util.List;
import java.util.Set;

public interface Ioc {

    void addBean(Object bean);

    void addBean(String name,Object bean);

    <T> T addBean(Class<T> type);

    void setBean(Class<?> type,Object proxyBean);

    Object getBean(String name);

    <T> T getBean(Class<T> type);

    List<BeanDefine> getBeanDefines();

    BeanDefine getBeanDefine(Class<?> type);

    List<Object> getBeans();

    Set<String> getBeansNames();

    void remove(Class<?> type);

    void remove(String beanName);

    void clearAll();




}
