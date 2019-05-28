package com.anvizent.scheduler.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

public class WildcardReloadableResourceBundleMessageSource extends org.springframework.context.support.ReloadableResourceBundleMessageSource {
	protected static final Log log = LogFactory.getLog(WildcardReloadableResourceBundleMessageSource.class);

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@Override
	public void setBasenames(String... basenames) {
		if (basenames != null) {
			List<String> baseNames = new ArrayList<String>();
			for (int i = 0; i < basenames.length; i++) {
				String basename = basenames[i];
				Assert.hasText(basename, "Basename must not be empty");
				try {
					Resource[] resources = resourcePatternResolver.getResources(basename.trim());
					for (int j = 0; j < resources.length; j++) {
						Resource resource = resources[j];
						String uri = resource.getURI().toString();
						String messageFile = null;

						// differ between file (contains the complete file path)
						// and class path resources
						if (resource instanceof FileSystemResource) {
							messageFile = "classpath:" + StringUtils.substringBetween(uri, "/classes", ".properties");
						} else if (resource instanceof ClassPathResource) {
							messageFile = StringUtils.substringBefore(uri, ".properties");
						}
						log.debug("Message properties file added: " + messageFile);
						baseNames.add(messageFile);
					}
				} catch (IOException e) {
					logger.debug("No message source files found for basename " + basename + ".");
				}
			}
			super.setBasenames(baseNames.toArray(new String[baseNames.size()]));
		}
	}
}
