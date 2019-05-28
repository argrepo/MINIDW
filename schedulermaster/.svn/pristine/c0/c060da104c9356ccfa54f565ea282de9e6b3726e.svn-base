package com.anvizent.scheduler.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import com.anvizent.client.data.to.csv.path.converter.constants.Constants;
import com.anvizent.minidw.service.utils.MinidwServiceUtil;
import com.anvizent.scheduler.master.MasterQuartzScheduler;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.model.DataResponse;
import com.datamodel.anvizent.service.model.Message;
import com.datamodel.anvizent.service.model.SchedulerMaster;

/**
 * Root configuration.
 * 
 * master scheduler start up , while server starting
 * 
 * @author mahender.alaveni
 * 
 */
@Configuration
@Import(AppProperties.class)
@ComponentScan(basePackages = { "com.anvizent.scheduler" })
@EnableWebMvc
public class RootConfig
{
	protected final Log log = LogFactory.getLog(RootConfig.class);

	private @Value("${scheduler.master.id:0}") String masterId;

	@Autowired
	MasterQuartzScheduler masterQuartzScheduler;

	@Autowired
	public CommonDao commonDao;

	public RootConfig()
	{
		log.debug("Root Configuration loaded.");
	}

	@Bean
	DataResponse startMaster()
	{
		DataResponse dataResponse = new DataResponse();
		Message message = new Message();

		try
		{
			if( !masterQuartzScheduler.isSchedulerRunning() )
			{
				if( Integer.valueOf(masterId) > 0 )
				{
					SchedulerMaster schedulerMasterInfo = commonDao.getSchedulerMasterById(Integer.valueOf(masterId));
					if( schedulerMasterInfo != null )
					{
						message.setCode(Constants.Config.SUCCESS);
						log.error("Master started.");
						masterQuartzScheduler.setSchedulerMaster(schedulerMasterInfo);
						masterQuartzScheduler.startScheduler();
						dataResponse.setObject(masterQuartzScheduler.isSchedulerRunning());
					}
					else
					{
						log.error("Server master details not found.");
						message.setCode(Constants.Config.ERROR);
						message.setCode("Server master details not found.");
					}
				}
				else
				{
					log.error("Master Id should be greater than zero.");
				}
			}
			else
			{
				message.setCode(Constants.Config.ERROR);
				message.setText("Scheduler already started.");
				log.error("Server master details not found.");
			}
		}
		catch ( Throwable e )
		{
			log.error("Error in master start up : " + e.getCause().getMessage());
			MinidwServiceUtil.addErrorMessage(message, e);
		}

		dataResponse.addMessage(message);
		return dataResponse;
	}
}
