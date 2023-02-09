package com.acimage.admin;


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







}
