package com.taobao.gps.plugins.filter.annotation;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

/**
 * ���������ļ������˵����е�ע�ͣ�����//��/**\���͵�ע��
 * Created by zhaodong.xzd zhaodong.xzd@alibaba-inc.com(liangzhi)
 * on 2015/7/29.11:47
 */
public class FilterAnnotationUtil {
    private static final String ANNOTATION_START = "/*";
    private static final String ANNOTATION_END = "*/";
    private static final int ANNOTAION_SIZE = 2;

    /**
     * ��ʵ��
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
     * @param url  �ļ�url
     * @param any  �Ƿ�����κ��ļ�,���ֻ�����gps�����ôany Ϊfalse
     * @return   json
     */
    public static String filterContent(URL url,boolean any) {
        return !any?filterALLStarAnnotation(filterAnnotationFromJSON(url)):filterAnnotationFinal(filterAnnotationFromAny(url));
    }

    /**
     * @param content     �ı�
     * @param indexStack  ����ջ�ռ�
     * @param stringStack ��ǰ��־����
     * @param indexNow    ���ڵ�����λ��
     * @param isBefore    �Ƿ���ע��before����
     */
    private static int addIndexFromContentAnnotation(String content, Stack<Integer> indexStack, Stack<String> stringStack, int indexNow, boolean isBefore) {
        int afterIndex = 0;
        if (isBefore) {//�����ǰ��ǰ׺
            //�ӵ�ǰ������ʼ�ҵ���׺+2����/[*/]*������м��������Ӱ����
            afterIndex = content.indexOf(ANNOTATION_END, indexNow + ANNOTAION_SIZE);
            stringStack.add(ANNOTATION_END);
        } else {
            //����Ǻ�׺�Ļ�Ӧ��ȥ�ҵ���ǰ������ʼ�ĵ�һ��ǰ׺
            afterIndex = content.indexOf(ANNOTATION_START, indexNow + ANNOTAION_SIZE);
            stringStack.add(ANNOTATION_START);
        }
        if (afterIndex != -1) indexStack.add(afterIndex);
        return afterIndex;
    }

    //TODO
    private static String filterALLStarAnnotation(String content) {
        //��ʱ�Ѿ���ȫ���˵������а���//������
        StringBuilder builder = new StringBuilder(content);
        Stack<Integer> indexStack = new Stack<Integer>();//���ע�Ϳ�ͷ��ע�ͽ�β��ջ�ռ�
        Stack<String> stringStack = new Stack<String>();
        //��־��ǰ����
        int indexNow = content.indexOf(ANNOTATION_START);//�õ���һ����ͷ��ע��
        if (indexNow == -1) return content;
        stringStack.add(ANNOTATION_START);
        indexStack.add(indexNow);//������ջ

        while (indexNow < content.length()) {
            boolean isBefore = false;
            if (stringStack.peek().equals(ANNOTATION_START)) isBefore = true;
            int isExists = addIndexFromContentAnnotation(content, indexStack, stringStack, indexNow, isBefore);
            indexNow = indexStack.peek();//�ı䵱ǰ����λ��
            if (isExists == -1) break;
        }
        /**
         * ��ʼ�����ַ���
         */
        while (indexStack.size() != 0) {
            int end = indexStack.pop();
            int start = indexStack.pop();
            builder.replace(start, end + ANNOTAION_SIZE, "");
        }
        return builder.toString();
    }

    private static String filterAnnotationFinal(String content) {
        //�Ƿ�ʼ����buffer,�Ƿ��Ѿ�������/**/,�Ƿ��Ѿ�����������
        boolean isWrite = true, isStar = false, isQuotation = false;
        StringBuilder builder = new StringBuilder();
        //ת��Ϊchar����
        char[] chars = content.toCharArray();
        int length = chars.length;
        loop:
        for (int index = 0; index < length; index++) {
            char now = chars[index];
            char nowAfter;
            if (isWrite) {
                if (now == '/' && !isQuotation) {//û�н��������ַ��������:"//qwe"
                    if (index + 1 < length) {
                        nowAfter = chars[index + 1];
                        if (nowAfter == '/') {
                            isWrite = false;//�������//����ע�ͣ�������д��buffer
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
                    isQuotation = !isQuotation;//�Ƿ���������л�
                }
                builder.append(now);
            } else if (isStar) {
                if (now == '*' && (index + 1 < length) && (nowAfter = chars[index + 1]) == '/') {
                    index++;
                    isStar = false;
                    isWrite = true;
                }
            } else {
                if (now == '\n') isWrite = true;//һֱ��������ĩβ,����д����Ϊtrue
            }
        }
        return builder.toString();
    }


    /**
     * ���˵�//ȥ�����������
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
                        line = line.substring(0, line.indexOf("//"));//����//ǰ�������
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
     * ͨ��url��ȡ�ļ�
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
                    stringBuilder.append(line + "\n");//������˼���\n
                    line = dataInputStream.readLine();
                }
            } catch (IOException e) {
            }
        }
        return stringBuilder.toString();

    }


}
