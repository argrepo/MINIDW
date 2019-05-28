################################################################
###################### Function to Load Data into Staging Table
############################################################

staging_data_load <- function(
  DB_Connection_Name_Staging,
  DB_Connection_Name,
  Staging_SQL_Query,
  Staging_Table
){
  
  ## Checking the Database connectivity
  
  if(dbIsValid(DB_Connection_Name_Staging)==TRUE){
    print("Staging Database Connection is still Valid")
  } else {
    print("Staging Database Connection couldn't be Established or Closed Connection")
  }
  
  if(dbIsValid(DB_Connection_Name)==TRUE){
    print("Database Connection is still Valid")
  } else {
    print("Database Connection couldn't be Established or Closed Connection")
  }
  
  
  #### Getting the data for the defined Source SQL Query
  ## Supressing the warning messages of data types while reading data
  
  suppressWarnings(
    in_staging_source_data <- dbGetQuery(DB_Connection_Name,Staging_SQL_Query)
  )
  
  if(exists("in_staging_source_data") & nrow(in_staging_source_data)>0) {
    
    ### Getting the rows of the result set
    
    result_record_count <- nrow(in_staging_source_data)
    print(paste("The Record Count of Source Result Set is: ",result_record_count,sep=""))
    
    ### Printing the summary results of Data
    
    print("Source Data Summary")
    print(summary(in_staging_source_data))
    
    ### Loading the source data to the Staging Table for further transformations
    ### DataBase Table will be created based on the Column Names provided in the source Defintion
    ## Truncate and Load
    
    if(nrow(in_staging_source_data)>0) {
      dbWriteTable(DB_Connection_Name_Staging,Staging_Table,in_staging_source_data, row.names=FALSE, overwrite=TRUE)
    } else {print("Staging Source Query Result Set Contains Zero Records")}
    
    ### Getting the count of records loaded in the staging Table
    staging_record_count <- dbGetQuery(DB_Connection_Name_Staging,paste0("select count(*) from ",Staging_Table,sep=""))
    print(paste0("The Record Count of Staging Table is : ",staging_record_count))
    
    ### Checking whether all the records from the source result set is loaded or not
    if(result_record_count==staging_record_count) {
      print("All the records from the source result set are loaded into Staging Table")
    } else {
      print("Record Count Mismatch between Source Result Set and Staging Table")
    }
    
    return(nrow(in_staging_source_data))
    
  } else {
    print("Staging Source Query Result Set Contains Zero Records")
    #stop("Model Execution Stopped")
    return(nrow(in_staging_source_data))
  }
  
}

######################## End of the Function ######################