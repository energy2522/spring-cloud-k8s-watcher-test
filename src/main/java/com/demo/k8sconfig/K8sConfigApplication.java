package com.demo.k8sconfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class K8sConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(K8sConfigApplication.class, args);
	}

	/**
	 * Prints all properties that were declared. This method is used for debugging purposes.
	 */
	@EventListener
	public void logProperties(ApplicationContextEvent applicationContextEvent) {
		log.info("**********************************************");
		var envs = (ConfigurableEnvironment) applicationContextEvent.getApplicationContext().getEnvironment();
		List<MapPropertySource> propertySourceList = new ArrayList<>();
		envs.getPropertySources().forEach(it -> {
			if (it instanceof MapPropertySource
					&& (it.getName().contains("applicationConfig") || it.getName().startsWith("Config resource"))) {
				propertySourceList.add((MapPropertySource) it);
			}
		});

		propertySourceList.stream()
				.map(ps -> ps.getSource().keySet())
				.flatMap(Collection::stream)
				.distinct()
				.sorted()
				.forEach(key -> log.info("{} = {}", key, envs.getProperty(key)));
		log.info("**********************************************");
	}

}
