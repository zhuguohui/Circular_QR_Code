package com.example.circular_qr_code;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2023/5/26
 * Time: 14:07
 * Desc:用于生成圆形的二维码的工具
 * </pre>
 */
public class CircularQRCodeUtil {


    /**
     * 生成圆形二维码
     * @param text 需要编码的文本
     * @param width 生成图片的宽度
     * @param height 生成图片的高度
     * @param color 圆点的颜色
     * @param quietZone 边距
     * @return 生成好的二维码
     * @throws WriterException
     */
    public static Bitmap generateQRCodeImage(String text, int width, int height, int color,int quietZone) throws WriterException {
        final Map<EncodeHintType, Object> encodingHints = new HashMap<>();
        encodingHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCode code = Encoder.encode(text, ErrorCorrectionLevel.M, encodingHints);
        return renderQRImage(code, width, height, color,quietZone);
    }



    private static Bitmap renderQRImage(QRCode code, int width, int height,int color, int quietZone) {
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawColor(Color.WHITE);

        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
        final int FINDER_PATTERN_SIZE = 7;
        final float CIRCLE_SCALE_DOWN_FACTOR = 30f/30f;
        int circleSize = (int) (multiple * CIRCLE_SCALE_DOWN_FACTOR);
        float radius=circleSize*1.0f/2;
        int cxStep=multiple/2;

        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    if (!(inputX <= FINDER_PATTERN_SIZE && inputY <= FINDER_PATTERN_SIZE ||
                            inputX >= inputWidth - FINDER_PATTERN_SIZE && inputY <= FINDER_PATTERN_SIZE ||
                            inputX <= FINDER_PATTERN_SIZE && inputY >= inputHeight - FINDER_PATTERN_SIZE)) {
                        int cx=outputX+cxStep;
                        int cy=outputY+cxStep;
                        canvas.drawCircle(cx,cy, radius,paint);
                    }
                }
            }
        }

        int circleDiameter = multiple * FINDER_PATTERN_SIZE;
        circleDiameter/=2;
        drawFinderPatternCircleStyle(canvas,paint,color, leftPadding, topPadding, circleDiameter);
        drawFinderPatternCircleStyle(canvas,paint, color,leftPadding + (inputWidth - FINDER_PATTERN_SIZE) * multiple, topPadding, circleDiameter);
        drawFinderPatternCircleStyle(canvas,paint, color,leftPadding, topPadding + (inputHeight - FINDER_PATTERN_SIZE) * multiple, circleDiameter);

        return bitmap;
    }

    private static void drawFinderPatternCircleStyle(Canvas canvas,Paint paint,int color, int x, int y, int circleDiameter) {
        final int WHITE_CIRCLE_DIAMETER = circleDiameter*5/7;
        final int MIDDLE_DOT_DIAMETER = circleDiameter*3/7;
        x+=circleDiameter;
        y+=circleDiameter;
        paint.setColor(color);
        canvas.drawCircle(x, y, circleDiameter,paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x , y , WHITE_CIRCLE_DIAMETER,paint);
        paint.setColor(color);
        canvas.drawCircle(x , y , MIDDLE_DOT_DIAMETER,paint);
    }
}
