package ioc;


import ioc.bean.ClassInfo;
import ioc.bean.Scanner;

import java.util.Set;

public interface ClassReader {

    Set<ClassInfo> readClasses(Scanner scanner);

}
