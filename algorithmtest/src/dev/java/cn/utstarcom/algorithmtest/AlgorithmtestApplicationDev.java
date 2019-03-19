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
public class AlgorithmtestApplicationDev {

    private static final Logger log = LoggerFactory.getLogger(AlgorithmtestApplicationDev.class);

    static {
        String userDir = System.getProperty("user.dir");
        System.getProperties().put("spring.property.path", userDir + "/src/main");
        System.getProperties().put("spring.config.location",
                "classpath:application.yml, file:${spring.property.path}/config/algorithmtest.yml");
        System.getProperties().put("logging.level.root", "info");
    }

    public static void main(String[] args) {

        log.info("the algorithmtest begin to start ....");
        String userDir = System.getProperty("user.dir");
        SpringApplication.run(AlgorithmtestApplicationDev.class, args);
        log.info("the algorithmtest start completed. the user dir: {}", userDir);
        LogToConsole.out("AlgorithmtestApplication",
                "the algorithmtest start completed. the user dir: " + userDir);
    }
}
