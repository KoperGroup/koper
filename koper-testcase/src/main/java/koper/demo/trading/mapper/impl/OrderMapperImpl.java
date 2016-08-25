package koper.demo.trading.mapper.impl;

import koper.aop.SendMessageAfter;
import koper.aop.SendMessageBefore;
import koper.demo.trading.mapper.OrderMapper;
import koper.demo.trading.mapper.Order;
import org.springframework.stereotype.Component;

/**
 * @author kk
 * @since 1.0
 */
@Component
public class OrderMapperImpl implements OrderMapper {

    /**
     * 方法调用前发送数据消息
     */
    @SendMessageBefore(topic = "Order.onBeforeCreateOrder")
    public String createOrder(String memberId, Order order) {
        System.out.println(String.format("createOrder创建订单: %s,%s, %s", order.getOrderId(), order.getOrderName(), this));

        return "NEW_ORDER" + order.getOrderId();
    }

    /**
     * 方法调用后发送数据消息
     */
    @Override
    public String cancelOrder(Order order) {
        System.out.println(String.format("cancelOrder创建订单: %s,%s, %s", order.getOrderId(), order.getOrderName(), this));
        cancelOldOrder(order);
        return "NEW_ORDER" + order.getOrderId();
    }

    @Override
    @SendMessageAfter(topic = "Order.cancelOldOrder")
    public String cancelOldOrder(Order order) {
        System.out.println(String.format("cancelOldOrder创建订单: %s,%s, %s", order.getOrderId(), order.getOrderName(), this));
        return "CANCEL_ORDER" + order.getOrderId();
    }

    public String getOrder(String orderId, Order order) {
        System.out.println(">>>>> getOrder " + orderId);
        return "ORDER_" + orderId;
    }


    @Override
    public String updateOrder(Order order) {
        System.out.println(">>>>> updateOrder " + order);
        return "UPDATEORDER_" + order.getOrderId();
    }

    /* (non-Javadoc)
     * @see com.kk.trading.mapper.OrderMapper#insert(java.lang.String, com.kk.trading.mapper.Order)
     */
    @Override
    public String insertOrder(Order order) {
        return "OD-" + order.getOrderId();
    }

    /* (non-Javadoc)
     * @see com.kk.trading.mapper.OrderMapper#insert(java.lang.String, com.kk.trading.mapper.Order)
     */
    @Override
    public String deleteOrder(String orderId, Order order) {
        System.out.println(">>>>> insert Order " + orderId);
        throw new RuntimeException("Stock insufficient!");
        // return "OD-" + orderId;
    }

    /**
     * @see OrderMapper#deleteOldOrder(int, boolean)
     */
    @SendMessageAfter(topic = "deleteOldOrder")
    @Override
    public String deleteOldOrder(int orderId, boolean reserve) {
        return "DELOLD-" + orderId;
    }
}
