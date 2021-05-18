package com.montnets.emp.rms.tools;


import com.montnets.emp.common.context.EmpExecutionContext;
import org.im4java.core.CompositeCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

	/**
	 * 按九宫格位置添加水印
	 * 
	 * @param srcPath
	 *            原图片路径
	 * @param distPath
	 *            新图片路径
	 * @param watermarkImg
	 *            水印图片路径
	 * @param position
	 *            九宫格位置[1-9],从上往下,从左到右排序
	 * @param x
	 *            横向边距
	 * @param y
	 *            纵向边距
	 * @param alpha
	 *            透明度
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public void WatermarkImgHandler(String srcPath, String distPath, String watermarkImg, int position, int x, int y, int alpha) throws IOException, InterruptedException, IM4JavaException {
		int[] watermarkImgSide = getImageSide(watermarkImg);
		int[] srcImgSide = getImageSide(srcPath);
		int[] xy = getXY(srcImgSide, watermarkImgSide, position, y, x);
		watermarkImg(srcPath, distPath, watermarkImg, watermarkImgSide[0], watermarkImgSide[1], xy[0], xy[1], Double.valueOf(alpha));
	}

	private int[] getImageSide(String imgPath) throws IOException {
		int[] side = new int[2];
		Image img = ImageIO.read(new File(imgPath));
		side[0] = img.getWidth(null);
		side[1] = img.getHeight(null);
		return side;
	}

	/**
	 * 添加图片水印
	 * 
	 * @param srcPath
	 *            原图片路径
	 * @param distPath
	 *            新图片路径
	 * @param watermarkImg
	 *            水印图片路径
	 * @param width
	 *            水印宽度（可以于水印图片大小不同）
	 * @param height
	 *            水印高度（可以于水印图片大小不同）
	 * @param x
	 *            水印开始X坐标
	 * @param y
	 *            水印开始Y坐标
	 * @param alpha
	 *            透明度[0-100]
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws IM4JavaException
	 */
	public synchronized void watermarkImg(String srcPath, String distPath, String watermarkImg, int width, int height, int x, int y,Double rotate) {
		try {
			CompositeCmd cmd = new CompositeCmd(true);
			String path = "D:/Program Files/GraphicsMagick-1.3.27-Q16";
			cmd.setSearchPath(path);
			IMOperation op = new IMOperation();
			op.rotate(rotate);
			op.geometry(width, height, x, y);
			op.addImage(watermarkImg);
			op.addImage(srcPath);
			op.addImage(distPath);
			cmd.run(op);
		} catch (IOException e) {
            EmpExecutionContext.error(e, "发现异常");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
            EmpExecutionContext.error(e, "发现异常");
		} catch (IM4JavaException e) {
            EmpExecutionContext.error(e, "发现异常");
		}
	}

	private int[] getXY(int[] image, int[] watermark, int position, int x, int y) {
		int[] xy = new int[2];
		if (position == 1) {
			xy[0] = x;
			xy[1] = y;
		} else if (position == 2) {
			xy[0] = (image[0] - watermark[0]) / 2; // 横向边距
			xy[1] = y; // 纵向边距
		} else if (position == 3) {
			xy[0] = image[0] - watermark[0] - x;
			xy[1] = y;
		} else if (position == 4) {
			xy[0] = x;
			xy[1] = (image[1] - watermark[1]) / 2;
		} else if (position == 5) {
			xy[0] = (image[0] - watermark[0]) / 2;
			xy[1] = (image[1] - watermark[1]) / 2;
		} else if (position == 6) {
			xy[0] = image[0] - watermark[0] - x;
			xy[1] = (image[1] - watermark[1]) / 2;
		} else if (position == 7) {
			xy[0] = x;
			xy[1] = image[1] - watermark[1] - y;
		} else if (position == 8) {
			xy[0] = (image[0] - watermark[0]) / 2;
			xy[1] = image[1] - watermark[1] - y;
		} else {
			xy[0] = image[0] - watermark[0] - x;
			xy[1] = image[1] - watermark[1] - y;
		}
		return xy;
	}

	/**
	 * 把文字转化为一张背景透明的png图片
	 * 
	 * @param str
	 *            文字的内容
	 * @param fontType
	 *            字体，例如宋体
	 * @param fontSize
	 *            字体大小
	 * @param colorStr
	 *            字体颜色，不带#号，例如"990033"
	 * @param outfile
	 *            png图片的路径
	 * @throws Exception
	 */
	public void converFontToImage(String str, String fontType, Integer fontSize, String colorStr, Integer width, Integer height, String backgroundColorStr, Integer fbold, Integer fagen, String outfile) throws Exception {

		Font font = new Font(fontType, fbold, fontSize);
		File file = new File(outfile);
		// 获取font的样式应用在str上的整个矩形
		// Rectangle2D r = font.getStringBounds(str, new
		// FontRenderContext(AffineTransform.getScaleInstance(1, 1), false,
		// false));
		// int unitHeight = (int) Math.floor(r.getHeight());// 获取单个字符的高度
		// 获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
		// int width = (int) Math.round(r.getWidth()) + 1;
		// int height = unitHeight + 3;// 把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
		// 创建图片

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
		g2d.dispose();
		g2d = image.createGraphics();
		if(!backgroundColorStr.equals("transparent")){
			g2d.setColor(new Color(Integer.parseInt(backgroundColorStr, 16)));
			g2d.fillRect(0,0,600,600);
		}
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(new Color(Integer.parseInt(colorStr, 16)));// 在换成所需要的字体颜色
		g2d.setFont(font);
		FontMetrics fontMetrics = g2d.getFontMetrics();
		int stringWidth = fontMetrics.stringWidth(str);
		switch (fagen) {
		case 0:
			g2d.drawString(str, 0, font.getSize());
			break;
		case 1:
			 int xCoordinate = width/2 - stringWidth/2;
			g2d.drawString(str,xCoordinate,font.getSize());
			break;
		case 2:
			 int x = width - stringWidth;
			g2d.drawString(str,x,font.getSize() );
			break;
		default:
			break;
		}
		ImageIO.write(image, "png", file);// 输出png图片
	}

	public static void main(String[] args) {

		try {
			String src = "D:/image/5.jpg"; // 需要加水印的源图片
			String desc = "D:/image/5_.jpg"; // 生成的水印图片的路径
			String water = "D:/image/water.png"; // 用中文转换成的背景透明的png图片
			String fontType = "方正舒体"; // 指定字体文件为宋体
			String colorStr = "000000"; // 颜色
			int fontSize = 18;

			ImageUtils watermark = new ImageUtils();

			/*
			 * 把文字转化为一张背景透明的png图片
			 * 
			 * @param str 文字的内容
			 * 
			 * @param fontType 字体，例如宋体
			 * 
			 * @param fontSize 字体大小
			 * 
			 * @param colorStr 字体颜色，不带#号，例如"990033"
			 * 
			 * @param outfile png图片的路径
			 * 
			 * @throws Exception
			 */
			 watermark.converFontToImage("妈卖批", fontType, fontSize, colorStr, 100, 100, "00ff00", 0, 0, water);

			/*
			 * 把文字的png图片贴在原图上，生成水印
			 * 
			 * @param srcPath 原图片路径
			 * 
			 * @param distPath 新图片路径
			 * 
			 * @param watermarkImg 水印图片路径
			 * 
			 * @param position 九宫格位置[1-9],从上往下,从左到右排序
			 * 
			 * @param x 横向边距
			 * 
			 * @param y 纵向边距
			 * 
			 * @param alpha 透明度
			 */
			watermark.watermarkImg(src, desc, water, 100, 100, 100, 100, 100D);
		} catch (Exception e) {
			// TODO Auto-generated catch block
            EmpExecutionContext.error(e, "发现异常");
		}

	}

}