package com.example.shopmebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.example.shopmecommon.entity", "com.example.shopmebackend.user"})
public class ShopmeBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeBackEndApplication.class, args);
    }

}
