package com.zxing.encoding;

import java.util.Hashtable;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.qrcode.R;
/**
 * @author George
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;
	
	private static final int WHITE = 0xFFFFFFFF;
	
	public static Bitmap createQRCode(String str,int widthAndHeight,Bitmap photo) throws WriterException {
		//Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();  
		Hashtable hints = new Hashtable();  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
        
         //指定纠错级别(L--7%,M--15%,Q--25%,H--30%) 
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
       
        
     
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight,hints);
		
	
		
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		int x1=0,x2=0,y1=0,y2=0,x3=0,y3=0;
		int smallRecSide=0;
		
		int w2=0;
	    int h2=0;
		
		
		boolean flag=true;
		boolean flag2=true;
		
        //将像素还原
		for (int y = 0; y < height; y++) {
			
			for (int x = 0; x < width; x++) {
				
				if (matrix.get(x, y)) {
					if(flag==true){
						x1=x;
						y1=y;
						flag=false;
					}
					//pixels[y * width + x] = PhotoCopy.getPixel(x, y);
					x2=x;
					y2=y;
				}else{
					
					if(flag2==true&&flag==false){
						x3=x;
						y3=y;
						flag2=false;
					}
				}
				
			}
		
		}
		
		
		
		
		smallRecSide=(x3-x1)/7;
		
		w2=h2=y2-y1;
		
		//对图片进行缩放处理
		
		int w = photo.getWidth();  
        int h = photo.getHeight();  
        float scaleX = w2 * 1F / w;  
        float scaleY = h2* 1F / h;  
        Matrix matrix2 = new Matrix();  
        matrix2.postScale(scaleX, scaleY);  
          
        Bitmap PhotoCopy = Bitmap.createBitmap(photo, 0, 0, w, h, matrix2, true);
        
        Bitmap PhotoT=getTransparentBitmap(PhotoCopy,100);
		
		
        
        
        //初始的扫描算法
	       for (int y = 0; y < height; y++) {
				
				for (int x = 0; x < width; x++) {
					
					 pixels[y * width + x] = WHITE;
					
					
				}
	       }
        
	       
	       
          //累加image图片部分
	      
          for (int y = y1; y < y2; y++) {
			
			for (int x = x1; x < x1+w2; x++) {
				
				if (matrix.get(x, y)) {
					
					    //pixels[y * width + x] = BLACK;
					    pixels[y * width + x] = PhotoCopy.getPixel(x-x1, y-y1);
					
					
				}
				else{
					
				      //pixels[y * width + x] =WHITE;
					  pixels[y * width + x] =PhotoT.getPixel(x-x1, y-y1);
					
				}
			}
          }
        
          
//          //加入三个固定点
//          
//          
//          //1
//          for (int y = y1; y < y1+7*smallRecSide; y++) {
//  			
//  			for (int x = x1; x < x1+7*smallRecSide; x++) {
//  				
//  				if (matrix.get(x, y)) {
//  					
//  				
//  					pixels[y * width + x] = BLACK;
//  					
//  				
//  				}
//  				else{
//  					
//  					//pixels[y * width + x] =PhotoCopy.getPixel(x-x1, y-y1);
//  					
//  				}
//  			}
//  			
//            }
//          //2
//          for (int y = y1; y < y1+7*smallRecSide; y++) {
//    			
//    			for (int x =x1+w2-7*smallRecSide; x < x1+w2; x++) {
//    				
//    				if (matrix.get(x, y)) {
//    					
//    				
//    					pixels[y * width + x] = BLACK;
//    					
//    				
//    				}
//    				else{
//    					
//    					//pixels[y * width + x] =PhotoCopy.getPixel(x-x1, y-y1);
//    					
//    				}
//    			}
//    			
//              }
//          
//          //3
//          for (int y = y2-7*smallRecSide; y < y2; y++) {
//  			
//  			for (int x =x1; x < x1+7*smallRecSide; x++) {
//  				
//  				if (matrix.get(x, y)) {
//  					
//  				
//  					pixels[y * width + x] = BLACK;
//  					
//  				
//  				}
//  				else{
//  					
//  					//pixels[y * width + x] =PhotoCopy.getPixel(x-x1, y-y1);
//  					
//  				}
//  		  	}
//  			
//           }
          
          
          //加入辅助块
          int center=smallRecSide/3; //3*3
          
          int center2=smallRecSide/5; //5*5
          
          int center3=smallRecSide/4; //2*2
          
          int center4=smallRecSide/8; //4*4
          
          int center5=smallRecSide/7; //7*7
          
          int center6=smallRecSide/9; //9*9
          
          
          
          for (int y = y1; y < y2; y+=smallRecSide) {
  			
  			for (int x = x1; x < x1+w2; x+=smallRecSide) {
  				
  				
  				if (matrix.get(x, y)) {
  					
//  					    if(PhotoCopy.getPixel(x-x1,y-y1)==WHITE)
//  					    {
  					
  					    //pixels[y * width + x] = PhotoCopy.getPixel(x-x1, y-y1);
  					    for(int b=y+2*center2;b<y+3*center2;b++)
  					    {
  					    	
  					    	for(int a=x+2*center2;a<x+3*center2;a++)
  					    	{
  					    		pixels[b * width + a] =BLACK;
  					    	}
  					    	
  					    }
//  					    }
  					
  					
  				}else{
  					
//                        if(PhotoCopy.getPixel(x-x1,y-y1)==WHITE)
//                        {
  					    for(int b=y+2*center2;b<y+3*center2;b++)
					    {
					    	for(int a=x+2*center2;a<x+3*center2;a++)
					    	{
					    		pixels[b * width + a] =WHITE;
					    	}
					    }
//                        }
  					
  					
  					
  				}
  				
  			
  		}
      }
          
          //累加半张图片
          for (int y = y1+0*h2/3+40; y < y1+1*h2/3+30; y++) {
  			
  			for (int x = x1+1*w2/3+10; x < x1+2*w2/3+10; x++) {
  				
  				if (matrix.get(x, y)) {
  					
  					    pixels[y * width + x] = PhotoCopy.getPixel(x-x1, y-y1);
  					
  					
  				}
  				else{
  					
  					pixels[y * width + x] =PhotoT.getPixel(x-x1, y-y1);
  					
  					
  					
  				}
  			}
            }
          
    
          //加入三个固定点
          //1
          for (int y = y1; y < y1+7*smallRecSide; y++) {
  			
  			for (int x = x1; x < x1+7*smallRecSide; x++) {
  				
  				if (matrix.get(x, y)) {
  					
  				
  					pixels[y * width + x] = BLACK;
  					
  				
  				}
  				else{
  					
  					//pixels[y * width + x] =PhotoCopy.getPixel(x-x1, y-y1);
  					pixels[y * width + x] = WHITE;
  				}
  			}
  			
            }
          //2
          for (int y = y1; y < y1+7*smallRecSide; y++) {
    			
    			for (int x =x1+w2-7*smallRecSide; x < x1+w2; x++) {
    				
    				if (matrix.get(x, y)) {
    					
    				
    					pixels[y * width + x] = BLACK;
    					
    				
    				}
    				else{
    					
    					//pixels[y * width + x] =PhotoCopy.getPixel(x-x1, y-y1);
    					pixels[y * width + x] = WHITE;
    				}
    			}
    			
              }
          
          //3
          for (int y = y2-7*smallRecSide; y < y2; y++) {
  			
  			for (int x =x1; x < x1+7*smallRecSide; x++) {
  				
  				if (matrix.get(x, y)) {
  					
  				
  					pixels[y * width + x] = BLACK;
  					
  				
  				}
  				else{
  					
  					//pixels[y * width + x] =PhotoCopy.getPixel(x-x1, y-y1);
  					pixels[y * width + x] = WHITE;
  				}
  		  	}
  			
           }
          
//          //加入两条Timing Pattern
//          
//          //1
//          for (int y = y1+6*smallRecSide; y < y2-7*smallRecSide; y+=smallRecSide) {
//    			
//    			for (int x =x1+6*smallRecSide; x < x1+7*smallRecSide; x+=smallRecSide) {
//    				
//    				if (matrix.get(x, y)) {
//    					
//    					 for(int b=y+1*center;b<y+2*center;b++)
//   					    {
//   					    	
//   					    	for(int a=x+1*center;a<x+2*center;a++)
//   					    	{
//   					    		pixels[b * width + a] =BLACK;
//   					    	}
//   					    	
//   					    }
//    					
//    				
//    				}
//    				else{
//    					
//    					for(int b=y+1*center;b<y+2*center;b++)
//					    {
//					    	for(int a=x+1*center;a<x+2*center;a++)
//					    	{
//					    		pixels[b * width + a] =WHITE;
//					    	}
//					    }
//    				}
//    		  	}
//    			
//             }
//          
//          //2
//          for (int y = y1+6*smallRecSide; y < y1+7*smallRecSide; y+=smallRecSide) {
//  			
//  			for (int x =x1+7*smallRecSide; x < x1+w2-7*smallRecSide; x+=smallRecSide) {
//  				
//  				if (matrix.get(x, y)) {
//  					
//  					 for(int b=y+1*center;b<y+2*center;b++)
// 					    {
// 					    	
// 					    	for(int a=x+1*center;a<x+2*center;a++)
// 					    	{
// 					    		pixels[b * width + a] =BLACK;
// 					    	}
// 					    	
// 					    }
//  					
//  				
//  				}
//  				else{
//  					
//  					for(int b=y+1*center;b<y+2*center;b++)
//					    {
//					    	for(int a=x+1*center;a<x+2*center;a++)
//					    	{
//					    		pixels[b * width + a] =WHITE;
//					    	}
//					    }
//  				}
//  		  	}
//  			
//           }
          
          
          
          
          
          
          
		
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	
	//修改原图片的透明度
	public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number){  
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];  
  
        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg  
  
                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值  
  
        number = number * 255 / 100;  
  
        for (int i = 0; i < argb.length; i++) {  
  
            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);  
  
        }  
  
        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg  
  
                .getHeight(), Config.ARGB_8888);  
  
        return sourceImg;  
    }  
	
	

	
	
}

 
