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
    private static final String ANNOTATION_START="/*";
    private static final String ANNOTATION_END="*/";
    private static final int ANNOTAION_SIZE=2;

    /**
     * ��ʵ��
     */
    private  FilterAnnotationUtil(){}

    private static class ClassInstance{
        private static final FilterAnnotationUtil FILTER_ANNOTATION_UTIL=new FilterAnnotationUtil();
    }

    public static   FilterAnnotationUtil Instanca(){
        return ClassInstance.FILTER_ANNOTATION_UTIL;
    }


    public static String filterContent(URL url){
        return filterALLStarAnnotation(filterAnnotationFromJSON(url));
    }
    /**
     * @param content �ı�
     * @param indexStack  ����ջ�ռ�
     * @param stringStack ��ǰ��־����
     * @param indexNow ���ڵ�����λ��
     * @param isBefore �Ƿ���ע��before����
     */
    private static int  addIndexFromContentAnnotation(String content,Stack<Integer> indexStack,Stack<String> stringStack,int indexNow,boolean isBefore){
        int afterIndex=0;
        if(isBefore){//�����ǰ��ǰ׺
            //�ӵ�ǰ������ʼ�ҵ���׺+2����/[*/]*������м��������Ӱ����
            afterIndex=content.indexOf(ANNOTATION_END,indexNow+ANNOTAION_SIZE);
            stringStack.add(ANNOTATION_END);
        }else{
            //����Ǻ�׺�Ļ�Ӧ��ȥ�ҵ���ǰ������ʼ�ĵ�һ��ǰ׺
            afterIndex=content.indexOf(ANNOTATION_START,indexNow+ANNOTAION_SIZE);
            stringStack.add(ANNOTATION_START);
        }
        if(afterIndex!=-1)indexStack.add(afterIndex);
        return afterIndex;
    }

    //TODO
    private static String filterALLStarAnnotation(String content) {
        //��ʱ�Ѿ���ȫ���˵������а���//������
        StringBuilder builder = new StringBuilder(content);
        Stack<Integer> indexStack = new Stack<Integer>();//���ע�Ϳ�ͷ��ע�ͽ�β��ջ�ռ�
        Stack<String>  stringStack=new Stack<String>();
        //��־��ǰ����
        int indexNow = content.indexOf(ANNOTATION_START);//�õ���һ����ͷ��ע��
        if(indexNow==-1) return content;
        stringStack.add(ANNOTATION_START);
        indexStack.add(indexNow);//������ջ

        while(indexNow<content.length()){
            boolean isBefore=false;
            if (stringStack.peek().equals(ANNOTATION_START)) isBefore=true;
            int isExists = addIndexFromContentAnnotation(content, indexStack, stringStack, indexNow, isBefore);
            indexNow=indexStack.peek();//�ı䵱ǰ����λ��
            if(isExists==-1)break;
        }
        /**
         * ��ʼ�����ַ���
         */
        while(indexStack.size()!=0){
            int end=indexStack.pop();
            int start=indexStack.pop();
            builder.replace(start,end+ANNOTAION_SIZE,"");
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
                    stringBuilder.append(line);
                    line = dataInputStream.readLine();
                }
            } catch (IOException e) {
            }
        }
        return stringBuilder.toString();

    }



}
