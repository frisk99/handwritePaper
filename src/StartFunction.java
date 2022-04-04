//import WorkWrite
import java.awt.*;
import java.util.Scanner;
public class StartFunction {

    public static void main(String[] args) {
        System.out.print("请输入你的作业题目：");
        Scanner scanner = new Scanner(System.in);
        String fileName=scanner.next();
        System.out.println("开始生成笔记...");
        //启动
        WorkWrite workWrite = new WorkWrite();
        String backgroundImgPath="D:\\JavaProject\\bg.png"; //纸张图片地址
        String bijiPath="D:\\JavaProject\\test.txt";//你的笔记文本的地址
        String fontPath="D:\\JavaProject\\日记.ttf";//你的自定义字体文件的地址
        Font newFont=workWrite.newFont(fontPath,20);//自定义字体和大小，未设置大小则默认90
        String[] txt=workWrite.MarkContent(bijiPath);//笔记文本
        workWrite.setRowlen(30);//设置行间距，未设置默认为100
        Color color= new Color(Color.black.getRGB());//文字为黑色
        workWrite.addWaterMark(backgroundImgPath, fileName,txt,color,newFont);//生成
        System.out.println("完成");
    }
}
