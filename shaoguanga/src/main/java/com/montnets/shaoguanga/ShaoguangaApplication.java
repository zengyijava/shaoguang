package com.montnets.shaoguanga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author WJH
 * @Description
 * @date 2021/4/7 16:52
 * @Email ibytecode2020@gmail.com
 */
@EnableScheduling
@SpringBootApplication
public class ShaoguangaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShaoguangaApplication.class, args);
    }

}
