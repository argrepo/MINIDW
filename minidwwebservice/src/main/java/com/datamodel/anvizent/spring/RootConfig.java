package com.datamodel.anvizent.spring;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.datamodel.anvizent.helper.Constants;
import com.datamodel.anvizent.security.CustomSecurityAuthenticationProvider;
import com.datamodel.anvizent.security.CustomSecurityUserDetailsServiceImpl;
import com.datamodel.anvizent.service.DataValidationService;
import com.datamodel.anvizent.service.DataValidationServiceImpl;
import com.datamodel.anvizent.service.FileService;
import com.datamodel.anvizent.service.FileServiceImpl;
import com.datamodel.anvizent.service.MigrationService;
import com.datamodel.anvizent.service.MigrationServiceImpl;
import com.datamodel.anvizent.service.PackageService;
import com.datamodel.anvizent.service.PackageServiceImpl;
import com.datamodel.anvizent.service.StandardPackageService;
import com.datamodel.anvizent.service.StandardPackageServiceImpl;
import com.datamodel.anvizent.service.dao.CommonDao;
import com.datamodel.anvizent.service.dao.DataValidationDao;
import com.datamodel.anvizent.service.dao.ETLAdminDao;
import com.datamodel.anvizent.service.dao.FileDao;
import com.datamodel.anvizent.service.dao.MigrationDao;
import com.datamodel.anvizent.service.dao.PackageDao;
import com.datamodel.anvizent.service.dao.QuartzDao;
import com.datamodel.anvizent.service.dao.ScheduleDao;
import com.datamodel.anvizent.service.dao.StandardPackageDao;
import com.datamodel.anvizent.service.dao.UserDao;
import com.datamodel.anvizent.service.dao.WebServiceDao;
import com.datamodel.anvizent.service.dao.impl.CommonDaoImpl;
import com.datamodel.anvizent.service.dao.impl.DataValidationDaoImpl;
import com.datamodel.anvizent.service.dao.impl.ETLAdminDaoImpl;
import com.datamodel.anvizent.service.dao.impl.FileDaoImpl;
import com.datamodel.anvizent.service.dao.impl.MigrationDaoImpl;
import com.datamodel.anvizent.service.dao.impl.PackageDaoImpl;
import com.datamodel.anvizent.service.dao.impl.QuartzDaoImpl;
import com.datamodel.anvizent.service.dao.impl.ScheduleDaoImpl;
import com.datamodel.anvizent.service.dao.impl.StandardPackageDaoImpl;
import com.datamodel.anvizent.service.dao.impl.UserDaoImpl;
import com.datamodel.anvizent.service.dao.impl.WebServiceDaoImpl;
import com.datamodel.anvizent.service.model.ClientDbCredentials;

/**
 * Root configuration.
 * 
 * define service impl, dao impl beans and other business related beans
 * 
 * @author rakesh.gajula
 * 
 */
@Configuration
@Import(AppProperties.class)
@ComponentScan(basePackages = { "com.datamodel.anvizent" })
@EnableWebMvc
public class RootConfig {
	protected final Log log = LogFactory.getLog(RootConfig.class);

	
	@Autowired
	private ClientDbCredentials clientDbCredentials;
	private @Value("${java.opt.params:}") String javaOptsParams;
	@Autowired
	@Qualifier("app_dataSource")
	private DataSource app_dataSource;

	public RootConfig() {
		log.debug("Root Configuration loaded.");
	}

	@Bean
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		return restTemplate; 
	}
 
	@Bean
	public UserDao userDao() {

		String appDbHost = clientDbCredentials.getHostname();
		String appDbPort = clientDbCredentials.getPortnumber();
		String appDbName = clientDbCredentials.getClientDbSchema();
		Constants.Config.JAVA_OPT_PARAMS = javaOptsParams;
		return new UserDaoImpl(app_dataSource, clientDbCredentials.getClientDbUsername(), 
				clientDbCredentials.getClientDbPassword(), appDbHost, appDbPort, appDbName);
	}

	@Bean
	public QuartzDao quartzDaoImpl() {
		log.info("in quartzDao creation ");
		return new QuartzDaoImpl(app_dataSource);
	}

	@Bean
	public CommonDao commonDaoImpl() {
		log.info("in commondao creation");
		return new CommonDaoImpl(app_dataSource);
	}

	@Bean
	public PackageDao packageDao() {
		return new PackageDaoImpl(app_dataSource);
	}

	@Bean
	public JdbcTemplate getCommonJdbcTemplate() {
		return new JdbcTemplate(app_dataSource);
	}

	@Bean
	public WebServiceDao webServiceDao() {
		return new WebServiceDaoImpl(app_dataSource);
	}


	@Bean
	public CustomSecurityUserDetailsServiceImpl customSecurityUserDetailsServiceImpl() {
		CustomSecurityUserDetailsServiceImpl customSecurityUserDetailsServiceImpl = null;
		try {
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}

		return customSecurityUserDetailsServiceImpl;

	}

	@Bean
	public CustomSecurityAuthenticationProvider customSecurityAuthenticationProvider() {

		CustomSecurityAuthenticationProvider customSecurityAuthenticationProvider = null;
		try {
			customSecurityAuthenticationProvider = new CustomSecurityAuthenticationProvider(
					customSecurityUserDetailsServiceImpl());
		} catch (Exception ex) {
			log.debug(ex.getMessage());
		}
		return customSecurityAuthenticationProvider;

	}

	@Bean
	public ScheduleDao scheduleDao() {
		return new ScheduleDaoImpl();
	}

	@Bean
	public FileService fileService() {
		// Initializing S3 connection details
		FileService fileService = new FileServiceImpl(fileDao());
		return fileService;
	}

	@Bean
	public PackageService packageService() {
		// Initializing S3 connection details
		PackageService packageService = new PackageServiceImpl(packageDao());
		return packageService;
	}
	

	@Bean
	public MigrationService migrationService() {
		return new MigrationServiceImpl(migrationDao());
	}

	@Bean
	public MigrationDao migrationDao() {
		return new MigrationDaoImpl();
	}

	@Bean
	public FileDao fileDao() {
		return new FileDaoImpl();
	}

	@Bean
	public ETLAdminDao etlAdminDao() {
		return new ETLAdminDaoImpl(app_dataSource);
	}
	
	@Bean
	public StandardPackageDao standardPackageDao(){
		return new StandardPackageDaoImpl(app_dataSource);
	}
	
	@Bean 
	public StandardPackageService standardPackageService(){
		StandardPackageService  standardPackageService= new StandardPackageServiceImpl(standardPackageDao());
		return standardPackageService;
	}
	
	@Bean
    public DataValidationDao dataValidationDao(){
		return new DataValidationDaoImpl();
	}
	
	@Bean
	public DataValidationService dataValidationService(){
		DataValidationService dataValidationService = new DataValidationServiceImpl(dataValidationDao());
		return dataValidationService;
	}
	
}
