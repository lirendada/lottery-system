package com.liren.lottery_system.common.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DirectRabbitConfig {
    public static final String QUEUE_NAME = "DirectQueue";
    public static final String EXCHANGE_NAME = "DirectExchange";
    public static final String ROUTING = "DirectRouting";

    public static final String DLX_QUEUE_NAME = "DlxQueue";
    public static final String DLX_EXCHANGE_NAME = "DlxExchange";
    public static final String DLX_ROUTING = "DlxRouting";

    /**
     * 队列 起名：DirectQueue
     */
    @Bean
    public Queue directQueue() {
        // durable：是否持久化，默认是false（持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效）
        // exclusive：默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete：是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // return new Queue("DirectQueue",true,true,false);
        // 一般设置一下队列的持久化就好,其余两个就是默认false
//        return new Queue(QUEUE_NAME, true);

        return QueueBuilder.durable(QUEUE_NAME)
                .deadLetterExchange(DLX_EXCHANGE_NAME)
                .deadLetterRoutingKey(DLX_ROUTING).build();
    }

    /**
     * Direct交换机 起名：DirectExchange
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    /**
     * 绑定 将队列和交换机绑定, 并设置用于匹配键：DirectRouting
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder
                .bind(directQueue())
                .to(directExchange())
                .with(ROUTING);
    }


    /**
     * 死信队列
     */
    @Bean
    public Queue dlxQueue() {
        // durable：是否持久化，默认是false（持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效）
        // exclusive：默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete：是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // return new Queue("DirectQueue",true,true,false);
        // 一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(DLX_QUEUE_NAME, true);
    }

    /**
     * Direct交换机 起名：DirectExchange
     */
    @Bean
    DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME, true, false);
    }

    /**
     * 绑定 将队列和交换机绑定, 并设置用于匹配键：DirectRouting
     */
    @Bean
    Binding bindingDlx() {
        return BindingBuilder
                .bind(dlxQueue())
                .to(dlxExchange())
                .with(DLX_ROUTING);
    }



    /**
     * 消息转换器，将对象序列化为JSON，反序列时自动转换为java对象
     */
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}