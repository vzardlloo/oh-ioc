package ioc.reader;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import ioc.ClassReader;
import ioc.bean.ClassInfo;
import ioc.bean.Scanner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarReader extends AbstractClassReader implements ClassReader {

    private static final String JAR_FILE    = "jar:file";
    private static final String WSJAR_FILE  = "wajar:file";


    @Override
    public Set<ClassInfo> getClassByAnnotation(String packageName, Class<?> parent, Class<? extends Annotation> annotation, boolean recursive) {
        Set<ClassInfo> classes = new HashSet<>();

        String packageDirName = packageName.replace('.','/');

        Enumeration<URL> dirs;
        try {
            dirs = this.getClass().getClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()){
                URL                 url         = dirs.nextElement();
                Set<ClassInfo>      subClass    = this.getClasses(url,packageDirName,packageName,parent,annotation,recursive,classes);
                if (subClass.size() > 0){
                    classes.addAll(subClass);
                }
            }
        }catch (IOException e){

        }
        return classes;
    }

    private Set<ClassInfo> getClasses(final URL url,final String packageDirName,String packageName,final Class<?> parent,
                                      final Class<? extends Annotation> annotation,final boolean recursive,Set<ClassInfo> classes){
        try {
            //Get jar file
            if (url.toString().startsWith(JAR_FILE) || url.toString().endsWith(WSJAR_FILE)){
                JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();

                Enumeration<JarEntry> eje = jarFile.entries();
                while (eje.hasMoreElements()){
                    JarEntry jarEntry = eje.nextElement();
                    String name = jarEntry.getName();

                    if (!name.startsWith(packageDirName)){
                        continue;
                    }
                    int idx = name.lastIndexOf("/");
                    if (idx != -1){
                        packageName = name.substring(0,idx).replace('.','/');
                    }
                    if (!name.endsWith(".class") || jarEntry.isDirectory()){
                        continue;
                    }

                    String className = name.substring(packageName.length() + 1,name.length() - 6);

                    Class<?> clazz = Class.forName(packageName +'.' + className);
                    if (null != parent && null != annotation){
                        if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent) && null != clazz.getAnnotation(annotation)){
                            classes.add(new ClassInfo(clazz));
                        }
                        continue;
                    }

                    if (null != parent) {
                        if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent)) {
                            classes.add(new ClassInfo(clazz));
                        }
                        continue;
                    }
                    if (null != annotation) {
                        if (null != clazz.getAnnotation(annotation)) {
                            classes.add(new ClassInfo(clazz));
                        }
                        continue;
                    }
                    classes.add(new ClassInfo(clazz));

                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return classes;
    }

}
