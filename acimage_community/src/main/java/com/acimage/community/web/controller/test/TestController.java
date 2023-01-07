package com.acimage.community.web.controller.test;



import com.acimage.feign.bak.FileClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@Validated
@RequestMapping("/api")
public class TestController {

    @Autowired
    FileClient fileClient;


    @GetMapping("/testClient")
    @ResponseBody
    public String downloadImagesOfTopic() {
        System.out.println(fileClient.sayHello());
        return "community:"+fileClient.sayHello();
    }



}
