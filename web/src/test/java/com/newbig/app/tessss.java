package com.newbig.app;

import org.assertj.core.util.Lists;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;


@Ignore
public class tessss {
    @Test
    public void test(){
        List<String> list1 = readTxtFile("/home/xiaofan/a.log");
        List<String> list2 = readTxtFile("/home/xiaofan/b.log");
        list2.removeAll(list1);
        for(String a:list2){
            System.out.println(a);
        }
    }


    public static List<String> readTxtFile(String filePath){
        try {
            String encoding="UTF8";
            List<String> a= Lists.newArrayList();
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    a.add(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
            return a;
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return null;
    }
    public static String convertPatrolPeriod(String period){
        String[] ps = period.split(",");
        StringBuilder result=new StringBuilder();
        for(String s:ps){
            switch (s){
                case "1": {
                    result.append("星期一,");
                    break;
                }
                case "2": {
                    result.append("星期二,");
                    break;
                }
                case "3": {
                    result.append("星期三,");
                    break;
                }
                case "4": {
                    result.append("星期四,");
                    break;
                }
                case "5": {
                    result.append("星期五,");
                    break;
                }
                case "6": {
                    result.append("星期六,");
                    break;
                }
                case "7": {
                    result.append("星期日,");
                    break;
                }
                default:
                    break;
            }
        }
        return result.toString();
    }

    public static void main(String[] args){
        System.out.println(convertPatrolPeriod(",2,3,4"));
    }
}
