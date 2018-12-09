package com.core.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CompileClassLoader extends ClassLoader {
    private byte[] getBytes(String filename) {
        File file = new File(filename);
        long len  = file.length();
        byte[] raw = new byte[(int)len];

        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            int r = in.read(raw);
            if (r != len) {
                throw new IOException("不能读取全部文件");
            }
            return raw;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private boolean compile(String file) throws IOException {
        Process p = Runtime.getRuntime().exec("javac "+file);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int ret = p.exitValue();
        return ret == 0;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class clazz = null;
        // 将包路径的点换成/
        String fileStub = name.replace(".","/");
        String srcFilepath = fileStub + ".java";
        String classFilepath = fileStub + ".class";
        File srcFile = new File(srcFilepath);
        File classFile = new File(classFilepath);
        // 当指定Java源文件存在，且class文件不存在或者Java源文件的修改时间比class文件修改时间晚的时候，重新编译
        if (srcFile.exists() && (!classFile.exists() || srcFile.lastModified() > classFile.lastModified())) {
            try {
                if (!compile(srcFilepath) || !classFile.exists()) {
                    throw new ClassNotFoundException("ClassNotFoundException: "+srcFilepath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (classFile.exists()) {
            byte[] raw = getBytes(classFilepath);
            clazz = defineClass(name,raw,0,raw.length);
        }

        if (clazz == null) {
            throw new ClassNotFoundException(name);
        }
        return clazz;
    }
}
