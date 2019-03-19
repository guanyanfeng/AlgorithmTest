/**
 * created on 2018年8月29日 下午2:08:02
 */
package cn.utstarcom.algorithmtest.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author UTSC0167
 * @date 2018年8月29日
 *
 */
@RefreshScope
@Component("algorithmtestConfig")
public class AlgorithmtestConfig {

    private static final Logger log = LoggerFactory.getLogger(AlgorithmtestConfig.class);

    private final String applicationName;

    @Value("${server.address:127.0.0.1}")
    private String serverAddress;

    @Value("${server.port:8080}")
    private int serverPort;

    @PostConstruct
    private void init() {

        log.info("init algorithmtestConfig applicationName: {} serverAddress: {} serverPort： {}",
                this.applicationName, this.serverAddress, this.serverPort);
    }

    public AlgorithmtestConfig(
            @Value("${spring.application.name:algorithmtestConfig}") String applicationName) {
        this.applicationName = applicationName;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String toString() {
        return "AlgorithmtestConfig [applicationName=" + applicationName + ", serverAddress="
                + serverAddress + ", serverPort=" + serverPort + "]";
    }
}
