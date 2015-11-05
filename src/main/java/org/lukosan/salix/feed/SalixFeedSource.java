package org.lukosan.salix.feed;

import java.util.List;
import java.util.stream.Collectors;

public class SalixFeedSource {

	private List<SalixFeed> feeds;
	
	public SalixFeedSource(List<? extends SalixFeed> feeds) {
		this.feeds = feeds.stream().map(f -> (SalixFeed) f).collect(Collectors.toList());
	}
	
	public List<SalixFeed> getFeeds() {
		return feeds;
	}
}
