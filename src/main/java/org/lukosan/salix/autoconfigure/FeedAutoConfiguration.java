package org.lukosan.salix.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lukosan.salix.feed.SalixFeed;
import org.lukosan.salix.feed.SalixFeedManager;
import org.lukosan.salix.feed.SalixFeedProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(prefix = "salix.feed", name = "enabled", matchIfMissing = true)
public class FeedAutoConfiguration {

	@Configuration
	@ConditionalOnClass({ Servlet.class })
	@ConditionalOnWebApplication
	@EnableScheduling
	public static class FeedConfiguration {
		
		private static final Log logger = LogFactory.getLog(FeedConfiguration.class);

		@Autowired(required=false)
		private List<SalixFeed> feeds = new ArrayList<SalixFeed>();
		
		@Autowired(required=false)
		private List<SalixFeedProcessor> processors = new ArrayList<SalixFeedProcessor>();

		@Bean
		public SalixFeedManager salixFeedManager() {
			return new SalixFeedManager(feeds, processors);
		}

		@PostConstruct
		public void postConstruct() {
			if(logger.isInfoEnabled())
				logger.info("PostConstruct " + getClass().getSimpleName());
		}
	}
	
}