package com.acimage.admin;

import com.acimage.common.global.enums.ImageType;
import com.acimage.common.utils.QiniuUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class AdminApplicationTest {
	@Autowired
	QiniuUtils qiniuUtils;

	@Test
	void qiniuUtilsTest() {
		File file = new File("F:\\MyImage\\素材\\bg2.png");
		qiniuUtils.upload(file,"test/xlg.png");
	}

	@Test
	void enumTest() {
		ImageType imageType=ImageType.HOME_CAROUSEL;
		System.out.println(imageType);
		String homeCarousel="HOME_CAROUSEL";
		System.out.println(ImageType.getImageType(homeCarousel));
		String nullString=null;
		System.out.println(nullString+"dsadsa");
	}





}
