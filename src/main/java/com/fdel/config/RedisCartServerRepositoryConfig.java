package com.fdel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableRedisRepositories
public class RedisCartServerRepositoryConfig {
	
	private final RedisProperties redisCartServerProperties;
	
	@Autowired
	public  RedisCartServerRepositoryConfig(
			@Qualifier("redisCartServerProperties") RedisProperties redisCartServerProperties) {
		this.redisCartServerProperties = redisCartServerProperties;
	}

    // lettuce
	@Qualifier("redisCartServerConnectionFactory")
    @Bean
    public RedisConnectionFactory redisCartServerConnectionFactory() {
        return new LettuceConnectionFactory(redisCartServerProperties.getHost(), redisCartServerProperties.getPort());
    }

    @Qualifier("redisCartTemplate")
    @Bean
    public RedisTemplate<String, String> redisCartTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisCartServerConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // Hash Operation 사용 시
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        
        // 아래 설정으로 모든 Key / Value Serialization을 변경할 수 있음
        //redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        
        return redisTemplate;
    }
    
    @Bean
    public ObjectMapper objectMapper() {
    	return new ObjectMapper();
    }

}
