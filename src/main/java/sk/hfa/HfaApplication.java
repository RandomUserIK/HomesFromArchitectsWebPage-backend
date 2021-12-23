package sk.hfa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class HfaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HfaApplication.class, args);

        log.info("+---------------------------+");
        log.info("| Application HFA is active |");
        log.info("+---------------------------+");
    }

}
