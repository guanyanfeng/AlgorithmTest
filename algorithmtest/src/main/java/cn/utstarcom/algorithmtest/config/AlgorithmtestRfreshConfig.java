/**
 * created on 2018年8月29日 下午2:38:24
 */
package cn.utstarcom.algorithmtest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

/**
 * @author UTSC0167
 * @date 2018年8月29日
 *
 */
@Component
public class AlgorithmtestRfreshConfig {

    private static final Logger log = LoggerFactory.getLogger(AlgorithmtestRfreshConfig.class);

    @Autowired
    private RefreshScope refreshScope;

    @Autowired
    AlgorithmtestConfig algorithmtestConfig;

    @ApolloConfigChangeListener
    private void onChange(ConfigChangeEvent changeEvent) {

        log.info("onChange changeEvent for nameSpace: {}", changeEvent.getNamespace());
        if (changeEvent.changedKeys() != null || changeEvent.changedKeys().size() > 0) {
            log.info("onChange before algorithmtestConfig refresh {}", algorithmtestConfig);
            refreshScope.refresh("algorithmtestConfig");
            log.info("onChange after algorithmtestConfig refresh {}", algorithmtestConfig);
        }
    }
}
