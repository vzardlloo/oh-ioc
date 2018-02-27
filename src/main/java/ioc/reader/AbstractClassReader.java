package ioc.reader;


import ioc.ClassReader;
import ioc.bean.ClassInfo;
import ioc.bean.Scanner;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class AbstractClassReader implements ClassReader {


    public Set<ClassInfo> readClasses(Scanner scanner) {
        return null;
    }

    private Set<ClassInfo> findClassByPackage(final String packageName, final String packagePath,
                                              final Class<?> parent, final Class<? extends Annotation> annotation,
                                              final boolean recursive,Set<ClassInfo> classes) throws ClassNotFoundException{

        File dir = new File(packagePath);

        if ((!dir.exists()) || (!dir.isDirectory())){
            System.out.println("The package "+ packageName+" not found ");
        }

        File[] dirFiles = accept(dir,recursive);
        //Loop all files
        if (null != dirFiles && dirFiles.length > 0){
            for (File file : dirFiles){
                if (file.isDirectory()){
                    findClassByPackage(packageName+'.'+file.getName(), file.getAbsolutePath(), parent, annotation, recursive, classes);
                }else {

                    String className    = file.getName().substring(0,file.getName().length() - 6);
                    Class<?> clazz      = Class.forName(packageName+'.'+className);
                    if (null != parent && null != annotation){
                        if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent) && null != clazz.getAnnotation(annotation)){
                            classes.add(new ClassInfo(clazz));
                        }
                        continue;
                    }
                    if (null != parent){
                        if (null != clazz.getSuperclass() && clazz.getSuperclass().equals(parent)){
                            classes.add(new ClassInfo(clazz));
                        }else {
                            if (null != clazz.getInterfaces() && clazz.getInterfaces().length > 0 && clazz.getInterfaces()[0].equals(parent)){
                                classes.add(new ClassInfo(clazz));
                            }
                        }
                        continue;
                    }

                    if (null != annotation){
                        if (clazz.getAnnotation(annotation) != null){
                            classes.add(new ClassInfo(clazz));
                        }
                        continue;
                    }
                    classes.add(new ClassInfo(clazz));
                }
            }
        }

        return classes;
    }

    private File[] accept(File file,final boolean recursive){
        // Custom filtering rules If you can loop (include subdirectories) or is the end of the file. Class (compiled java class file)
        return file.listFiles(f -> (recursive && f.isDirectory()) || (f.getName().endsWith(".class")));
    }

    /**
     * 根据注解获取类的集合
     * @param packageName 类的名字
     * @param parent
     * @param annotation
     * @param recursive
     * @return
     */
    public Set<ClassInfo> getClassByAnnotation(String packageName,Class<?> parent,Class<? extends Annotation> annotation,boolean recursive){
        Set<ClassInfo> classes = new HashSet<>();
        // Get the name of the package and replace it
        String packageDirName = packageName.replace(".","/");
        // Defines an enumerated collection and loops to process the URL in this directory
        Enumeration<URL> dirs;
        try {
            dirs = this.getClass().getClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()){
                URL             url         = dirs.nextElement();
                String          filePath    = new URI(url.getFile()).getPath();
                Set<ClassInfo>  subClasses  = findClassByPackage(packageName,filePath,parent,annotation,recursive,classes);
                if (subClasses !=null && !subClasses.isEmpty()){
                    classes.addAll(subClasses);
                }
            }

        }catch (IOException | URISyntaxException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return classes;
    }

}
