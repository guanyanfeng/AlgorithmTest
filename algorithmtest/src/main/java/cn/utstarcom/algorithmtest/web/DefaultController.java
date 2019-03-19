/**
 * created on 2017年11月14日 下午4:47:56
 */
package cn.utstarcom.algorithmtest.web;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.utstarcom.algorithmtest.vo.NeHeartbeatRequest;
import cn.utstarcom.algorithmtest.vo.NeHeartbeatResponse;
import cn.utstarcom.udnt.common.util.TimeUtils;

/**
 * @author UTSC0167
 * @date 2017年11月14日
 *
 */
@RestController
public class DefaultController {

    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);

    @PostMapping("/api/v1/ne-heartbeat")
    public NeHeartbeatResponse neHeartbeat(@RequestBody NeHeartbeatRequest heartbeatRequest,
            HttpServletRequest request) {

        final Instant startInstant = TimeUtils.getCurrentInstant();
        log.debug("neHeartbeat begin for clientIp: {} clientPort: {} requestBody: {}",
                request.getRemoteAddr(), request.getRemotePort(), heartbeatRequest);

        NeHeartbeatResponse heartbeatResponse = new NeHeartbeatResponse();
        heartbeatResponse.setAppType(999);
        heartbeatResponse.setAppVersion("1.0.0");
        heartbeatResponse.setServiceStatus(0);
        log.debug(
                "neHeartbeat end for clientIp: {} clientPort: {} heartbeatResponse: {} usedTime: {}",
                request.getRemoteAddr(), request.getRemotePort(), heartbeatResponse,
                TimeUtils.calcMillisStartToCurrent(startInstant));

        return heartbeatResponse;
    }
}
