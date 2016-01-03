package org.zhaodong.filter;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

/**
 * 解析配置文件，过滤掉所有的注释，包括//和/**\类型的注释
 * Created by zhaodong.xzd zhaodong.xzd@alibaba-inc.com(liangzhi)
 * on 2015/7/29.11:47
 */
public class FilterAnnotationUtil {
    private static final String ANNOTATION_START = "/*";
    private static final String ANNOTATION_END = "*/";
    private static final int ANNOTAION_SIZE = 2;

    public static void main(String[] args) {
        System.out.println("xiexie");
    }
    /**
     * 单实例
     */
    private FilterAnnotationUtil() {
    }

    private static class ClassInstance {
        private static final FilterAnnotationUtil FILTER_ANNOTATION_UTIL = new FilterAnnotationUtil();
    }

    public static FilterAnnotationUtil Instance() {
        return ClassInstance.FILTER_ANNOTATION_UTIL;
    }

    /**
     *
     * @param url  文件url
     * @param any  是否针对任何文件,如果只是针对gps框架那么any 为false
     * @return   json
     */
    public static String filterContent(URL url,boolean any) {
        return !any?filterALLStarAnnotation(filterAnnotationFromJSON(url)):filterAnnotationFinal(filterAnnotationFromAny(url));
    }

    /**
     * @param content     文本
     * @param indexStack  索引栈空间
     * @param stringStack 当前标志符号
     * @param indexNow    现在的索引位置
     * @param isBefore    是否是注释before索引
     */
    private static int addIndexFromContentAnnotation(String content, Stack<Integer> indexStack, Stack<String> stringStack, int indexNow, boolean isBefore) {
        int afterIndex = 0;
        if (isBefore) {//如果当前是前缀
            //从当前索引开始找到后缀+2避免/[*/]*情况，中间两个组合影响结果
            afterIndex = content.indexOf(ANNOTATION_END, indexNow + ANNOTAION_SIZE);
            stringStack.add(ANNOTATION_END);
        } else {
            //如果是后缀的话应该去找到当前索引开始的第一个前缀
            afterIndex = content.indexOf(ANNOTATION_START, indexNow + ANNOTAION_SIZE);
            stringStack.add(ANNOTATION_START);
        }
        if (afterIndex != -1) indexStack.add(afterIndex);
        return afterIndex;
    }

    //TODO
    private static String filterALLStarAnnotation(String content) {
        //此时已经完全过滤掉了所有包含//的行数
        StringBuilder builder = new StringBuilder(content);
        Stack<Integer> indexStack = new Stack<Integer>();//存放注释开头和注释结尾的栈空间
        Stack<String> stringStack = new Stack<String>();
        //标志当前索引
        int indexNow = content.indexOf(ANNOTATION_START);//得到第一个开头的注释
        if (indexNow == -1) return content;
        stringStack.add(ANNOTATION_START);
        indexStack.add(indexNow);//索引入栈

        while (indexNow < content.length()) {
            boolean isBefore = false;
            if (stringStack.peek().equals(ANNOTATION_START)) isBefore = true;
            int isExists = addIndexFromContentAnnotation(content, indexStack, stringStack, indexNow, isBefore);
            indexNow = indexStack.peek();//改变当前索引位置
            if (isExists == -1) break;
        }
        /**
         * 开始处理字符串
         */
        while (indexStack.size() != 0) {
            int end = indexStack.pop();
            int start = indexStack.pop();
            builder.replace(start, end + ANNOTAION_SIZE, "");
        }
        return builder.toString();
    }

    private static String filterAnnotationFinal(String content) {
        //是否开始读入buffer,是否已经进入了/**/,是否已经进入了引用
        boolean isWrite = true, isStar = false, isQuotation = false;
        StringBuilder builder = new StringBuilder();
        //转换为char数组
        char[] chars = content.toCharArray();
        int length = chars.length;
        loop:
        for (int index = 0; index < length; index++) {
            char now = chars[index];
            char nowAfter;
            if (isWrite) {
                if (now == '/' && !isQuotation) {//没有进入引用字符串类比如:"//qwe"
                    if (index + 1 < length) {
                        nowAfter = chars[index + 1];
                        if (nowAfter == '/') {
                            isWrite = false;//如果是有//这种注释，将不会写入buffer
                            index++;
                            continue loop;
                        } else if (nowAfter == '*') {
                            isWrite = false;
                            isStar = true;
                            index++;
                            continue loop;
                        }
                    }
                } else if (now == '"' || now == '\'' || now == '`') {
                    isQuotation = !isQuotation;//是否进入引用切换
                }
                builder.append(now);
            } else if (isStar) {
                if (now == '*' && (index + 1 < length) && (nowAfter = chars[index + 1]) == '/') {
                    index++;
                    isStar = false;
                    isWrite = true;
                }
            } else {
                if (now == '\n') isWrite = true;//一直读到该行末尾,将可写入置为true
            }
        }
        return builder.toString();
    }


    /**
     * 过滤掉//去掉后面的所有
     *
     * @param url
     * @return
     */
    private static String filterAnnotationFromJSON(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        if (url != null) {
            try {
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                String line = dataInputStream.readLine();
                while (line != null) {
                    if (line.contains("//")) {
                        line = line.substring(0, line.indexOf("//"));//保留//前面的内容
                    }
                    stringBuilder.append(line + "\n");
                    line = dataInputStream.readLine();
                }
            } catch (IOException e) {
            }
        }
        return stringBuilder.toString();

    }


    /**
     * 通过url读取文件
     *
     * @param url
     * @return
     */
    private static String filterAnnotationFromAny(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        if (url != null) {
            try {
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                String line = dataInputStream.readLine();
                while (line != null) {
                    stringBuilder.append(line + "\n");//方便过滤加上\n
                    line = dataInputStream.readLine();
                }
            } catch (IOException e) {
            }
        }
        return stringBuilder.toString();

    }


}
