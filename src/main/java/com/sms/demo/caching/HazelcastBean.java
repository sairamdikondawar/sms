package com.sms.demo.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

@Configuration
public class HazelcastBean {

	private static final Logger log = LoggerFactory.getLogger(HazelcastBean.class);

	@Bean
	public Config config() {
		Config config = new Config();
		return config;

	}

	@Bean
	public HazelcastInstance hazelcastInstance(Config config) {
		return Hazelcast.newHazelcastInstance(config);
	}

//	@Bean
//	public ClientConfig clientConfig() {
//		ClientConfig clientConfig = new ClientConfig();
//
//		return clientConfig;
//	}
//
//	@Bean
//	public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
//		return HazelcastClient.newHazelcastClient(clientConfig);
//	}

	@Bean(name = "stopMap")
	public IMap<String, String> getStopMap(HazelcastInstance hazelcastInstance) {
		return hazelcastInstance.getMap("stopMap");
	}
	
	@Bean(name = "dailyLimitMap")
	public IMap<String, Integer> getDailyLimitMap(HazelcastInstance hazelcastInstance) {
		return hazelcastInstance.getMap("dailyLimitMap");
	}

}
