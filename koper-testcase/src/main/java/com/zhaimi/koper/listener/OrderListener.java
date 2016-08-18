package com.zhaimi.koper.listener;

import com.zhaimi.koper.AbstractMessageListener;
import com.zhaimi.koper.Listen;
import com.zhaimi.koper.demo.trading.mapper.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author caie
 */
@Component
@Listen(topic = "zhaimi.orderListener")
public class OrderListener extends AbstractMessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(String msg) {
        logger.info("got msg! msg: {}", msg);
    }

    public void onMessage(Order order) {
        logger.info("got Order! order: {}", order.toString());
    }

}
