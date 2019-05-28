package com.anvizent.packagerunner.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.model.SchedulerSlave;

/**
 * Root configuration.
 * 
 * slave start up , while server starting
 * 
 * @author mahender.alaveni
 * 
 */
@Configuration
@Import(AppProperties.class)
@ComponentScan(basePackages = { "com.anvizent.packagerunner" })
@EnableWebMvc
public class RootConfig
{
	protected final Log log = LogFactory.getLog(RootConfig.class);

	private @Value("${scheduler.slave.id:0}") String slaveId;

    @Autowired
	public CommonDao commonDao;

	public RootConfig()
	{
		log.debug("Root Configuration loaded.");
	}

    @Bean
    SchedulerSlave startSlave()
	{
		log.debug("in startSlave()");
		SchedulerSlave schedulerSlave;
		try
		{
			if( Integer.valueOf(slaveId) > 0 )
			{
				schedulerSlave = commonDao.getServerSlaveById(Integer.valueOf(slaveId));

				if( schedulerSlave != null )
				{
					return schedulerSlave;
				}
				else
				{
					log.error("slave details not found.");
				}
				log.error("Slave started.");
			}
			else
			{
				log.error("Slave Id should be greater than zero.");
			}
			
		}
		catch ( Throwable e )
		{
			log.error("error while startSlave() ", e);
		}
		 
		return null;
	} 
}
