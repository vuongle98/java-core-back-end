package com.vuog.core;

import com.vuog.core.module.rest.shared.annotation.EnableAutoGenericRest;
import com.vuog.core.module.storage.application.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoGenericRest
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableJpaRepositories
public class CoreApplication implements CommandLineRunner {

    @Resource
    private FileService fileService;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileService.init();
    }

}
