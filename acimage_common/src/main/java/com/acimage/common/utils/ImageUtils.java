package com.acimage.common.utils;

import com.acimage.common.global.exception.BusinessException;
import com.acimage.common.global.consts.FileFormatConstants;
import com.acimage.common.utils.common.FileUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Slf4j
public class ImageUtils {

    public static InputStream compressAsFixedWebpImage(MultipartFile multipartFile, int width, int height, int limitSize) {
        try {
            float qualify = 0.77f;
            BufferedImage bufferedImage = Thumbnails.fromInputStreams(Collections.singletonList(multipartFile.getInputStream()))
                    .outputQuality(qualify)
                    .size(width, height)
                    .outputFormat(FileFormatConstants.WEBP)
                    .asBufferedImage();
            try (InputStream is = ImageUtils.bufferedImage2InputStream(bufferedImage, FileFormatConstants.WEBP)) {
                if (is != null && is.available() > limitSize) {
                    log.warn("the image size after compressing exceed {} size:{}", limitSize, is.available());
                    throw new BusinessException("图片压缩后仍然较大，请尝试其它图片");
                }
                return is;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException("图片数据错误");
        }
    }

    public static InputStream compressAsWebpImage(MultipartFile multipartFile, int limitSize, int limitLength) {
        try {
            float qualify = 0.77f;
            BufferedImage image = ImageIO.read(multipartFile.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();
            if (width > limitLength || height > limitLength) {
                if (width > height) {
                    width = limitLength;
                    //等比例缩放
                    height = (int) (1.0 * height * limitLength / width);
                } else {
                    height = limitLength;
                    width = (int) (1.0 * width * limitLength / height);
                }
            }

            BufferedImage bufferedImage = Thumbnails.fromInputStreams(Collections.singletonList(multipartFile.getInputStream()))
                    .outputQuality(qualify)
                    .size(width, height)
                    .outputFormat(FileFormatConstants.WEBP)
                    .asBufferedImage();

            try (InputStream is = ImageUtils.bufferedImage2InputStream(bufferedImage, FileFormatConstants.WEBP)) {
                if (is != null && is.available() > limitSize) {
                    log.warn("the image size after compressing exceed {} size:{}", limitSize, is.available());
                    throw new BusinessException("图片压缩后仍然较大，请尝试其它图片");
                }
                return is;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessException("图片数据错误");
        }
    }


    public static InputStream compressBak(MultipartFile multipartFile, int limitSize) {

        String format = FileUtils.formatOf(multipartFile);
        try {
            long size = multipartFile.getSize();
            if (size < limitSize) {
                return multipartFile.getInputStream();
            }
//            if (size < 240 * 1000) {
//                return multipartFile.getInputStream();
//            } else if (limitSize * 1.0 / size > 0.8f) {
//                return multipartFile.getInputStream();
//            }
//            Thumbnails.fromInputStreams(Collections.singletonList(multipartFile.getInputStream()));

            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            if (bufferedImage == null) {
                log.error("bufferedImage为空");
                throw new BusinessException("图像为空");
            }
            Thumbnails.Builder<BufferedImage> imageBuilder = Thumbnails.fromImages(Collections.singletonList(bufferedImage));
//            BufferedImage bufferedImage = inputStreamBuilder.asBufferedImage();
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            //按边压缩比例，要开根号
            double scale = Math.sqrt(limitSize * 1.0 / (width * height));
            imageBuilder.scale(scale).outputQuality(1f);
//            imageBuilder.size(270, 260).outputQuality(1f);
            return ImageUtils.bufferedImage2InputStream(imageBuilder.asBufferedImage(), FileUtils.formatOf(multipartFile));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static InputStream bufferedImage2InputStream(BufferedImage image, String format) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
