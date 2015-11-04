
package org.lukosan.salix.feed;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.lukosan.salix.SalixHandlerMapping;
import org.lukosan.salix.SalixResource;
import org.lukosan.salix.SalixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

public class SalixFeedManager {

	private static final Log logger = LogFactory.getLog(SalixFeedManager.class);
	
	@Autowired
	private SalixService salixService;
	
	@Autowired(required=false)
	private SalixHandlerMapping salixHandlerMapping;
	
	private Map<SalixFeed, LocalDateTime> checkLog;	
	private List<SalixFeedProcessor> processors;

	public SalixFeedManager(List<SalixFeed> feeds, List<SalixFeedProcessor> processors) {
		Assert.notNull(feeds, "Parameter [feeds] cannot be null");
		Assert.notNull(feeds, "Parameter [processors] cannot be null");
		this.checkLog = feeds.stream().collect(Collectors.toMap(f -> f, f -> LocalDateTime.now()));
		this.processors = processors;
	}

	@Scheduled(cron = "0 * * ? * *")
	public void checkFeeds() {
		LocalDateTime checkTime = LocalDateTime.now();

		if(logger.isDebugEnabled())
			logger.debug("Tick: " + checkTime);

		List<SalixResource> resources = new ArrayList<SalixResource>();
		
		for(Entry<SalixFeed, LocalDateTime> entry : checkLog.entrySet()) {
			if(entry.getKey().getMinutesCheckInterval() > 0 && (null == entry.getValue() || ChronoUnit.MINUTES.between(entry.getValue(), checkTime) > entry.getKey().getMinutesCheckInterval())) {
				entry.setValue(checkTime);
				resources.addAll(entry.getKey().process(salixService));
			}
		}
		
		if(! resources.isEmpty() && ! processors.isEmpty()) {
			processors.forEach(p -> p.process(resources, salixService));
			if(null != salixHandlerMapping)
				salixHandlerMapping.reloadHandlers();
		}
	}
}