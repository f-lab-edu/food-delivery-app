package com.fdel.config.property;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Qualifier("redisSessionServerProperties")
@ConfigurationProperties(prefix = "spring.redis")
@Component
@Getter
public class RedisSessionServerPorperties extends RedisProperties{
	
	/**
	 * Database index used by the connection factory.
	 */
	private int database = 0;
	
	/**
	 * Redis server host.
	 */
	private String host = "localhost";

	/**
	 * Redis server port.
	 */
	private int port;

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
