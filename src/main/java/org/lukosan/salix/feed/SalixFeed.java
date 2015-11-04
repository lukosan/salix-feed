package org.lukosan.salix.feed;

import java.util.List;

import org.lukosan.salix.SalixResource;
import org.lukosan.salix.SalixService;

public interface SalixFeed {

	/**
	 * -ve : shouldn't ever happen (but interpreted as 0)
	 *   0 : do not check
	 * +ve : minimum interval between checks, in minutes
	 */
	int getMinutesCheckInterval();
	
	List<SalixResource> process(SalixService salixService);
}