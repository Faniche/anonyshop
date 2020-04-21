package indi.faniche.anonyshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "indi.faniche.anonyshop.user.mapper")
public class AnonyshopUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonyshopUserServiceApplication.class, args);
    }

}
