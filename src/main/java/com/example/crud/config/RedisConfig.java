package com.example.crud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisProperties;
import redis.clients.jedis.JedisPoolConfig;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述：redis默认使用StringRedisSerializer来实现序列化，但是StringRedisSerializer不支持中文，
 * 会出现 乱码，所以使用GenericJackson2JsonRedisSerializer，支持中文
 *
 * @author: lijie
 * @date: 2021/1/21 9:20
 * @version: V1.0
 */


@Configuration
public class RedisConfig {

    /**
     * 单机版用下面这一套就够了
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建RedisTemplate<String, Object>对象
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 配置连接工厂
        redisTemplate.setConnectionFactory(factory);
        // 定义Jackson2JsonRedisSerializer序列化对象
        Jackson2JsonRedisSerializer<Object> jacksonSeial = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会报异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);
        StringRedisSerializer stringSerial = new StringRedisSerializer();
        // redis key 序列化方式使用stringSerial
        redisTemplate.setKeySerializer(stringSerial);
        // redis value 序列化方式使用jackson
        redisTemplate.setValueSerializer(jacksonSeial);
        // redis hash key 序列化方式使用stringSerial
        redisTemplate.setHashKeySerializer(stringSerial);
        // redis hash value 序列化方式使用jackson
        redisTemplate.setHashValueSerializer(jacksonSeial);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


//    /**
//     * 集群还要把下面这些代码都加上
//     */
//    @Value("${spring.redis.lettuce.pool.max-wait}")
//    private Long maxWait;
//    @Value("${spring.redis.lettuce.pool.max-active}")
//    private Integer maxActive;
//    @Value("${spring.redis.lettuce.pool.max-idle}")
//    private Integer maxIdle;
//    @Value("${spring.redis.lettuce.pool.min-idle}")
//    private Integer minIdle;
//    @Value("${spring.redis.cluster.nodes}")
//    private String nodes;
//    @Value("${spring.redis.sentinel.password}")
//    private String password;
//    @Value("${spring.redis.database}")
//    private Integer database;
//
//    @Bean
//    public JedisPoolConfig jedisPoolConfig() {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(maxWait);
//        jedisPoolConfig.setMaxTotal(maxActive);
//        jedisPoolConfig.setMinIdle(minIdle);
//        return jedisPoolConfig;
//    }
//
//    @Bean
//    public RedisClusterConfiguration jedisClusterConfig() {
//        RedisClusterConfiguration config = new RedisClusterConfiguration();
//        String[] sub = nodes.split(",");
//        List<RedisNode> nodeList = new ArrayList<>(sub.length);
//        String[] tmp;
//        for (String s : sub) {
//            tmp = s.split(":");
//            // fixme 先不考虑异常配置的case
//            nodeList.add(new RedisNode(tmp[0], Integer.valueOf(tmp[1])));
//        }
//        config.setClusterNodes(nodeList);
//        config.setPassword(RedisPassword.of(password));
//        return config;
//    }
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPool, RedisClusterConfiguration jedisConfig) {
//        JedisConnectionFactory factory = new JedisConnectionFactory(jedisConfig, jedisPool);
//        factory.afterPropertiesSet();
//        return factory;
//    }

}
