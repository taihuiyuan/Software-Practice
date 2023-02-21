package fudan.se.myWardrobe;

import fudan.se.myWardrobe.entity.*;
import fudan.se.myWardrobe.repository.CategoryRepository;
import fudan.se.myWardrobe.repository.LocationRepository;
import fudan.se.myWardrobe.repository.WardrobeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
//import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Welcome to 2021 Software Engineering Lab2.
 * This is your first lab to write your own code and build a spring boot application.
 * Enjoy it :)
 *
 * @author LBW
 */
@SpringBootApplication
@Configuration
public class MyWardrobeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyWardrobeApplication.class, args);
    }

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader(CategoryRepository categoryRepository, WardrobeRepository wardrobeRepository, LocationRepository locationRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                List<Wardrobe> wardrobes = wardrobeRepository.findAll();
                for (Wardrobe wardrobe: wardrobes){
                    //对于每个衣橱都有一个未分类
                    if (categoryRepository.findByWardrobeAndName(wardrobe, "未分类") == null){
                        categoryRepository.save(new Category(wardrobe, "未分类"));
                    }

                    //对于每一个衣橱添加初始位置信息：卧室衣柜、衣帽间、鞋柜
                    Location location0 = locationRepository.findByWardrobeAndName(wardrobe, "默认");
                    Location location1 = locationRepository.findByWardrobeAndName(wardrobe, "卧室衣柜");
                    Location location2 = locationRepository.findByWardrobeAndName(wardrobe, "衣帽间");
                    Location location3 = locationRepository.findByWardrobeAndName(wardrobe, "鞋柜");

                    if (location0 == null){
                        Location location = new Location(wardrobe, "默认");
                        locationRepository.save(location);
                    }

                    if (location1 == null){
                        Location location = new Location(wardrobe, "卧室衣柜");
                        locationRepository.save(location);
                    }

                    if (location2 == null){
                        Location location = new Location(wardrobe, "衣帽间");
                        locationRepository.save(location);
                    }

                    if (location3 == null){
                        Location location = new Location(wardrobe, "鞋柜");
                        locationRepository.save(location);
                    }
                }
            }
        };
    }
}

