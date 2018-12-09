package com.core.search.stop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;

public class StopWordSet extends HashSet<String>{

    private static StopWordSet stopSet = new StopWordSet();

    public static StopWordSet getInstance() {
        return stopSet;
    }

    // 获取词典文件所在路径
    public static String getDictPath() {
        String dir = System.getProperty("dic.dir");
        if (StringUtils.isBlank(dir)) {
            dir = "D:/dict/";
        }

        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

    // 加载停用词词典
    private StopWordSet() {
        super(1000);
        String line;
        String dict = "stopword.txt";
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        File file = FileUtils.getFile(getDictPath() + dict);
        try {
            is = new FileInputStream(file);
            isr = new InputStreamReader(is, Charset.defaultCharset());
            reader = new BufferedReader(isr);
            while (StringUtils.isNoneBlank(line = reader.readLine())) {
                this.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 判断是不是停用词
    public static boolean isStopWord(String word) {
        StopWordSet stopWordSet = StopWordSet.getInstance();
        return stopWordSet.contains(word);
    }
}
