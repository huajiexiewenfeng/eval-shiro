package com.huajie.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis缓存配置类
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	@Autowired
	JedisConfig jedisConfig;
	@Autowired
    JedisConnectionFactory jedisConnectionFactory;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory (){
		RedisStandaloneConfiguration rf=new RedisStandaloneConfiguration();
		rf.setDatabase(jedisConfig.database);
		rf.setHostName(jedisConfig.host);
		rf.setPort(jedisConfig.port);
		rf.setPassword(RedisPassword.of(jedisConfig.password));
		int to=Integer.parseInt(jedisConfig.timeout.substring(0,jedisConfig.timeout.length()-2));
		//JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
		//jedisClientConfiguration.connectTimeout(Duration.ofMillis(to));
		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpb=
				(JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(jedisConfig.maxIdle);
		jedisPoolConfig.setMinIdle(jedisConfig.minIdle);
		jedisPoolConfig.setMaxTotal(jedisConfig.maxActive);
		int l=Integer.parseInt(jedisConfig.maxWait.substring(0,jedisConfig.maxWait.length()-2));
		jedisPoolConfig.setMaxWaitMillis(l);
		jpb.poolConfig(jedisPoolConfig);
		JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(rf,jpb.build());
		return jedisConnectionFactory;
	}

	@Bean
	public RedisTemplate redisTemplate(){
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory);
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key采用String的序列化方式
		template.setKeySerializer(stringRedisSerializer);
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(stringRedisSerializer);
		// value序列化方式采用jackson
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// hash的value序列化方式采用jackson
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.afterPropertiesSet();
		return template;
	}

}