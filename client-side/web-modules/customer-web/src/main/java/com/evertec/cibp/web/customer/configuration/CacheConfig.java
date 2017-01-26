package com.evertec.cibp.web.customer.configuration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
	
	/**
	 * Type safe representation of application.properties
	 */
	@Autowired
	private ClusterConfigurationProperties clusterProperties;

	@Bean
	public JedisConnectionFactory redisConnectionFactory() {
		return new JedisConnectionFactory(new RedisClusterConfiguration(clusterProperties.getNodes()));
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory cf) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(cf);

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		// redisTemplate.setValueSerializer( new GenericToStringSerializer<
		// Object >( Object.class ) );
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

		// Number of seconds before expiration. Defaults to unlimited (0)
		cacheManager.setDefaultExpiration(300);
		return cacheManager;
	}

	@Bean
	public GenericObjectPoolConfig jedisPoolConfig() {

		GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
		genericObjectPoolConfig.setTestWhileIdle(true);
		genericObjectPoolConfig.setMinEvictableIdleTimeMillis(60000);
		genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		genericObjectPoolConfig.setNumTestsPerEvictionRun(-1);

		return genericObjectPoolConfig;

	}
}