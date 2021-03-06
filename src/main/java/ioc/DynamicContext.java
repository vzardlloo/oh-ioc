package ioc;


import ioc.bean.ClassInfo;
import ioc.bean.Scanner;
import ioc.reader.ClassPathClassReader;
import ioc.reader.JarReader;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
public final class DynamicContext {

    private static final ClassReader classpathReader  = new ClassPathClassReader();
    private static final ClassReader jarReader        = new JarReader();
    private static final String      SUFFIX_JAR       = ".jar";

    private static  boolean isJarContext = false;

    public static void init(Class<?> clazz){
        String rs = clazz.getResource("").toString();
        if (rs.contains(SUFFIX_JAR)){
            isJarContext = true;
        }
    }

    public static Stream<ClassInfo> recursionFindClasses(String packageName){
        Scanner             scanner     =  Scanner.builder().packageName(packageName).recursive(true).build();
        Set<ClassInfo>      classInfos  =  getClassReader(packageName).readClasses(scanner);
        return classInfos.stream();
    }

    public static ClassReader getClassReader(String packageName){
        if (isJarPackage(packageName)){
            return jarReader;
        }
        return classpathReader;
    }

    public static boolean isJarPackage(String packageName){
        if (packageName == null || "".equals(packageName.trim())){
            return false;
        }
        try {
            packageName = packageName.replace(".","/");
            Enumeration<URL> dirs = DynamicContext.class.getClassLoader().getResources(packageName);
            if(dirs.hasMoreElements()){
                String url = dirs.nextElement().toString();
                return url.indexOf(".jar!") != -1 || url.indexOf(".zip") != -1;
            }
        }catch (IOException e){

        }
        return false;
    }

    public static boolean isIsJarContext(){
        return isJarContext;
    }
}
