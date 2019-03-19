package cn.utstarcom.algorithmtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

import cn.utstarcom.udnt.common.util.LogToConsole;

/**
 * Hello world!
 *
 */
@EnableApolloConfig
@SpringBootApplication
public class AlgorithmtestApplication {

    private static final Logger log = LoggerFactory.getLogger(AlgorithmtestApplication.class);

    static {

        System.getProperties().put("spring.config.location",
                "classpath:application.yml, file:${spring.property.path}/config/algorithmtest.yml");
    }

    public static void main(String[] args) {

        log.info("the algorithmtest begin to start ....");
        String userDir = System.getProperty("user.dir");
        SpringApplication.run(AlgorithmtestApplication.class, args);
        log.info("the algorithmtest start completed. the user dir: {}", userDir);
        LogToConsole.out("AlgorithmtestApplication",
                "the algorithmtest start completed. the user dir: " + userDir);
    }
}
