#################################################################
################### Data Summary Report Generation ################

data_quality_report <- function(DB_Connection_Name,Staging_Table_Name) {
  
  
  ###### Sourcing the dependant data summary functions
  
 source(paste0(user_defined_functions_path,"basic_data_summary_report.R",sep=""))
 source(paste0(user_defined_functions_path,"missing_data_pattern_report.R",sep=""))
 source(paste0(user_defined_functions_path,"numerical_data_summary_report.R",sep=""))
 source(paste0(user_defined_functions_path,"string_data_summary_report.R",sep=""))
 source(paste0(user_defined_functions_path,"data_skewness_report.R",sep=""))
 source(paste0(user_defined_functions_path,"data_kurtosis_report.R",sep=""))
  
  ###### Creating a list object to store all the summaries
  
  data_quality_report_list <- list()
  
  ###### Checking the Database connectivity
  
  if(dbIsValid(DB_Connection_Name)==TRUE){
    
    print("Database Connection is still Valid")
    
  } else {
    
    print("Database Connection couldn't be Established or Closed Connection")
    
    print("Try Reconnecting to the Database")
    
    
  }
  
  
  ###### Reading the data from the staging Table
  
  in_data <- dbGetQuery(DB_Connection_Name,paste0("select * from ",Staging_Table_Name,sep=""))
  
  ###### Checking the count of records
  
  if(nrow(in_data)>0){
    
    cat("\n")
    
    print(paste0("The Record Count in Staging Table: ", nrow(in_data)))
    
    cat("\n")
    
    ### Basic R Imported Data Object Info
    
    OBJECT_SIZE_KB <- round(as.numeric(object.size(in_data)/1024),2)
    
    ROW_COUNT <- nrow(in_data)
    
    COLUMN_COUNT <- ncol(in_data)
    
    basic_object_info <- as.data.frame(cbind(OBJECT_SIZE_KB,ROW_COUNT,COLUMN_COUNT))
    
    row.names(basic_object_info) <- NULL
    
    print("Basic Data Object Information")
    
    print(basic_object_info)
    
    data_quality_report_list[["basic_data_object_info"]] <- basic_object_info
    
    cat("\n")
    
    ### Previewing the Data Object
    
    print("Preview of the Data Object")
    
    print(head(in_data))
    
    data_quality_report_list[["preview_data"]] <- head(in_data)
    
    cat("\n")
    
    ########## Basic Data Summary Report
    
    print("Basic Data Summary Report")
    
    basic_data_summary_report(in_data)
    
    data_quality_report_list[["basic_data_summary_report"]] <- basic_data_summary_report(in_data)
    
    cat("\n")
    
    ########## Missing Data Pattern Report
    
    print("Missing Data Pattern Report")
    
    missing_data_pattern_report(in_data)
    
    data_quality_report_list[["missing_data_pattern_report"]] <- missing_data_pattern_report(in_data)
    
    cat("\n")
    
    ############ Numerical Data Summary Report
    
    print("Numerical Data Summary Report")
    
    numerical_data_summary_report(in_data)
    
    data_quality_report_list[["numerical_data_summary_report"]] <- numerical_data_summary_report(in_data)
    
    cat("\n")
    
    ############ String Data Summary Report
    
    print("String Data Summary Report")
    
    string_data_summary_report(in_data)
    
    data_quality_report_list[["string_data_summary_report"]] <- string_data_summary_report(in_data)
    
    
  } else {
    
    print("The Staging Table doesn't contain any Records")
    
    print("Please check and Load Data into the Table for Summary Analysis")
    
  }
  
  return(data_quality_report_list)
  
}

################ End of the Function #########################################