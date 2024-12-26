package com.placeHere.server.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {

    public ThymeleafConfig(){
		System.out.println("==> ThymeleafConfig() default Constructor call.............");
	}

    @Bean
    public SpringResourceTemplateResolver springResourceTemplateResolver() {
        SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
        springResourceTemplateResolver.setPrefix("classpath:templates/");
        springResourceTemplateResolver.setCharacterEncoding("UTF-8");
        springResourceTemplateResolver.setSuffix(".html");
        springResourceTemplateResolver.setTemplateMode(TemplateMode.HTML);
        springResourceTemplateResolver.setCacheable(false);

        return springResourceTemplateResolver;
    }


}
