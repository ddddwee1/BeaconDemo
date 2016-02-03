package potato.beacondemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class functions {
    int height, width;
    public functions(int height, int width){
        this.height = height;
        this.width = width;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    public int widthScale(double scale){
        return (int)((double)width * scale);
    }
    public int heightScale(double scale) {
        return (int) ((double) height * scale);
    }
    public Bitmap scaledBitmap(Bitmap map,int height,int width){
        float hscale = (float)height/map.getHeight();
        float wscale = (float)width/map.getWidth();
        Matrix mtx = new Matrix();
        mtx.postScale(wscale, hscale);
        Bitmap result= Bitmap.createBitmap(map, 0, 0, map.getWidth(), map.getHeight(), mtx, true);
        return result;
    }
    public Drawable getDrawable(Bitmap bitmap,int left, int top, final int heightpix, final int widthpix){
        final Bitmap mp = bitmap;
        final float lef = (float)left;
        final float tp = (float)top;
        Drawable dr = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint pt = new Paint();
                canvas.drawBitmap(scaledBitmap(mp,heightpix,widthpix),lef,tp,pt);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter cf) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        return dr;
    }
    public String post(String data, String page) throws Exception {
        System.out.println("data="+data);
        System.out.println("site="+page);
        String result="";
        URL url = new URL(page);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //((HttpsURLConnection) conn).setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-length", String.valueOf(data.length()));

        OutputStream out = conn.getOutputStream();
        out.write(data.getBytes());
        out.flush();

        InputStream in = conn.getInputStream();
        while(true){
            int c = in.read();
            if(c == -1){
                break;
            }
            result = result + String.valueOf((char) c);
        }

        in.close();
        out.close();
        System.out.println(result);
        return result;
    }

    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private String byteToArrayString(byte bByte) {
        int iRet = bByte;
        System.out.println(iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        System.out.println(strDigits[iD1] + strDigits[iD2]);
        return strDigits[iD1] + strDigits[iD2];
    }

    private String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
}