package org.lukosan.salix.autoconfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lukosan.salix.feed.SalixFeed;
import org.lukosan.salix.feed.SalixFeedManager;
import org.lukosan.salix.feed.SalixFeedProcessor;
import org.lukosan.salix.feed.SalixFeedSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConditionalOnProperty(prefix = "salix.feed", name = "enabled", matchIfMissing = true)
public class FeedAutoConfiguration {

	@Configuration
	@EnableScheduling
	public static class FeedConfiguration {
		
		private static final Log logger = LogFactory.getLog(FeedConfiguration.class);

		@Autowired(required=false)
		private List<SalixFeedSource> feeders = new ArrayList<SalixFeedSource>();
		
		@Autowired(required=false)
		private List<SalixFeedProcessor> processors = new ArrayList<SalixFeedProcessor>();

		@Bean
		public SalixFeedManager salixFeedManager() {
			List<SalixFeed> feeds = feeders.stream().flatMap(f -> f.getFeeds().stream()).collect(Collectors.toList());
			return new SalixFeedManager(feeds, processors);
		}

		@PostConstruct
		public void postConstruct() {
			if(logger.isInfoEnabled())
				logger.info("PostConstruct " + getClass().getSimpleName());
		}
	}
	
}