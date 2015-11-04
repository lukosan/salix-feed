package org.lukosan.salix.feed;

import java.util.List;

import org.lukosan.salix.SalixResource;
import org.lukosan.salix.SalixService;

public interface SalixFeedProcessor {

	void process(List<SalixResource> resources, SalixService salixService);
	
}