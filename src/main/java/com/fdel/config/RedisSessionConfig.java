package com.fdel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;


/**
 * 1. ConcurrentSessionControlAuthenticationStrategy
 * 같은 계정의 사용자가 사용할 수 있는 최대 세션 수 제한
 * 2. ChangeSessionAuthenticationStragegy
 * 세션 위변조 방지 전략
 * 3. RegisterSessionAuthenticationStrategy
 * 새로운 새션을 만드는 것에 관한 전략
 * 4. CsrfAuthenticationStrategy
 * csrf 방어를 위해 csrfToken이 사라지더라도 다시 셋팅될 수 있도록 지원해주는 전략
 * 
 * 위의 4가지 전략을 정상적으로 수행하려면 target이 되는 SessionRegistry를 설정해주어야 합니다.
 * 먼저 SessionRegistry 구현체를 Application Context에 등록합니다.
 * SpringSessionBackedSessionRegistry는 클러스터 환경을 지원할 수 있습니다.
 * RedisIndexedSessionRepository 객체는 spring-session-data-redis 의존성을 추가하면
 * 자동으로 어플리케이션 로딩시 Application Context에 등록되므로 파라미터 주입이 가능합니다.
 * 
 * 참조 : https://blog.naver.com/PostView.naver?blogId=jieuni4u&logNo=222045853732
 */
@Configuration
public class RedisSessionConfig {

   @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> springSessionBackedSessionRegistry(
    		RedisIndexedSessionRepository redisIndexedSessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(redisIndexedSessionRepository);
    }
	
}