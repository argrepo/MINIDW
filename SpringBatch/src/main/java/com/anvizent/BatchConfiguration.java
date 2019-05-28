package com.anvizent;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

 

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

 @Autowired
 public JobBuilderFactory jobBuilderFactory;
 
 @Autowired
 public StepBuilderFactory stepBuilderFactory;
 
 @Autowired
 public DataSource dataSource;
 
 @Bean
 public DataSource dataSource() {
  final DriverManagerDataSource dataSource = new DriverManagerDataSource();
  dataSource.setDriverClassName("com.mysql.jdbc.Driver");
  dataSource.setUrl("jdbc:mysql://192.168.0.131:4475/springbatch");
  dataSource.setUsername("almadmin");
  dataSource.setPassword("Ewc@4fvQ#pT5");
  
  return dataSource;
 }
 
 @Bean
 public FlatFileItemReader<User> reader(){
  FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
  reader.setResource(new ClassPathResource("users.csv"));
  reader.setLineMapper(new DefaultLineMapper<User>() {{
   setLineTokenizer(new DelimitedLineTokenizer() {{
    setNames(new String[] { "name" });
   }});
   setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
    setTargetType(User.class);
   }});
   
  }});
  
  return reader;
 }
 
 @Bean
 public UserItemProcessor processor(){
  return new UserItemProcessor();
 }
 
 @Bean
 public JdbcBatchItemWriter<User> writer(){
  JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
  writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
  writer.setSql("INSERT INTO user(name) VALUES (:name)");
  writer.setDataSource(dataSource);
  
  return writer;
 }
 
 @Bean
 public Step step1() {
  return stepBuilderFactory.get("step1").<User, User> chunk(1000)
    .reader(reader())
    .processor(processor())
    .writer(writer())
    .build();
 }
 
 @Bean
 public Job importUserJob() {
  return jobBuilderFactory.get("importUserJob")
    .incrementer(new RunIdIncrementer())
    .flow(step1())
    .end()
    .build();
 }
 
}