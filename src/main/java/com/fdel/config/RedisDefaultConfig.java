package com.fdel.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fdel.config.property.RedisCartServerProperties;

@Configuration
public class RedisDefaultConfig {
	
	@Primary
	@Bean
	public RedisProperties redisProperty() {
		return new RedisCartServerProperties();
	}
	
	// lettuce
    @Bean
    public RedisConnectionFactory redisConnectionFactory(
    		@Qualifier("redisCartServerProperties") RedisProperties redisPorperties) {
        return new LettuceConnectionFactory(redisPorperties.getHost(), redisPorperties.getPort());
    }
	
	@Qualifier("redisTemplate")
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
