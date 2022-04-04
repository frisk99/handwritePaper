import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
public class WorkWrite {

    private float fontSize=20;//字体大小
    private int rowlen=15;//行间距
    private int page=0;//页码
    /**
     * 绘制水印
     * @param split
     * @param markContentColor
     * @param font
     */
    public void addWaterMark(String backgroundImgPath, String resultImgPath, String[] split,Color markContentColor,Font font) {

        try {
            // 读取原图片信息
            File srcImgFile = new File(backgroundImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g;
            g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(markContentColor); //设置水印颜色
            g.setFont(font);              //设置字体

            //内容起始坐标
            int x = 30;
            int y = 100;
            int maxWord= (int) (srcImgWidth/fontSize);//每行最多的字数
            for (int i=0;i<split.length;i++) {
                //字符串可能会出现��，导致出现一些空白，所以为了保险起见将这些字符替换成空字符串
                split[i]=split[i].replace("��","");
                //获取字符串的字数
                int collen=split[i].length();
                /*
                对每行的字符数进行规范
                 */

                if(collen>maxWord){//如果大于每行规定的字数
                    String substring = split[i].substring(0, maxWord);
                    g.drawString(substring, x, y);  //绘制从0截取到最大字数的字符串
                    y += rowlen;//换行
                    String more=split[i].substring(maxWord,collen);//截取多出来的字符串
                    g.drawString(more, x, y);//绘制多出来的字符串
                    y += rowlen;//换行
                }else {
                    //直接绘制
                    g.drawString(split[i], x, y);
                    y += rowlen;
                }
                //内容超出页面高度则分页
                if(y>=srcImgHeight-1.5*rowlen) {
                    g.dispose();
                    //导出已经写满的笔记
                    productImage(bufImg,resultImgPath,++page);
                    y=rowlen;
                    g = bufImg.createGraphics();
                    g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
                    g.setColor(markContentColor); //设置水印颜色
                    g.setFont(font);              //设置字体
                }
            }
            //将最后一张笔记生成
            g.dispose();
            productImage(bufImg,resultImgPath,++page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生产笔记图片
     * @param image
     * @param tarImgPath
     * @param page
     */
    public void productImage(BufferedImage image,String tarImgPath,int page){
        try {
            tarImgPath=tarImgPath+page+".jpg";
            FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
            ImageIO.write(image, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 获取笔记所有内容，存入字符串数组
     * @param path path
     * @return String[]
     */
    public String[] MarkContent(String path){
        StringBuilder builder = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            byte[] bytes = new byte[1024];
            int temp;
            while ((temp=inputStream.read(bytes))!=-1){
                builder.append(new String(bytes,0,temp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = 0;
        for (int i =0 ; i< builder.length();i++){
            count +=1;
            if( builder.charAt(i) == '\n'){
                builder.replace(i,i+1, "");
            }
            if(count ==30){
                count =0;
                builder.insert(i,'\n');
            }

        }
        return builder.toString().split("\n");
    }

    /**
     * 采用自定义手写字体，看起来更真实
     * @param path path
     * @param fontSize fontSize
     * @return Font
     */
    public Font newFont(String path,float fontSize){
        Font font = null;                     //水印字体
        try {
            font = Font.createFont(Font.TRUETYPE_FONT,new File(path));
            font=font.deriveFont(fontSize);
            setFontSize(fontSize);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font;
    }
    public void setFontSize(float size){
        this.fontSize=size;
    }
    public void setRowlen(int rowlen){
        this.rowlen=rowlen;
    }
}
