package com.example.shopmebackend.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("user-photos", registry);
        exposeDirectory("category-photos", registry);
        exposeDirectory("brand-photos", registry);
        exposeDirectory("product-photos", registry);
    }

    public void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path photoDir = Paths.get(dirName);
        String path = photoDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + path + "/");
    }

}
