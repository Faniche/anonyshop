package indi.faniche.anonyshop.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan(basePackages = "indi.faniche.anonyshop.product")
public class AnonyshopProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnonyshopProductServiceApplication.class, args);
    }
}
