#######################################################
################ Sales Forecast Generic Code
############################################################

#######################################################################################
############## Clearing the Plots and existing Objects
#######################################################################################
options(repos=structure(c(CRAN="https://cran.rstudio.com/")))

rm(list=ls())

##########################################################################

#################################################################
##################### Input Parameters
#################################################################

args <- commandArgs(TRUE)

parameter_file_name <- args[1]


#param_input <- read.csv("param.csv",header=TRUE,stringsAsFactors = FALSE)

##### Reading the Parameter File
param_input <- read.csv(parameter_file_name,header=TRUE,stringsAsFactors = FALSE)

print("Preview of the Parameter File")
print(param_input)

client <- as.character(param_input$VALUE[param_input$PARAM=="client"])
app_db_host <- as.character(param_input$VALUE[param_input$PARAM=="app_db_host"])
app_db_port <- as.integer(as.character(param_input$VALUE[param_input$PARAM=="app_db_port"]))
app_db_name <- as.character(param_input$VALUE[param_input$PARAM=="app_db_name"])
app_db_user_name <- as.character(param_input$VALUE[param_input$PARAM=="app_db_user_name"])
app_db_password <- as.character(param_input$VALUE[param_input$PARAM=="app_db_password"])
staging_db_host <- as.character(param_input$VALUE[param_input$PARAM=="staging_db_host"])
staging_db_port <- as.integer(as.character(param_input$VALUE[param_input$PARAM=="staging_db_port"]))
staging_db_name <- as.character(param_input$VALUE[param_input$PARAM=="staging_db_name"])
staging_db_user_name <- as.character(param_input$VALUE[param_input$PARAM=="staging_db_user_name"])
staging_db_password <- as.character(param_input$VALUE[param_input$PARAM=="staging_db_password"])
db_host <- as.character(param_input$VALUE[param_input$PARAM=="db_host"])
db_port <- as.integer(as.character(param_input$VALUE[param_input$PARAM=="db_port"]))
db_name <- as.character(param_input$VALUE[param_input$PARAM=="db_name"])
db_user_name <- as.character(param_input$VALUE[param_input$PARAM=="db_user_name"])
db_password <- as.character(param_input$VALUE[param_input$PARAM=="db_password"])
log_file_path <- as.character(param_input$VALUE[param_input$PARAM=="log_file_path"])
user_defined_functions_path <- as.character(param_input$VALUE[param_input$PARAM=="user_defined_functions_path"])


Business_Problem <- as.character(param_input$VALUE[param_input$PARAM=="Business Problem"])
Model_Name <- as.character(param_input$VALUE[param_input$PARAM=="Model Name"])
model_data_frequency <- as.integer(param_input$VALUE[param_input$PARAM=="model_data_frequency"])
current_year <- as.integer(param_input$VALUE[param_input$PARAM=="current_year"])
differencing_order <- as.integer(param_input$VALUE[param_input$PARAM=="differencing_order"])
desired_forecast_period <- as.integer(param_input$VALUE[param_input$PARAM=="desired_forecast_period"])
data_points_criteria <- as.integer(param_input$VALUE[param_input$PARAM=="data_points_criteria"])

###################################################################################
######################## Creating a Log File with the entire console execution
#############################################################################################

timestamp <- gsub(pattern=" ","",gsub(pattern = "[[:punct:]]","",as.character(Sys.time())))

log_file_name <- paste(client,"_",gsub(pattern = " ","_",Business_Problem),'_',gsub(pattern = " ","_",Model_Name),"_",timestamp,".log",sep="")

#### Sinking the console output to the Log File
sink(paste(log_file_path,"/",log_file_name,sep=""))

print(paste0("Log File Name: ",log_file_name,sep=""))

### Checking the System Resources and Memory

#system('wmic OS get TotalVisibleMemorySize /Value')

#system('wmic OS get FreePhysicalMemory /Value')

#Capturing the system Start Time

start_time <- Sys.time()

####################################################################################
###################### Installing the required Packages and loading the libraries
#################################################################################

if(!require("nnfor")) install.packages("nnfor"); library("nnfor")
if(!require("DMwR")) install.packages("DMwR"); library("DMwR")
if(!require("plyr")) install.packages("plyr"); library("plyr")
if(!require("DBI")) install.packages("DBI"); library("DBI")
if(!require("RMySQL")) install.packages("RMySQL"); library("RMySQL")
if(!require("dplyr")) install.packages("dplyr"); library("dplyr")
if(!require("forecast")) install.packages("forecast"); library("forecast")
if(!require("sqldf")) install.packages("sqldf"); library("sqldf")
if(!require("data.table")) install.packages("data.table"); library("data.table")
if(!require("forecast")) install.packages("forecast"); library("forecast")
if(!require("readr")) install.packages("readr"); library("readr")
if(!require("Metrics")) install.packages("Metrics"); library("Metrics")
if(!require("smooth")) install.packages("smooth"); library("smooth")
if(!require("mice")) install.packages("mice"); library("mice")
if(!require("stringr")) install.packages("stringr", type="source"); library("stringr")


###################### Setting the default options #################

###### setting default driver of sqldf library to SQLite
options(sqldf.driver = "SQLite")

###### Turning off the scientific Notation of Numbers
options(scipen = 999)

##################################################################################
############# Load the user defined functions ##################
##################################################################################

#source(paste0(user_defined_functions_path,"\\","clear_plots.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","clear_workspace.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","connect_mysql_db.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","get_AI_Model_Metadata.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","staging_data_load.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","data_quality_report.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","missing_data_pattern.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","numerical_data_summary.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","string_data_summary.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","data_summary_report.R",sep=""))
#source(paste0(user_defined_functions_path,"\\","random_forest_imputation.R",sep=""))


####################################################################
################### connect_mysql_db function ######################
##########################################################################

connect_mysql_db <- function(db_host,
                             db_port,
                             db_name,
                             db_user_name,
                             db_password) {
  
  ########### Establishing the DB Connection 
  
  mysql_db_conn <-  dbConnect(MySQL(), 
                              user=db_user_name, 
                              password=db_password, 
                              dbname=db_name, 
                              host=db_host, 
                              port=db_port)
  
  
  ########### Checking the DB connection
  
  if(dbIsValid(mysql_db_conn)==TRUE){
    print(paste0("DB Connection to ", db_name," Successfully Established"))
  } else {
    print(paste0("DB Connection to ", db_name," couldn't be Established"))
    print("Please check the Input DB Parameters")
  }
  
  return(mysql_db_conn)
  
}


########################## End of the Function ##############################

#########################################################
############## R Script to Initiate the Model Execution

get_AI_Model_Metadata <- function(DB_Connection_Name,Business_Problem,Model_Name) {
  
  ## Checking the Database connectivity
  
  if(dbIsValid(DB_Connection_Name)==TRUE){
    print("Database Connection is still Valid")
  } else {
    print("Database Connection couldn't be Established or Closed Connection")
  }
  
  ### Reading the MetaData for the Model
  
  in_model_table_metadata <- dbGetQuery(DB_Connection_Name,
                                        paste0("select AI_Staging_Table as STAGING_TABLE, AI_IL_Table as AIL_TABLE,
                                                AI_OL_Table as AOL_TABLE from AI_Tables_Master
                                               where BUSINESS_PROBLEM=\"",Business_Problem,"\"",sep=""))
  
  ### Printing the Table Names for the Model Execution
  
  cat("\n")
  in_staging_table_name <- in_model_table_metadata$STAGING_TABLE
  in_AIL_table_name <- in_model_table_metadata$AIL_TABLE
  in_AOL_table_name <- in_model_table_metadata$AOL_TABLE
  
  
  print(paste0("The Staging Table Name for the ",Model_Name," is: ", in_staging_table_name,sep=""))
  print(paste0("The AIL Table Name for the ",Model_Name," is: ", in_AIL_table_name,sep=""))
  print(paste0("The AOL Table Name for the ",Model_Name," is: ", in_AOL_table_name,sep=""))
  
  ### Getting the Source Definition SQL Query for the Staging Table Load
  
  in_staging_sql_query <- dbGetQuery(DB_Connection_Name,
                                     paste0("select SOURCE_QUERY from AI_Source_Definition
                                            where STAGING_TABLE=\"", in_staging_table_name,"\"",sep=""))
  
  in_staging_sql_query <- in_staging_sql_query$SOURCE_QUERY
  
  cat("\n")
  print("Staging Source SQL Query")
  print(in_staging_sql_query)
  
  ### Getting the Statistical Transformation Rules for the Staging Table
  
  in_transformation_rules <- dbGetQuery(DB_Connection_Name,
                                        paste0("select a.DATA_VARIABLE,a.AI_DATA_TYPE,a.RULE_ID,b.R_FUNCTION,b.R_FUNCTION,a.EXECUTION_ORDER from 
                                               AI_Statistical_Data_Transformations a
                                               inner join AI_Statistical_Data_Transformation_Rules b
                                               on a.RULE_ID=b.RULE_ID
                                               where a.STAGING_TABLE=\"",in_staging_table_name,
                                               "\" order by a.DATA_VARIABLE,a.EXECUTION_ORDER",sep=""))
  
  ### Checking whether transformation Rules are defined or not
  
  if(nrow(in_transformation_rules)>0){
    cat("\n")
    print("Transformation Rules are defined for the Staging Table")
    print(paste0("Total Number of Transformation Rules: ",nrow(in_transformation_rules)))
  } else {
    cat("\n")
    print("Transformation Rules are not defined for the Staging Table")
  }
  
  AI_Metadata <- cbind(cbind(in_model_table_metadata,in_staging_sql_query))
  return(AI_Metadata)
  
}


###################### End of the Function ##############################################

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

######################################################################
######################## Data Quality Report Generator ##############
############################################################

############## Get all column Names

basic_data_summary_report <- function(input_dataframe) {
  
  basic_data_summary_report <- list()
  
  column_names <- colnames(input_dataframe)
  
  print("******************************************************************",quote = FALSE)
  
  print("LIST OF COLUMNS", quote = FALSE)
  
  cat("\n")
  
  print(column_names)
  
  basic_data_summary_report[["column_names"]] <- column_names
  
  ######### Getting the column classes
  
  column_class <- as.data.frame(sapply(input_dataframe, class))
  
  colnames(column_class) <- c("Column_vs_DataType")
  
  column_class <- as.data.frame(t(column_class))
  
  rownames(column_class) <- NULL
  
  basic_data_summary_report[["column_vs_datatype"]] <- column_class
  
  ########## Displaying the summary of all columns
  
  column_class <- as.data.frame(sapply(input_dataframe, class))
  
  colnames(column_class) <- c("Column_vs_DataType")
  
  column_class$Column_vs_DataType <- as.factor(column_class$Column_vs_DataType)
  
  cat("\n")
  
  print("DATA TYPE COLUMN SUMMARY",quote = FALSE)
  
  data_type_frequency <- as.data.frame(table(column_class))
  
  print(data_type_frequency,quote = FALSE)
  
  basic_data_summary_report[["data_type_frequency"]] <- data_type_frequency
  
  unique_class <- unique(sapply(input_dataframe, class))
  
  unique_class <- as.data.frame(unique_class)
  
  basic_data_summary_report[["distinct_datatypes"]] <- unique_class
  
  ############# Displaying the column Names for each individual Data Type
  
  unique_class <- unique(sapply(input_dataframe, class))
  
  in_list <- list()
  
  for (i in 1:length(unique_class))
    
  {
    
    Column_Data_Type <- unique_class[i]
    
    cat("\n")
    
    print(toupper(paste("COLUMN DATA TYPE: ",Column_Data_Type,sep="")),quote = FALSE)
    
    print(row.names(subset(column_class,column_class$Column_vs_Data_Type==Column_Data_Type)),quote = FALSE)
    
    col_names <- paste(row.names(subset(column_class,column_class$Column_vs_Data_Type==Column_Data_Type)),
                       collapse=",")
    column_diversity <- cbind(Column_Data_Type,col_names)
    
    in_list[[i]] <- column_diversity
    
  }
  
  column_diversity <- as.data.frame(do.call(rbind, in_list))
  
  colnames(column_diversity) <- c("DATA_TYPE","COLUMN_NAMES")
  
  basic_data_summary_report[["column_diversity"]] <- column_diversity
  
  ############# Record Count Summary 
  
  Total_Records <- nrow(input_dataframe)
  
  Unique_Records <- nrow(unique(input_dataframe))
  
  Duplicate_Records <- ifelse(is.null(nrow(duplicated(input_dataframe))),0,nrow(duplicated(input_dataframe)))
  
  Record_Count_Summary <- as.data.frame(cbind(Total_Records,Unique_Records,Duplicate_Records))
  
  basic_data_summary_report[["record_count_summary"]] <- Record_Count_Summary
  
  cat("\n")
  
  print(Record_Count_Summary,quote = FALSE)
  
  print("******************************************************************",quote = FALSE)
  
  return(basic_data_summary_report)
  
  
}


###################### End of the Function #######################


######################################################################################
################################# Function to get Missing Values Pattern in a data frame ###########
##############################################################################

missing_data_pattern_report <- function(input_dataframe){
  
  if(!require("mice")) install.packages("mice"); library(mice)
  
  missing_data_details <- md.pattern(input_dataframe)
  
  return(missing_data_details)
  
  print(missing_data_details)
  
}


#############################################################
#########################################
############### Data Skewness Report
################################################

data_skewness_report <- function(input_column) {
  
  ########### Loading required R Packages
  
  if(!require("e1071")) install.packages("e1071"); library("e1071")
  
  if(!require("moments")) install.packages("moments"); library("moments")
  
  ################# Custom Functions for Skewness Analysis##################
  ############################ Skewness Strength #####################
  
  data_skewness_strength <- function(skewness_value){
    
    if (is.nan(skewness_value)) {
      
      Skewness_Strength <- "NaN" 
      
    } else if (skewness_value<=-1 | skewness_value >=1) {
      
      print("The Input Data Variable is Heavily Skewed")
      
      Skewness_Strength <- "Heavily Skewed"
      
    } else if ((skewness_value>-1 & skewness_value<=-0.5) | (skewness_value>=0.5 & skewness_value<1)) {
      
      print("The Input Data Variable is Moderately Skewed")
      
      Skewness_Strength <- "Moderately Skewed"
      
    } else if ((skewness_value>-0.5 & skewness_value<0) | (skewness_value>0 & skewness_value<0.5)) {
      
      print("The Input Data Variable is Approximately Skewed")
      
      Skewness_Strength <- "Approximately Skewed"
      
    } else if (skewness_value==0) {
      
      print("The Input Data Variable is Symmetrical")
      
      Skewness_Strength <- "Symmetrical"
      
    } else {
      
      print("Exception: Skewness is unknown")
      
      print("Please provide a valid Input Data Variable")
      
      Skewness_Strength <- "unknown"
      
    }
    
    return(Skewness_Strength)
    
  }
  
  ################################### Skewness Type ##############################
  
  data_skewness_type <- function(skewness_value) {
    
    if (is.nan(skewness_value)) {
      
      skewness_type <- "NaN"
      Recommended_Transformations <- "NaN"
      
    } else if(skewness_value==0) {
      
      print("No Transformations are required as distribution is symmetrical")
      
      skewness_type <- "Symmetrical"
      Recommended_Transformations <- "Not Required"
      
    } else if (skewness_value<0) {
      
      print("The Skewness is of Type:Negative/Left")
      
      print("Recommended Data Transformations:Square Root/Cube Root/Log(Base can be 10,2,e)")
      
      skewness_type <- "Negative/Left"
      Recommended_Transformations <- "sqrt/cuberoot/log(10,e,2)"
      
    } else if (skewness_value>0) {
      
      print("The Skewness is of Type:Positive/Right")
      
      print("Recommended Data Transformations:Square/Cube Root/Log (Base can be 10,2,e)")
      
      skewness_type <- "Positive/Right"
      Recommended_Transformations <- "square/cuberoot/log(10,e,2)"
      
    } else {
      
      print("Please Provide a Valid Skewness Value")
      
      skewness_type <- "unknown"
      Recommended_Transformations <- "unknown"
      
    }
    
    return(cbind(skewness_type,Recommended_Transformations))
  }
  
  ######################### Completed the Skewness Analysis Functions ##########
  ################################################################################
  
  ########## Checking the class of the input columns
  
  if (class(input_column) %in% c('numeric','integer')) {
    
    out_skewness_value <- round(skewness(input_column,na.rm = TRUE),4)
    
    print(paste("The Skewness Value is:",out_skewness_value))
    
    skewness_strength <- data_skewness_strength(out_skewness_value)
    
    skewness_type <- data_skewness_type(out_skewness_value) 
    
  } else if (!class(input_column) %in% c('numeric','integer')) {
    
    print("Input Data Variable is not of Valid Type: Integer/Numeric")
    
    skewness_strength <- "Non Numeric"
    
    skewness_type <- "unknown"
    
  } else {
    
    print("Please Provide a Valid Data Variable: Unknown Exception")
    
    skewness_strength <- "unknown Exception"
    
    skewness_type <- "unknown Exception"
    
  }
  
  return(cbind(out_skewness_value,skewness_strength,skewness_type))
}


################################ End of the Function #####################################

#########################################
############### Data kurtosis Report
################################################

data_kurtosis_report <- function(input_column) {
  
  ########### Loading required R Packages
  
  if(!require("e1071")) install.packages("e1071"); library("e1071")
  
  if(!require("moments")) install.packages("moments"); library("moments")
  
  ################# Custom Functions for Skewness Analysis##################
  ############################ Skewness Strength #####################
  
  data_kurtosis_type <- function(kurtosis_value){
    
    if (is.nan(kurtosis_value)) {
      
      Kurtosis_Type <- "NaN" 
      
    } else if (kurtosis_value==0) {
      
      print("Kurtosis is of Type: Normal/Mesokurtic")
      
      Kurtosis_Type <- "Normal/Mesokurtic"
      
    } else if (kurtosis_value < 0) {
      
      print("Kurtosis is of Type: LightTail/Platykurtic")
      
      Kurtosis_Type <- "LightTail/Platykurtic"
      
    } else if (kurtosis_value > 0) {
      
      print("Kurtosis is of Type: HeavyTail/Leptokurtic")
      
      Kurtosis_Type <- "HeavyTail/Leptokurtic"
      
    } else {
      
      print("Exception: Kurtosis is unknown")
      
      print("Please provide a valid Input Data Variable")
      
      Kurtosis_Type <- "unknown"
      
    }
    
    return(Kurtosis_Type)
    
  }
  
  ################################################################################
  ########## Checking the class of the input columns
  
  if (class(input_column) %in% c('numeric','integer')) {
    
    out_kurtosis_value <- round(kurtosis(input_column,na.rm = TRUE),4)
    
    print(paste("The Kurtosis Value is:",out_kurtosis_value))
    
    Kurtosis_Type <- data_kurtosis_type(out_kurtosis_value) 
    
  } else if (!class(input_column) %in% c('numeric','integer')) {
    
    print("Input Data Variable is not of Valid Type: Integer/Numeric")
    
    Kurtosis_Type <- "NaN"
    
  } else {
    
    print("Please Provide a Valid Data Variable: Unknown Exception")
    
    Kurtosis_Type <- "unknown"
    
  }
  
  return(cbind(out_kurtosis_value,Kurtosis_Type))
  
}


################################ End of the Function #####################################


########################################################################
################ Numerical Summary Report
###########################################

numerical_data_summary_report <- function(input_dataframe) {
  
  ############# Loading the skewness Analysis Functions
  
  #source("data_skewness_report.R")
  #source("data_kurtosis_report.R")
  
  ######################### Numerical Summary Functions #####################################
  ##########################################################################################
  
  na_count <- function(input) {
    sum(is.na(input))
  }
  
  
  zero_count <- function(input) {
    sum(input[!is.na(input)]==0)
  }
  
  positive_count <- function(input) {
    sum(input[!is.na(input)]>0)
  }
  
  negative_count <- function(input) {
    sum(input[!is.na(input)]<0)
  }
  
  
  mean_value <- function(input) {
    mean(input,na.rm = TRUE)
  }
  
  median_value <- function(input) {
    median(input,na.rm = TRUE)
  }
  
  min_value <- function(input) {
    min(input,na.rm = TRUE)
  }
  
  max_value <- function(input) {
    max(input,na.rm = TRUE)
  }
  
  sum_value <- function(input) {
    sum(input,na.rm = TRUE)
  }
  
  quantile_value <- function(input) {
    quantile(input,probs = c(0.05,0.95),na.rm = TRUE)
  }
  
  variance_value <- function(input) {
    var(input,na.rm = TRUE)
  }
  
  sd_value <- function(input) {
    sd(input,na.rm = TRUE)
  }
  
  ########################## Loading the required R Packages
  
  if(!require("dplyr")) install.packages("dplyr"); library(dplyr)
  if(!require("plyr")) install.packages("plyr"); library("plyr")
  
  ## Selecting only the numerical columns from the input data frame
  
  numerical_data <- select_if(input_dataframe,is.numeric)
  
  #### Calculating required numerical summaries
  
  ############## NA Count
  NA_COUNT <- sapply(numerical_data,na_count)
  NA_COUNT <- as.data.frame(NA_COUNT)
  COLUMN_NAME <- row.names(NA_COUNT)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(NA_COUNT) <- NULL
  NA_COUNT <- as.data.frame(cbind(COLUMN_NAME,NA_COUNT))
  head(NA_COUNT)
  
  ################## Zero Count
  ZEROS_COUNT <- sapply(numerical_data,zero_count)
  ZEROS_COUNT <- as.data.frame(ZEROS_COUNT)
  COLUMN_NAME <- row.names(ZEROS_COUNT)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(ZEROS_COUNT) <- NULL
  ZEROS_COUNT <- as.data.frame(cbind(COLUMN_NAME,ZEROS_COUNT))
  head(ZEROS_COUNT)
  
  ################ Positive Count
  POSITIVES_COUNT <- sapply(numerical_data,positive_count)
  POSITIVES_COUNT <- as.data.frame(POSITIVES_COUNT)
  COLUMN_NAME <- row.names(POSITIVES_COUNT)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(POSITIVES_COUNT) <- NULL
  POSITIVES_COUNT <- as.data.frame(cbind(COLUMN_NAME,POSITIVES_COUNT))
  head(POSITIVES_COUNT)
  
  ############# Negative Count
  NEGATIVES_COUNT <- sapply(numerical_data,negative_count)
  NEGATIVES_COUNT <- as.data.frame(NEGATIVES_COUNT)
  COLUMN_NAME <- row.names(NEGATIVES_COUNT)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(NEGATIVES_COUNT) <- NULL
  NEGATIVES_COUNT <- as.data.frame(cbind(COLUMN_NAME,NEGATIVES_COUNT))
  head(NEGATIVES_COUNT)
  
  ############# Mean of Each Numeric
  MEAN_VALUE <- sapply(numerical_data,mean_value)
  MEAN_VALUE <- as.data.frame(MEAN_VALUE)
  COLUMN_NAME <- row.names(MEAN_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(MEAN_VALUE) <- NULL
  MEAN_VALUE <- as.data.frame(cbind(COLUMN_NAME,MEAN_VALUE))
  head(MEAN_VALUE)
  
  ############ Median of Each Numeric
  MEDIAN_VALUE <- sapply(numerical_data,median_value)
  MEDIAN_VALUE <- as.data.frame(MEDIAN_VALUE)
  COLUMN_NAME <- row.names(MEDIAN_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(MEDIAN_VALUE) <- NULL
  MEDIAN_VALUE <- as.data.frame(cbind(COLUMN_NAME,MEDIAN_VALUE))
  head(MEDIAN_VALUE)
  
  ########### Minimum of Each Numeric
  MIN_VALUE <- sapply(numerical_data,min_value)
  MIN_VALUE <- as.data.frame(MIN_VALUE)
  COLUMN_NAME <- row.names(MIN_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(MIN_VALUE) <- NULL
  MIN_VALUE <- as.data.frame(cbind(COLUMN_NAME,MIN_VALUE))
  head(MIN_VALUE)
  
  ########### Maximum of Each Numeric
  MAX_VALUE <- sapply(numerical_data,max_value)
  MAX_VALUE <- as.data.frame(MAX_VALUE)
  COLUMN_NAME <- row.names(MAX_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(MAX_VALUE) <- NULL
  MAX_VALUE <- as.data.frame(cbind(COLUMN_NAME,MAX_VALUE))
  head(MAX_VALUE)
  
  ########### Sum of Each Numeric
  SUM_VALUE <- sapply(numerical_data,sum_value)
  SUM_VALUE <- as.data.frame(SUM_VALUE)
  COLUMN_NAME <- row.names(SUM_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(SUM_VALUE) <- NULL
  SUM_VALUE <- as.data.frame(cbind(COLUMN_NAME,SUM_VALUE))
  head(SUM_VALUE)
  
  ########## 0.05 and 0.95 Quantiles of each Numeric
  
  QUANTILE_VALUE <- sapply(numerical_data,quantile_value)
  COLUMN_NAME <- colnames(QUANTILE_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  QUANTILE_VALUE <- t(as.data.frame(QUANTILE_VALUE))
  row.names(QUANTILE_VALUE) <- NULL
  QUANTILE_VALUE <- as.data.frame(cbind(COLUMN_NAME,QUANTILE_VALUE))
  colnames(QUANTILE_VALUE) <- c("COLUMN_NAME","5%_QUANTILE_VALUE","95%_QUANTILE_QUANTILE")
  head(QUANTILE_VALUE)
  
  ########## variance value of each Numeric
  VARIANCE_VALUE <- sapply(numerical_data,variance_value)
  VARIANCE_VALUE <- as.data.frame(VARIANCE_VALUE)
  COLUMN_NAME <- row.names(VARIANCE_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(VARIANCE_VALUE) <- NULL
  VARIANCE_VALUE <- as.data.frame(cbind(COLUMN_NAME,VARIANCE_VALUE))
  head(VARIANCE_VALUE)
  
  ########## standard deviation value of each Numeric
  SD_VALUE <- sapply(numerical_data,sd_value)
  SD_VALUE <- as.data.frame(SD_VALUE)
  COLUMN_NAME <- row.names(SD_VALUE)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(SD_VALUE) <- NULL
  SD_VALUE <- as.data.frame(cbind(COLUMN_NAME,SD_VALUE))
  head(SD_VALUE)
  
  ########## Skewness Report
  
  skewness_report <- sapply(numerical_data,data_skewness_report)
  
  skewness_report <- t(skewness_report)
  
  COLUMN_NAME <- rownames(skewness_report)
  
  rownames(skewness_report) <- NULL
  
  skewness_report <- cbind(COLUMN_NAME, as.data.frame(skewness_report))
  
  colnames(skewness_report) <- c("COLUMN_NAME","SKEWNESS", "SKEWNESS_STRENGTH",
                                 "SKEWNESS_TYPE","RECOMMENDED_TRANSFORMATIONS")
  
  ####### Kurtosis Report
  
  kurtosis_report <- sapply(numerical_data,data_kurtosis_report)
  
  kurtosis_report <- t(kurtosis_report)
  
  COLUMN_NAME <- rownames(kurtosis_report)
  
  rownames(kurtosis_report) <- NULL
  
  kurtosis_report <- cbind(COLUMN_NAME, as.data.frame(kurtosis_report))
  
  colnames(kurtosis_report) <- c("COLUMN_NAME","KURTOSIS","KURTOSIS_TYPE")
  
  
  ######### Merging all the summary data into a single data frame
  
  NUMERICAL_SUMMARY <- join_all(list(NA_COUNT,ZEROS_COUNT,POSITIVES_COUNT,NEGATIVES_COUNT,
                                     MEAN_VALUE,MEDIAN_VALUE,MIN_VALUE,MAX_VALUE,
                                     SUM_VALUE,QUANTILE_VALUE,VARIANCE_VALUE,SD_VALUE,
                                     skewness_report,kurtosis_report),
                                by="COLUMN_NAME")
  
  if(nrow(NUMERICAL_SUMMARY)>0){
    
    print(NUMERICAL_SUMMARY)
    
  } else {
    
    print("No Numerical Data Summary Report Available")
    
  }
  
  return(NUMERICAL_SUMMARY)
  
}

##################### End of the function ###################


###########################################################
##### String or Factor Summary Analysis

string_data_summary_report <- function(input_dataframe){
  
  ############################ String Summary Functions #######
  ############################################################
  
  na_count <- function(input) {
    sum(is.na(input))
  }
  
  unique_values <- function(input){
    length(unique(input))
  }
  
  max_length <- function(input){
    max(str_length(input))
  }
  
  min_length <- function(input){
    min(str_length(input))
  }
  
  avg_length <- function(input){
    round(mean(str_length(input),na.rm = TRUE))
  }
  
  ################### End of String summary functions
  
  if(!require("dplyr")) install.packages("dplyr"); library("dplyr")
  
  if(!require("plyr")) install.packages("plyr"); library("plyr")
  
  ### selecting only string/character/text fields from the data frame  
  
  string_data <- select_if(input_dataframe,is.character)
  
  #### Calculating required String summaries
  
  ############## NA Count
  NA_COUNT <- sapply(string_data,na_count)
  NA_COUNT <- as.data.frame(NA_COUNT)
  COLUMN_NAME <- row.names(NA_COUNT)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(NA_COUNT) <- NULL
  NA_COUNT <- as.data.frame(cbind(COLUMN_NAME,NA_COUNT))
  head(NA_COUNT)
  
  ############## Unique Values
  UNIQUE_VALUES <- sapply(string_data,unique_values)
  UNIQUE_VALUES <- as.data.frame(UNIQUE_VALUES)
  COLUMN_NAME <- row.names(UNIQUE_VALUES)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(UNIQUE_VALUES) <- NULL
  UNIQUE_VALUES <- as.data.frame(cbind(COLUMN_NAME,UNIQUE_VALUES))
  head(UNIQUE_VALUES)
  
  ############## Maximum String Length
  MAX_LENGTH <- sapply(string_data,max_length)
  MAX_LENGTH <- as.data.frame(MAX_LENGTH)
  COLUMN_NAME <- row.names(MAX_LENGTH)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(MAX_LENGTH) <- NULL
  MAX_LENGTH <- as.data.frame(cbind(COLUMN_NAME,MAX_LENGTH))
  head(MAX_LENGTH)
  
  
  ############## Minimum String Length
  MIN_LENGTH <- sapply(string_data,min_length)
  MIN_LENGTH <- as.data.frame(MIN_LENGTH)
  COLUMN_NAME <- row.names(MIN_LENGTH)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(MIN_LENGTH) <- NULL
  MIN_LENGTH <- as.data.frame(cbind(COLUMN_NAME,MIN_LENGTH))
  head(MIN_LENGTH)
  
  ############## Average String Length
  AVG_LENGTH <- sapply(string_data,avg_length)
  AVG_LENGTH <- as.data.frame(AVG_LENGTH)
  COLUMN_NAME <- row.names(AVG_LENGTH)
  COLUMN_NAME <- as.data.frame(COLUMN_NAME)
  row.names(AVG_LENGTH) <- NULL
  AVG_LENGTH <- as.data.frame(cbind(COLUMN_NAME,AVG_LENGTH))
  head(AVG_LENGTH)
  
  
  ######### Merging all the summary data into a single data frame
  
  STRING_SUMMARY <- join_all(list(NA_COUNT,UNIQUE_VALUES,MAX_LENGTH,AVG_LENGTH,MIN_LENGTH),
                             by="COLUMN_NAME")
  
  if(nrow(STRING_SUMMARY)>0){
    
    print(STRING_SUMMARY)
    
  } else {
    
    print("No String Data Summary Report Available")
    
  }
  
  return(STRING_SUMMARY)
  
}

##############################################

#################################################################
################### Data Summary Report Generation ################

data_quality_report <- function(DB_Connection_Name,Staging_Table_Name) {
  
  
  ###### Sourcing the dependant data summary functions
  
  #source("basic_data_summary_report.R")
  #source("missing_data_pattern_report.R")
  #source("numerical_data_summary_report.R")
  #source("string_data_summary_report.R")
  #source("data_skewness_report.R")
  #source("data_kurtosis_report.R")
  
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
#################################################################################

#################################################################################
################ Function for Imputing Missing Values using MICE #########
#############################################################################

random_forest_imputation <- function(input_dataframe,max_iterations,fixed_seed) {
  
  if(!require("mice")) install.packages("mice"); library(mice)
  
  random_forest_imputation_method <- mice(data=input_dataframe,maxit = max_iterations,method="rf",
                                          seed=fixed_seed)
  
  random_forest_imputed_data <- as.data.frame(complete(random_forest_imputation_method,max_iterations))
  
  return(random_forest_imputed_data)
  
}


########################### End of Function ##############################


############################################################################
############### Connecting to the managaeDB Database
#############################################################################

mysql_db_connection <- connect_mysql_db(db_host,db_port,db_name,db_user_name,db_password)
mysql_staging_db_connection <- connect_mysql_db(staging_db_host,staging_db_port,staging_db_name,
                                                staging_db_user_name,staging_db_password)
mysql_app_db_connection <- connect_mysql_db(app_db_host,app_db_port,app_db_name,app_db_user_name,app_db_password)


##################################################################################
#################### Updating the AI Job Execution Summary 
##################################################################################

AI_Job_RUN_ID <- dbGetQuery(mysql_staging_db_connection,"select max(RUN_ID) from AI_Jobs_Execution_Summary")

new_AI_Job_RUN_ID <- if(is.na(AI_Job_RUN_ID)){1}else{AI_Job_RUN_ID+1}

dbSendQuery(mysql_staging_db_connection,paste0("insert into AI_Jobs_Execution_Summary(
                                       RUN_ID,BUSINESS_PROBLEM,AI_MODEL,JOB_START_DATETIME,JOB_RUN_STATUS,JOB_LOG_FILENAME)
                                       VALUES(",
                                       new_AI_Job_RUN_ID,",",
                                       "'",Business_Problem,"',",
                                       "'",Model_Name,"',",
                                       "'",start_time,"'",",",
                                       "'","Started","'",",",
                                       "'",log_file_name,"')",
                                       sep=""))

############################################################################
############### Getting the AI Metadata
#############################################################################

AI_Metadata_Details <- get_AI_Model_Metadata(mysql_app_db_connection,Business_Problem,Model_Name)

############################################################################
############### Loading Source Data into Staging Table
#############################################################################

staging_sql <- as.character(AI_Metadata_Details$in_staging_sql_query)

staging_table_name <- as.character(AI_Metadata_Details$STAGING_TABLE)

source_records <- staging_data_load(mysql_staging_db_connection,mysql_db_connection,staging_sql,staging_table_name)

##################################################################################
#################### Updating the AI Job Execution Summary 
##################################################################################

dbSendQuery(mysql_staging_db_connection,paste0("UPDATE AI_Jobs_Execution_Summary
                                       SET SOURCE_RECORD_COUNT=",
                                       source_records,
                                       " , JOB_RUN_STATUS=",
                                       "'","Source Data Reading Completed","'",
                                       " WHERE RUN_ID=",new_AI_Job_RUN_ID,sep=""))


############################################################################
########### Conditional Execution of AI Model
############################################################################

if(exists("source_records") & source_records>0){
  print(paste0("Source Query Result set contains: ",source_records,sep=""))
  
  ############################################################################
  ############### Displaying the Source Data Summary Report
  #############################################################################
  
  data_quality_report(mysql_staging_db_connection,staging_table_name)
  
  ############################################################################
  ############### Applying Statistical Transformations
  #############################################################################
  
  staging_data <- dbGetQuery(mysql_staging_db_connection,paste0("select * from ",staging_table_name,sep=""))
  
  staging_records <- nrow(staging_data)
  
  ##################################################################################
  #################### Updating the AI Job Execution Summary 
  ##################################################################################
  
  dbSendQuery(mysql_staging_db_connection,paste0("UPDATE AI_Jobs_Execution_Summary
                                                 SET STAGING_RECORD_COUNT=",
                                                 staging_records,
                                                 " , JOB_RUN_STATUS=",
                                                 "'","Staging Load Completed","'",
                                                 " WHERE RUN_ID=",new_AI_Job_RUN_ID,sep=""))
  
  transformed_data <- suppressWarnings(random_forest_imputation(staging_data,4,1234))
  
  ail_table_name <- as.character(AI_Metadata_Details$AIL_TABLE)
  
  ######################### Loading the AIL Table ######################################
  #### Checking the count of records for transformed_data and then proceeding
  #### with loading the transformed_data into AIL Table
  
  if(exists("transformed_data") & nrow(transformed_data)>0){
    
    #### Truncating the AIL Table
    #### Right now, insert and update option of data loading is not implemented
    
    dbSendQuery(mysql_db_connection,paste0("truncate table ",ail_table_name,sep=""))
    
    #################### Looping through the entire data set to create the insert statements
    
    for(i in 1:nrow(transformed_data)){
      
      dbSendQuery(mysql_db_connection,paste0("insert into ",ail_table_name, "(
                                             DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5,
                                             YEAR,MONTH,ACTUAL_SALES) ",
                                             "values(",
                                             "\"",transformed_data$DIMENSION1[i],"\"",",",
                                             "\"",transformed_data$DIMENSION2[i],"\"",",",
                                             "\"",transformed_data$DIMENSION3[i],"\"",",",
                                             "\"",transformed_data$DIMENSION4[i],"\"",",",
                                             "\"",transformed_data$DIMENSION5[i],"\"",",",
                                             transformed_data$YEAR[i],",",transformed_data$MONTH[i],",",
                                             transformed_data$ACTUAL_SALES[i],")",sep="")) } 
    
    ########################### End of the Looping to insert the records into AIL Table
    
    print("Transformed Data Successfully Loaded into AIL Table")
    
    
    ##################### Dimensional Analysis and Loading it into AI_DP_Analysis Table
    
    ##########  Analyzing the combination of dimensions for data criteria 
    ###################################################
    
    #### Function to get unique records in the dimensions
    
    unique_rows <- function(input){
      if(sum(str_length(input))==0 &
         length(is.na(input))==length(input)) {
        unique_rows <- 0
      } else {
        unique_rows <- length(unique(input))
      }
    }
    
    ############################### End of the Function 
    
    Dimension_Analysis <- as.data.frame(apply(transformed_data[,1:5],2,unique_rows))
    column <- row.names(Dimension_Analysis)
    row.names(Dimension_Analysis) <- NULL
    Dimension_Analysis <- cbind(column,Dimension_Analysis)
    colnames(Dimension_Analysis) <- c("DIMENSION","UNIQUE_VALUES")
    
    
    print("Analyzing the Combination of Dimensions")
    print(Dimension_Analysis)
    valid_dimensions <- filter(Dimension_Analysis,UNIQUE_VALUES>0)
    print(paste("Dimensions having one or more unique values are: ", 
                paste(valid_dimensions$DIMENSION,collapse = ","),sep=""))
    
    
    #####################################################################################
    
    Dim_Data_Analysis <- sqldf("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                               max(YEAR) as MAX_YEAR, 
                               min(YEAR) as MIN_YEAR, count(*) as TOTAL_DATA_POINTS
                               from transformed_data 
                               group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5")
    
    Dim_Data_Analysis_historical <- sqldf(paste("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                                                count(*) as HISTORICAL_DATA_POINTS
                                                from transformed_data
                                                where YEAR!=",current_year," 
                                                group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5",sep=""))
    
    
    Dim_Data_Analysis_Current_Year <- sqldf(paste("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                                                  count(*) as DATA_POINTS_CURRENT_YEAR
                                                  from transformed_data
                                                  where YEAR=",current_year," 
                                                  group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5",sep=""))
    
    
    Dim_Data_Analysis_last2_years <- sqldf(paste("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                                                 count(*) as DATA_POINTS_PAST_2_YEARS
                                                 from transformed_data
                                                 where YEAR in (",current_year-1,",",current_year-2,")
                                                 group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5"))
    
    Dim_Data_Analysis_last3_years <- sqldf(paste("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                                                 count(*) as DATA_POINTS_PAST_3_YEARS
                                                 from transformed_data
                                                 where YEAR in (",current_year-1,",",current_year-2,",",current_year-3,")
                                                 group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5"))
    
    Dim_Data_Analysis_last4_years <- sqldf(paste("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                                                 count(*) as DATA_POINTS_PAST_4_YEARS
                                                 from transformed_data
                                                 where YEAR in (",current_year-1,",",current_year-2,",",current_year-3,
                                                 ",",current_year-4,")
                                                 group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5"))
    
    Dim_Data_Analysis_CYMM <- sqldf(paste("select DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5,
                                          max(MONTH) as CURRENT_YEAR_MAX_MONTH
                                          from transformed_data
                                          where YEAR==",current_year," 
                                          group by DIMENSION1, DIMENSION2, DIMENSION3,DIMENSION4,DIMENSION5",
                                          sep=""))
    
    
    Dim_Data_Analysis_Combined <- sqldf("select x.*, y.HISTORICAL_DATA_POINTS,
                                        a.DATA_POINTS_PAST_2_YEARS,
                                        b.DATA_POINTS_PAST_3_YEARS,
                                        c.DATA_POINTS_PAST_4_YEARS,
                                        z.DATA_POINTS_CURRENT_YEAR,
                                        d.CURRENT_YEAR_MAX_MONTH
                                        from Dim_Data_Analysis x
                                        left outer join Dim_Data_Analysis_historical y
                                        on (x.DIMENSION1=y.DIMENSION1 and
                                        x.DIMENSION2=y.DIMENSION2 and
                                        x.DIMENSION3=y.DIMENSION3 and
                                        x.DIMENSION4=y.DIMENSION4 and 
                                        x.DIMENSION5=y.DIMENSION5)
                                        left outer join Dim_Data_Analysis_Current_Year z
                                        on (x.DIMENSION1=z.DIMENSION1 and
                                        x.DIMENSION2=z.DIMENSION2 and
                                        x.DIMENSION3=z.DIMENSION3 and
                                        x.DIMENSION4=z.DIMENSION4 and 
                                        x.DIMENSION5=z.DIMENSION5)
                                        left outer join Dim_Data_Analysis_last2_years a
                                        on (x.DIMENSION1=a.DIMENSION1 and
                                        x.DIMENSION2=a.DIMENSION2 and
                                        x.DIMENSION3=a.DIMENSION3 and
                                        x.DIMENSION4=a.DIMENSION4 and 
                                        x.DIMENSION5=a.DIMENSION5)
                                        left outer join Dim_Data_Analysis_last3_years b
                                        on (x.DIMENSION1=b.DIMENSION1 and
                                        x.DIMENSION2=b.DIMENSION2 and
                                        x.DIMENSION3=b.DIMENSION3 and
                                        x.DIMENSION4=b.DIMENSION4 and 
                                        x.DIMENSION5=b.DIMENSION5)
                                        left outer join Dim_Data_Analysis_last4_years c
                                        on (x.DIMENSION1=c.DIMENSION1 and
                                        x.DIMENSION2=c.DIMENSION2 and
                                        x.DIMENSION3=c.DIMENSION3 and
                                        x.DIMENSION4=c.DIMENSION4 and 
                                        x.DIMENSION5=c.DIMENSION5)
                                        left outer join  Dim_Data_Analysis_CYMM d
                                        on (x.DIMENSION1=d.DIMENSION1 and
                                        x.DIMENSION2=d.DIMENSION2 and
                                        x.DIMENSION3=d.DIMENSION3 and
                                        x.DIMENSION4=d.DIMENSION4 and 
                                        x.DIMENSION5=d.DIMENSION5)"
    )
    
    ### Removing the temp objects of dim analysis
    rm(Dim_Data_Analysis,Dim_Data_Analysis_Current_Year,Dim_Data_Analysis_historical,
       Dim_Data_Analysis_last2_years,Dim_Data_Analysis_last3_years,
       Dim_Data_Analysis_last4_years, Dim_Data_Analysis_CYMM)
    
    ### Replacing NAs with Zero
    Dim_Data_Analysis_Combined[is.na(Dim_Data_Analysis_Combined)] <- 0 
    
    ## Checking the valid combinations of the Dimensions
    Valid_Data <- subset(Dim_Data_Analysis_Combined,
                         Dim_Data_Analysis_Combined$TOTAL_DATA_POINTS>=data_points_criteria & 
                           Dim_Data_Analysis_Combined$MAX_YEAR==current_year &
                           Dim_Data_Analysis_Combined$DATA_POINTS_PAST_2_YEARS==24)
    
    Invalid_Data <- subset(Dim_Data_Analysis_Combined,
                           Dim_Data_Analysis_Combined$TOTAL_DATA_POINTS<data_points_criteria | 
                             Dim_Data_Analysis_Combined$MAX_YEAR!=current_year |
                             Dim_Data_Analysis_Combined$DATA_POINTS_PAST_2_YEARS<24)
    
    ### Printing the count of Valid and Invalid combinations of Dimensions
    print(paste0("Valid Combinations of Dimensions for Forecasting: ",nrow(Valid_Data),sep=""))
    
    print(paste0("Invalid Combinations of Dimensions for Forecasting: ",nrow(Dim_Data_Analysis_Combined)-nrow(Valid_Data),sep=""))
    print("Check the Data Analysis Table for more information")
    
    
    #######################################################################################
    ############# Writing the Valid and Invalid Dimension - Data Criteria Check
    ######################################################################################
    
    if(exists("Valid_Data") & nrow(Valid_Data)>0){
      
      print("Loading the Sales Forecast Valid Data Points Analysis into DB Table")
      
      #### Looping through the DP Analysis and creating insert statements
      
      #### Getting the RUN_ID from the table
      
      DP_Analysis_Valid_RUN_ID <- dbGetQuery(mysql_db_connection,"select max(RUN_ID) from AI_Sales_Forecast_DP_Analysis_Valid")
      
      new_DP_Analysis_Valid_RUN_ID <- if(is.na(DP_Analysis_Valid_RUN_ID)){1}else{DP_Analysis_Valid_RUN_ID+1}
    
      
       for(i in 1:nrow(Valid_Data)){
        dbSendQuery(mysql_db_connection,paste0("insert into AI_Sales_Forecast_DP_Analysis_Valid",
                                               "(RUN_ID,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                               DIMENSION5,MAX_YEAR,MIN_YEAR,TOTAL_DATA_POINTS,
                                               HISTORICAL_DATA_POINTS,DATA_POINTS_LAST_2_YEARS,
                                               DATA_POINTS_LAST_3_YEARS,DATA_POINTS_LAST_4_YEARS,
                                               DATA_POINTS_CURRENT_YEAR,CURRENT_YEAR_MAX_MONTH) ",
                                               "values(",new_DP_Analysis_Valid_RUN_ID,",",
                                               "'",Valid_Data$DIMENSION1[i],"'",",",
                                               "'",Valid_Data$DIMENSION2[i],"'",",",
                                               "'",Valid_Data$DIMENSION3[i],"'",",",
                                               "'",Valid_Data$DIMENSION4[i],"'",",",
                                               "'",Valid_Data$DIMENSION5[i],"'",",",
                                               Valid_Data$MAX_YEAR[i],",",
                                               Valid_Data$MIN_YEAR[i],",",
                                               Valid_Data$TOTAL_DATA_POINTS[i],",",
                                               Valid_Data$HISTORICAL_DATA_POINTS[i],",",
                                               Valid_Data$DATA_POINTS_PAST_2_YEARS[i],",",
                                               Valid_Data$DATA_POINTS_PAST_3_YEARS[i],",",
                                               Valid_Data$DATA_POINTS_PAST_4_YEARS[i],",",
                                               Valid_Data$DATA_POINTS_CURRENT_YEAR[i],",",
                                               Valid_Data$CURRENT_YEAR_MAX_MONTH[i],
                                               ")",sep=""))}
      
      print("Data Loading into Valid Data Points Analysis Table is completed Successfully") 

    } else {
      
      print("Error Occured in Dimensional Data Analysis/No Valid Data Points")
      print("Please check the Transformed Data Object")
      
    }
    
    
    if(exists("Invalid_Data") & nrow(Invalid_Data)>0){
      
      print("Loading the Sales Forecast Invalid Data Points Analysis into DB Table")
      
      #### Looping through the DP Analysis and creating insert statements
      
      #### Getting the RUN_ID from the table
      
      DP_Analysis_Invalid_RUN_ID <- dbGetQuery(mysql_db_connection,"select max(RUN_ID) from AI_Sales_Forecast_DP_Analysis_Invalid")
      
      new_DP_Analysis_Invalid_RUN_ID <- if(is.na(DP_Analysis_Invalid_RUN_ID)){1}else{DP_Analysis_Invalid_RUN_ID+1}
    
    
    for(i in 1:nrow(Invalid_Data)){
      dbSendQuery(mysql_db_connection,paste0("insert into AI_Sales_Forecast_DP_Analysis_Invalid",
                                             "(RUN_ID,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,MAX_YEAR,MIN_YEAR,TOTAL_DATA_POINTS,
                                             HISTORICAL_DATA_POINTS,DATA_POINTS_LAST_2_YEARS,
                                             DATA_POINTS_LAST_3_YEARS,DATA_POINTS_LAST_4_YEARS,
                                             DATA_POINTS_CURRENT_YEAR,CURRENT_YEAR_MAX_MONTH) ",
                                               "values(",new_DP_Analysis_Invalid_RUN_ID,",",
                                               "'",Invalid_Data$DIMENSION1[i],"'",",",
                                               "'",Invalid_Data$DIMENSION2[i],"'",",",
                                               "'",Invalid_Data$DIMENSION3[i],"'",",",
                                               "'",Invalid_Data$DIMENSION4[i],"'",",",
                                               "'",Invalid_Data$DIMENSION5[i],"'",",",
                                               Invalid_Data$MAX_YEAR[i],",",
                                               Invalid_Data$MIN_YEAR[i],",",
                                               Invalid_Data$TOTAL_DATA_POINTS[i],",",
                                               Invalid_Data$HISTORICAL_DATA_POINTS[i],",",
                                               Invalid_Data$DATA_POINTS_PAST_2_YEARS[i],",",
                                               Invalid_Data$DATA_POINTS_PAST_3_YEARS[i],",",
                                               Invalid_Data$DATA_POINTS_PAST_4_YEARS[i],",",
                                               Invalid_Data$DATA_POINTS_CURRENT_YEAR[i],",",
                                               Invalid_Data$CURRENT_YEAR_MAX_MONTH[i],
                                               ")",sep=""))}
      
      ##### End of Data Loading Looping
      print("Data Loading into Invalid Data Points Analysis Table is completed Successfully") 
      
  } else {
    
    print("Error Occured in Dimensional Data Analysis/No Invalid Data Points")
    print("Please check the Transformed Data Object")
    
  }
    
    
    
  } else {
    
    print("Transformed Data Object not found")
    print("Error might have occured in transforming the staging Data")
    
  }
  
  ############################## End of Loading of AIL Table #################################
  ############################## End of Loading of AI DP Analysis Table ######################
  
  
  ##################################################################################################
  ####################### Sales Forecast Models Validation for Dimensional Combinations ##############
  #################################################################################################
  
  ##### Assigning the list objects for various SF Models
  
  list.sales_forecast_arima <- list()
  
  #list.sales_forecast_nnelm <- list()
  
  #list.sales_forecast_nnmlp <- list()
  
  list.sales_forecast_ets <- list()
  
  list.sales_forecast_sma <- list()
  
  list.sales_forecast_holtwinters <- list()
  
  list.sales_forecast_recommended <- list()
  
  list.sales_forecast_validation <- list()
  
  list.sales_forecast_models_mape <- list()
  
  list.arima.method <- list()
  
  
  ###### Looping through every combination of valid Data Combination 
  
  #i <- 1
  
  for (i in 1:nrow(Valid_Data))
    
  {
    
    ###### Dynamically checking the availability of DP
    ##### finding out the start year for each combination
    
    if(Valid_Data$DATA_POINTS_PAST_4_YEARS[i]==48){
      valid_start_year <- current_year-4
    } else if (Valid_Data$DATA_POINTS_PAST_3_YEARS[i]==36) {
      valid_start_year <- current_year-3
    } else if (Valid_Data$DATA_POINTS_PAST_2_YEARS[i]==24){
      valid_start_year <- current_year-2
    } else {
      valid_start_year <- NULL
    }
    
    #### Start Year Check Completed
    
    
    x_dimension1 <- Valid_Data$DIMENSION1[i]
    x_dimension2 <- Valid_Data$DIMENSION2[i]
    x_dimension3 <- Valid_Data$DIMENSION3[i]
    x_dimension4 <- Valid_Data$DIMENSION4[i]
    x_dimension5 <- Valid_Data$DIMENSION5[i]
    x_current_year_max_month <- Valid_Data$CURRENT_YEAR_MAX_MONTH[i]
    
    
    x_data <- subset(transformed_data,(transformed_data$DIMENSION1==x_dimension1 &
                                         transformed_data$DIMENSION2==x_dimension2 &
                                         transformed_data$DIMENSION3==x_dimension3 &
                                         transformed_data$DIMENSION4==x_dimension4 &
                                         transformed_data$DIMENSION5==x_dimension5 &
                                         transformed_data$YEAR>=valid_start_year))
    
    ### Filtering the train and validation data from the entire data
    
    x_train_data <- filter(x_data,x_data$YEAR!=current_year)
    
    x_current_data <- filter(x_data,x_data$YEAR==current_year)
    
    ### Creating the time series object
    
    data.ts <- ts(x_train_data$ACTUAL_SALES,start=c(valid_start_year,01),end=c(current_year-1,12),frequency = 12)
    
    ## Plotting the time series object
    
    #plot(data.ts)
    
    ## Fitting a straight Line
    
    #abline(reg=lm(data.ts~time(data.ts)))
    
    #Plotting the decomposition of Time series
    #(Seasonal, Trend and Irregular components)
    
    #plot(decompose(data.ts))
    
    out_decompose <- decompose(data.ts)
    
    Add_multiply_type <- out_decompose$type
    
    #print(paste("The type of model(Additive/Multiplicative):",Add_multiply_type ))
    
    ### Cycle across the years
    
    #cycle(data.ts)
    
    #aggregate the cycles and display a year on year trend
    
    #plot(aggregate(data.ts,FUN=mean))
    
    #Plotting the differences in the time series to make it stationary
    
    #diff(data.ts)
    
    #differenced_time_series <- diff(data.ts, differences = 2)
    
    #plot(differenced_time_series)
    
    #######################################################################
    ############################ Building the Arima Model
    ##########################################################################
    
    arima.model <- auto.arima(data.ts,stationary = FALSE, seasonal = FALSE)
    
    #summary(arima.model)
    
    #### Calculating the new forecast period to accomodate the validation and customer desired forecast period
    
    new_desired_forecast_period <- x_current_year_max_month+desired_forecast_period
    
    predicted_data <- forecast(arima.model,new_desired_forecast_period)
    
    ## Plotting the predicted Values
    
    #plot(predicted_data)
    
    #autoplot(predicted_data)
    
    arima_method <- predicted_data$method
    
    ### creating the Forecast Method Variable
    
    FORECAST_METHOD <- data.frame(rep('AUTO ARIMA',new_desired_forecast_period))
    
    colnames(FORECAST_METHOD) <- "FORECAST_METHOD"
    
    ### Creating the Dimensions variables
    
    DIMENSION1 <- rep(x_dimension1,new_desired_forecast_period)
    DIMENSION2 <- rep(x_dimension2,new_desired_forecast_period)
    DIMENSION3 <- rep(x_dimension3,new_desired_forecast_period)
    DIMENSION4 <- rep(x_dimension4,new_desired_forecast_period)
    DIMENSION5 <- rep(x_dimension5,new_desired_forecast_period)
    
    Dimensions <- cbind(FORECAST_METHOD,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5)
    
    ### Creating the Measures Variables
    
    PREDICTED_PERIOD <- row.names(as.data.frame(predicted_data))
    
    YEAR <- substr(PREDICTED_PERIOD,(nchar(PREDICTED_PERIOD)+1)-4,nchar(PREDICTED_PERIOD))
    
    MONTH <- substr(PREDICTED_PERIOD,1,3)
    
    x_predicted_data <- as.data.frame(predicted_data)
    
    FORECASTED_SALES <- as.numeric(x_predicted_data[,1])
    
    Measures <- cbind(YEAR,MONTH,FORECASTED_SALES)
    
    ### Creatning the Final Data Frame
    
    Final_Data_arima <- cbind(Dimensions,Measures)
    
    ### Storing the Arima Method for every Combination
    
    Final_Data_Arima_Method <- cbind(Dimensions[,-1],arima_method)
    
    ### Changing the MMM format to MM Format for Month
    
    Final_Data_arima <- sqldf("select FORECAST_METHOD,
                              DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5,
                              YEAR,
                              case 
                              when MONTH='Jan' then 1
                              when MONTH='Feb' then 2
                              when MONTH='Mar' then 3
                              when MONTH='Apr' then 4
                              when MONTH='May' then 5
                              when MONTH='Jun' then 6
                              when MONTH='Jul' then 7
                              when MONTH='Aug' then 8
                              when MONTH='Sep' then 9
                              when MONTH='Oct' then 10
                              when MONTH='Nov' then 11
                              when MONTH='Dec' then 12
                              else MONTH end as MONTH,FORECASTED_SALES
                              from Final_Data_arima")
    
    
    #colnames(Final_Data_arima)[8] <- c("MONTH")
    
    row.names(Final_Data_arima) <- NULL
    
    list.sales_forecast_arima[[i]] <- Final_Data_arima
    list.arima.method[[i]] <- arima_method
    
    #######################################################################################
    #########      Exponential Smoothing Method
    #######################################################################################
    
    ets_data.ts <- ets(data.ts, model="ZZZ", damped = NULL)
    
    #plot(ets_data.ts)
    
    #summary(ets_data.ts)
    
    predicted_data_ets <- forecast(ets_data.ts, new_desired_forecast_period)
    
    # Plotting the forecasted Data
    
    #plot(predicted_data)
    
    #autoplot(predicted_data_ets)
    
    FORECAST_METHOD <- data.frame(rep('ETS',new_desired_forecast_period))
    
    colnames(FORECAST_METHOD) <- "FORECAST_METHOD"
    
    Dimensions <- cbind(FORECAST_METHOD,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5)
    
    PREDICTED_PERIOD <- row.names(as.data.frame(predicted_data_ets))
    
    YEAR <- substr(PREDICTED_PERIOD,(nchar(PREDICTED_PERIOD)+1)-4,nchar(PREDICTED_PERIOD))
    
    MONTH <- substr(PREDICTED_PERIOD,1,3)
    
    x_predicted_data <- as.data.frame(predicted_data_ets)
    
    FORECASTED_SALES <- abs(as.numeric(x_predicted_data[,1]))
    
    Measures <- cbind(YEAR,MONTH,FORECASTED_SALES)
    
    Final_Data_ets <- cbind(Dimensions,Measures)
    
    Final_Data_ets <- sqldf("select FORECAST_METHOD,
                            DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5,
                            YEAR,
                            case 
                            when MONTH='Jan' then 1
                            when MONTH='Feb' then 2
                            when MONTH='Mar' then 3
                            when MONTH='Apr' then 4
                            when MONTH='May' then 5
                            when MONTH='Jun' then 6
                            when MONTH='Jul' then 7
                            when MONTH='Aug' then 8
                            when MONTH='Sep' then 9
                            when MONTH='Oct' then 10
                            when MONTH='Nov' then 11
                            when MONTH='Dec' then 12
                            else MONTH end as MONTH,FORECASTED_SALES
                            from Final_Data_ets")
    
    row.names(Final_Data_ets) <- NULL
    
    list.sales_forecast_ets[[i]] <- Final_Data_ets
    
    ######################################################################################
    #######################################################################################
    #################### Simple Moving Average
    #######################################################################################
    
    sma_model <- sma(data.ts,h=new_desired_forecast_period,holdout = TRUE)
    
    predicted_data_sma <- forecast(sma_model, new_desired_forecast_period)
    
    FORECAST_METHOD <- data.frame(rep('SMA',new_desired_forecast_period))
    
    colnames(FORECAST_METHOD) <- "FORECAST_METHOD"
    
    Dimensions <- cbind(FORECAST_METHOD,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5)
    
    PREDICTED_PERIOD <- row.names(as.data.frame(predicted_data_sma))
    
    YEAR <- substr(PREDICTED_PERIOD,(nchar(PREDICTED_PERIOD)+1)-4,nchar(PREDICTED_PERIOD))
    
    MONTH <- substr(PREDICTED_PERIOD,1,3)
    
    x_predicted_data <- as.data.frame(predicted_data_sma$mean)
    
    FORECASTED_SALES <- abs(as.numeric(x_predicted_data[,1]))
    
    Measures <- cbind(YEAR,MONTH,FORECASTED_SALES)
    
    Final_Data_sma <- cbind(Dimensions,Measures)
    
    Final_Data_sma <- sqldf("select FORECAST_METHOD,
                            DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5,
                            YEAR,
                            case 
                            when MONTH='Jan' then 1
                            when MONTH='Feb' then 2
                            when MONTH='Mar' then 3
                            when MONTH='Apr' then 4
                            when MONTH='May' then 5
                            when MONTH='Jun' then 6
                            when MONTH='Jul' then 7
                            when MONTH='Aug' then 8
                            when MONTH='Sep' then 9
                            when MONTH='Oct' then 10
                            when MONTH='Nov' then 11
                            when MONTH='Dec' then 12
                            else MONTH end as MONTH,FORECASTED_SALES
                            from Final_Data_sma")
    
    row.names(Final_Data_sma) <- NULL
    
    list.sales_forecast_sma[[i]] <- Final_Data_sma
    
    
    #######################################################################################
    ############## Holts Winter Method with Seasonality
    #######################################################################################
    #beta=FALSE to facilitate the exponential smoothing
    
    holt_winters_model <- HoltWinters(data.ts, beta=FALSE, seasonal=Add_multiply_type)
    
    #summary(holt_winters_model)
    
    predicted_data_holtwinters <- forecast(holt_winters_model,new_desired_forecast_period)
    
    FORECAST_METHOD <- data.frame(rep('HOLT WINTERS',new_desired_forecast_period))
    
    colnames(FORECAST_METHOD) <- "FORECAST_METHOD"
    
    Dimensions <- cbind(FORECAST_METHOD,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5)
    
    PREDICTED_PERIOD <- row.names(as.data.frame(predicted_data_holtwinters))
    
    YEAR <- substr(PREDICTED_PERIOD,(nchar(PREDICTED_PERIOD)+1)-4,nchar(PREDICTED_PERIOD))
    
    MONTH <- substr(PREDICTED_PERIOD,1,3)
    
    FORECASTED_SALES <- abs(as.numeric(predicted_data_holtwinters$mean))
    
    Measures <- cbind(YEAR,MONTH,FORECASTED_SALES)
    
    Final_Data_holtwinters <- cbind(Dimensions,Measures)
    
    Final_Data_holtwinters <- sqldf("select FORECAST_METHOD,
                                    DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,DIMENSION5,
                                    YEAR,
                                    case 
                                    when MONTH='Jan' then 1
                                    when MONTH='Feb' then 2
                                    when MONTH='Mar' then 3
                                    when MONTH='Apr' then 4
                                    when MONTH='May' then 5
                                    when MONTH='Jun' then 6
                                    when MONTH='Jul' then 7
                                    when MONTH='Aug' then 8
                                    when MONTH='Sep' then 9
                                    when MONTH='Oct' then 10
                                    when MONTH='Nov' then 11
                                    when MONTH='Dec' then 12
                                    else MONTH end as MONTH,FORECASTED_SALES
                                    from Final_Data_holtwinters")
    
    row.names(Final_Data_holtwinters) <- NULL
    
    list.sales_forecast_holtwinters[[i]] <- Final_Data_holtwinters
    
    
    #################################################################################################
    ############### Validation of Models ############################
    ##################################################################################################
    
    
    validation_qry <- paste0("SELECT 
                             a.DIMENSION1 as DIMENSION1,
                             a.DIMENSION2 as DIMENSION2,
                             a.DIMENSION3 as DIMENSION3,
                             a.DIMENSION4 as DIMENSION4,
                             a.DIMENSION5 as DIMENSION5,
                             cast(a.YEAR as integer) as YEAR,
                             cast(a.MONTH as integer) as MONTH",
                             if(exists("x_current_data")){",cast(k.ACTUAL_SALES as decimal(38,6)) as ACTUAL_SALES"} else{""},
                             if(exists("Final_Data_sma")){",cast(b.FORECASTED_SALES as decimal(38,6)) as SMA"} else{""},
                             if(exists("Final_Data_ets")){",cast(c.FORECASTED_SALES as decimal(38,6)) as ETS"} else{""},
                             if(exists("Final_Data_holtwinters")){",cast(d.FORECASTED_SALES as decimal(38,6)) as HOLT_WINTERS"} else{""},
                             if(exists("Final_Data_arima")){",cast(a.FORECASTED_SALES as decimal(38,6)) as ARIMA"} else{""},
                             if(exists("Final_Data_nnelm")){",cast(g.FORECASTED_SALES as decimal(38,6)) as NN_ELM"} else{""},
                             if(exists("Final_Data_nnmlp")){",cast(h.FORECASTED_SALES as decimal(38,6)) as NN_MLP"} else{""},
                             
                             " from Final_Data_arima a ",
                             
                             if(exists("Final_Data_sma")){" inner join Final_Data_sma b on 
                               a.DIMENSION1=b.DIMENSION1 and a.DIMENSION2=b.DIMENSION2 and 
                               a.DIMENSION3=b.DIMENSION3 and a.DIMENSION4=b.DIMENSION4 and 
                               a.DIMENSION5=b.DIMENSION5 and a.YEAR=b.YEAR and a.MONTH=b.MONTH"} else{""},
                             if(exists("Final_Data_ets")){" inner join Final_Data_ets c on 
                               a.DIMENSION1=c.DIMENSION1 and a.DIMENSION2=c.DIMENSION2 and 
                               a.DIMENSION3=c.DIMENSION3 and a.DIMENSION4=c.DIMENSION4 and 
                               a.DIMENSION5=c.DIMENSION5 and a.YEAR=c.YEAR and a.MONTH=c.MONTH"} else{""},
                             if(exists("Final_Data_holtwinters")){" inner join Final_Data_holtwinters d on
                               a.DIMENSION1=d.DIMENSION1 and a.DIMENSION2=d.DIMENSION2 and 
                               a.DIMENSION3=d.DIMENSION3 and a.DIMENSION4=d.DIMENSION4 and 
                               a.DIMENSION5=d.DIMENSION5 and a.YEAR=d.YEAR and a.MONTH=d.MONTH"} else{""},
                             if(exists("Final_Data_nnelm")){" inner join Final_Data_nnelm g on
                               a.DIMENSION1=g.DIMENSION1 and a.DIMENSION2=g.DIMENSION2 and 
                               a.DIMENSION3=g.DIMENSION3 and a.DIMENSION4=g.DIMENSION4 and 
                               a.DIMENSION5=g.DIMENSION5 and a.YEAR=g.YEAR and a.MONTH=g.MONTH"} else{""},
                             if(exists("Final_Data_nnmlp")){" inner join Final_Data_nnmlp h on
                               a.DIMENSION1=h.DIMENSION1 and a.DIMENSION2=h.DIMENSION2 and 
                               a.DIMENSION3=h.DIMENSION3 and a.DIMENSION4=h.DIMENSION4 and 
                               a.DIMENSION5=h.DIMENSION5 and a.YEAR=h.YEAR and a.MONTH=h.MONTH"} else{""},
                             if(exists("x_current_data")){" inner join x_current_data k on
                               a.DIMENSION1=k.DIMENSION1 and a.DIMENSION2=k.DIMENSION2 and 
                               a.DIMENSION3=k.DIMENSION3 and a.DIMENSION4=k.DIMENSION4 and 
                               a.DIMENSION5=k.DIMENSION5 and a.YEAR=k.YEAR and a.MONTH=k.MONTH"} else{""},
                             " where a.YEAR==",current_year,
                             " and k.ACTUAL_SALES!=0",
                             
                             sep="")
    
    
    validation_data <- sqldf(validation_qry)
    
    list.sales_forecast_validation[[i]] <- validation_data
    
    #########################################################################################################
    ################# Mean Absolute Percentage Error
    ############################################################################################
    
    SMA_Error <- mape(validation_data$ACTUAL_SALES,validation_data$SMA)*100
    
    ETS_Error <- mape(validation_data$ACTUAL_SALES,validation_data$ETS)*100
    
    Holtwinters_Error <- mape(validation_data$ACTUAL_SALES,validation_data$HOLT_WINTERS)*100
    
    ARIMA_Error <- mape(validation_data$ACTUAL_SALES,validation_data$ARIMA)*100
    
    #NNELM_Error <- mape(validation_data$ACTUAL_SALES,validation_data$NN_ELM)*100
    
    #NNMLP_Error <- mape(validation_data$ACTUAL_SALES,validation_data$NN_MLP)*100
    
    
    
    error_table <- as.data.frame(rbind(SMA_Error,ETS_Error,Holtwinters_Error,ARIMA_Error))
    
    SF_Model <- rownames(error_table)
    
    row.names(error_table) <- NULL
    
    error_table <- cbind(as.character(SF_Model),error_table)
    
    colnames(error_table) <- c("Model","Error")
    
    
    min_error <- as.character(filter(error_table,Error==min(Error))$Model)
    
    if(min_error=="SMA_Error") {
      Recommended_Model <- "SMA"
      Recommended_Model_Data <- Final_Data_sma
    } else if(min_error=="ETS_Error") {
      Recommended_Model <- "ETS"
      Recommended_Model_Data <- Final_Data_ets
    } else if(min_error=="Holtwinters_Error") {
      Recommended_Model <- "HOLTWINTERS"
      Recommended_Model_Data <- Final_Data_holtwinters
    } else if(min_error=="ARIMA_Error") {
      Recommended_Model <- "ARIMA"
      Recommended_Model_Data <- Final_Data_arima
    } else if(min_error=="NNELM_Error") {
      Recommended_Model <- "NN_ELM"
      Recommended_Model_Data <- Final_Data_nnelm
    } else if(min_error=="NNMLP_Error") {
      Recommended_Model <- "NN_MLP"
      Recommended_Model_Data <- Final_Data_nnmlp
    } else {
      print("Model Recommendation Process Failed")
    }
    
    
    sales_forecast_models_performance <- cbind(x_dimension1,x_dimension2,x_dimension3,
                                               x_dimension4,x_dimension5,
                                               SMA_Error,ETS_Error,Holtwinters_Error,ARIMA_Error,Recommended_Model)
    
    colnames(sales_forecast_models_performance) <- c("DIMENSION1","DIMENSION2","DIMENSION3","DIMENSION4","DIMENSION5",
                                                     "SMA","ETS","HOLT_WINTERS","ARIMA","RECOMMENDED_MODEL")
    
    row.names(sales_forecast_models_performance) <- NULL
    
    list.sales_forecast_models_mape[[i]] <- as.data.frame(sales_forecast_models_performance)
    
    list.sales_forecast_recommended[[i]] <- Recommended_Model_Data
    
                             }
  
  
  ######## Binding the list objects into a data frame
  
  SF_ARIMA_METHODS <- do.call(rbind, list.arima.method)
  
  Final_Data_arima <- do.call(rbind, list.sales_forecast_arima)
  
  #Final_Data_nnelm <- do.call(rbind, list.sales_forecast_nnelm)
  
  #Final_Data_nnmlp <- do.call(rbind, list.sales_forecast_nnmlp)
  
  Final_Data_ets <- do.call(rbind, list.sales_forecast_ets)
  
  Final_Data_sma <- do.call(rbind, list.sales_forecast_sma)
  
  Final_Data_holtwinters <- do.call(rbind, list.sales_forecast_holtwinters)
  
  sales_forecast_validation <- do.call(rbind, list.sales_forecast_validation)
  
  sales_forecast_recommended <- do.call(rbind, list.sales_forecast_recommended)
  
  sales_forecast_models_performance <- do.call(rbind,list.sales_forecast_models_mape)
  
  
  
  #######################################################################################
  ################# Loading the ARIMA forecasted data into DB AOL Tables
  #######################################################################################
  
  ####ARIMA###
  
  if(exists("Final_Data_arima") & nrow(Final_Data_arima)>0){
    print("Loading the Sales Forecast Output- ARIMA into AOL Table")
    dbSendQuery(mysql_db_connection,"truncate table AOL_Sales_Forecast_ARIMA")
    for(i in 1:nrow(Final_Data_arima)){
      dbSendQuery(mysql_db_connection,paste0("insert into AOL_Sales_Forecast_ARIMA",
                                             "(DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             YEAR,MONTH,FORECASTED_SALES) ",
                                             "values(",
                                             "'",Final_Data_arima$DIMENSION1[i],"'",",",
                                             "'",Final_Data_arima$DIMENSION2[i],"'",",",
                                             "'",Final_Data_arima$DIMENSION3[i],"'",",",
                                             "'",Final_Data_arima$DIMENSION4[i],"'",",",
                                             "'",Final_Data_arima$DIMENSION5[i],"'",",",
                                             Final_Data_arima$YEAR[i],",",
                                             Final_Data_arima$MONTH[i],",",
                                             Final_Data_arima$FORECASTED_SALES[i],")",sep=""))}
    print("Data Loading into AOL(ARIMA) Table is completed Successfully")
  } else {
    print("Sales Forecast Output(ARIMA) Result doesn't contain any Records")
  }
  
  ####ETS###
  
  if(exists("Final_Data_ets") & nrow(Final_Data_ets)>0){
    print("Loading the Sales Forecast Output- ETS into AOL Table")
    dbSendQuery(mysql_db_connection,"truncate table AOL_Sales_Forecast_ETS")
    for(i in 1:nrow(Final_Data_ets)){
      dbSendQuery(mysql_db_connection,paste0("insert into AOL_Sales_Forecast_ETS",
                                             "(DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             YEAR,MONTH,FORECASTED_SALES) ",
                                             "values(",
                                             "'",Final_Data_ets$DIMENSION1[i],"'",",",
                                             "'",Final_Data_ets$DIMENSION2[i],"'",",",
                                             "'",Final_Data_ets$DIMENSION3[i],"'",",",
                                             "'",Final_Data_ets$DIMENSION4[i],"'",",",
                                             "'",Final_Data_ets$DIMENSION5[i],"'",",",
                                             Final_Data_ets$YEAR[i],",",
                                             Final_Data_ets$MONTH[i],",",
                                             Final_Data_ets$FORECASTED_SALES[i],")",sep=""))}
    print("Data Loading into AOL(ETS) Table is completed Successfully")
  } else {
    print("Sales Forecast Output(ETS) Result doesn't contain any Records")
  }
  
  ####SMA###
  
  if(exists("Final_Data_sma") & nrow(Final_Data_sma)>0){
    print("Loading the Sales Forecast Output- SMA into AOL Table")
    dbSendQuery(mysql_db_connection,"truncate table AOL_Sales_Forecast_SMA")
    for(i in 1:nrow(Final_Data_sma)){
      dbSendQuery(mysql_db_connection,paste0("insert into AOL_Sales_Forecast_SMA",
                                             "(DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             YEAR,MONTH,FORECASTED_SALES) ",
                                             "values(",
                                             "'",Final_Data_sma$DIMENSION1[i],"'",",",
                                             "'",Final_Data_sma$DIMENSION2[i],"'",",",
                                             "'",Final_Data_sma$DIMENSION3[i],"'",",",
                                             "'",Final_Data_sma$DIMENSION4[i],"'",",",
                                             "'",Final_Data_sma$DIMENSION5[i],"'",",",
                                             Final_Data_sma$YEAR[i],",",
                                             Final_Data_sma$MONTH[i],",",
                                             Final_Data_sma$FORECASTED_SALES[i],")",sep=""))}
    print("Data Loading into AOL(SMA) Table is completed Successfully")
  } else {
    print("Sales Forecast Output(SMA) Result doesn't contain any Records")
  }
  
  
  
  ####HOLTWINTERS###
  
  if(exists("Final_Data_holtwinters") & nrow(Final_Data_holtwinters)>0){
    print("Loading the Sales Forecast Output- HOLTWINTERS into AOL Table")
    dbSendQuery(mysql_db_connection,"truncate table AOL_Sales_Forecast_HOLTWINTERS")
    for(i in 1:nrow(Final_Data_holtwinters)){
      dbSendQuery(mysql_db_connection,paste0("insert into AOL_Sales_Forecast_HOLTWINTERS",
                                             "(DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             YEAR,MONTH,FORECASTED_SALES) ",
                                             "values(",
                                             "'",Final_Data_holtwinters$DIMENSION1[i],"'",",",
                                             "'",Final_Data_holtwinters$DIMENSION2[i],"'",",",
                                             "'",Final_Data_holtwinters$DIMENSION3[i],"'",",",
                                             "'",Final_Data_holtwinters$DIMENSION4[i],"'",",",
                                             "'",Final_Data_holtwinters$DIMENSION5[i],"'",",",
                                             Final_Data_holtwinters$YEAR[i],",",
                                             Final_Data_holtwinters$MONTH[i],",",
                                             Final_Data_holtwinters$FORECASTED_SALES[i],")",sep=""))}
    print("Data Loading into AOL(HOLTWINTERS) Table is completed Successfully")
  } else {
    print("Sales Forecast Output(HOLTWINTERS) Result doesn't contain any Records")
  }
  
  
  ####Sales Forecast Validation###
  
  if(exists("sales_forecast_validation") & nrow(sales_forecast_validation)>0){
    print("Loading the Sales Forecast Methods Comparison Results into DB")
    #dbSendQuery(mysql_db_connection,"truncate table AI_Sales_Forecast_Validation")
    
    validation_RUN_ID <- dbGetQuery(mysql_db_connection,"select max(RUN_ID) from AI_Sales_Forecast_Validation")
    
    new_validation_RUN_ID <- if(is.na(validation_RUN_ID)){1}else{validation_RUN_ID+1}
    
    for(i in 1:nrow(sales_forecast_validation)){
      dbSendQuery(mysql_db_connection,paste0("insert into AI_Sales_Forecast_Validation",
                                             "(RUN_ID,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             YEAR,MONTH,ACTUAL_SALES,SMA,ETS,HOLT_WINTERS,
                                             ARIMA) ",
                                             "values(",
                                             new_validation_RUN_ID,",",
                                             "'",sales_forecast_validation$DIMENSION1[i],"'",",",
                                             "'",sales_forecast_validation$DIMENSION2[i],"'",",",
                                             "'",sales_forecast_validation$DIMENSION3[i],"'",",",
                                             "'",sales_forecast_validation$DIMENSION4[i],"'",",",
                                             "'",sales_forecast_validation$DIMENSION5[i],"'",",",
                                             sales_forecast_validation$YEAR[i],",",
                                             sales_forecast_validation$MONTH[i],",",
                                             sales_forecast_validation$ACTUAL_SALES[i],",",
                                             sales_forecast_validation$SMA[i],",",
                                             sales_forecast_validation$ETS[i],",",
                                             sales_forecast_validation$HOLT_WINTERS[i],",",
                                             sales_forecast_validation$ARIMA[i],")",sep=""))}
    print("Data Loading into Sales Forecast Validation Results Table is completed Successfully")
  } else {
    print("Sales Forecast Validation Results doesn't contain any Records")
  }
  
  
  ####Sales Forecast Model Performance###
  
  if(exists("sales_forecast_models_performance") & nrow(sales_forecast_models_performance)>0){
    print("Loading the Sales Forecast Models Performance Results into DB")
    #dbSendQuery(mysql_db_connection,"truncate table AI_Sales_Forecast_Models_MAPE")
    
    mape_RUN_ID <- dbGetQuery(mysql_db_connection,"select max(RUN_ID) from AI_Sales_Forecast_Models_MAPE")
    
    new_mape_RUN_ID <- if(is.na(mape_RUN_ID)){1}else{mape_RUN_ID+1}
    
    for(i in 1:nrow(sales_forecast_models_performance)){
      dbSendQuery(mysql_db_connection,paste0("insert into AI_Sales_Forecast_Models_MAPE",
                                             "(RUN_ID,DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             SMA,ETS,HOLT_WINTERS,
                                             ARIMA,RECOMMENDED_MODEL) ",
                                             "values(",
                                             new_mape_RUN_ID,",",
                                             "'",sales_forecast_models_performance$DIMENSION1[i],"'",",",
                                             "'",sales_forecast_models_performance$DIMENSION2[i],"'",",",
                                             "'",sales_forecast_models_performance$DIMENSION3[i],"'",",",
                                             "'",sales_forecast_models_performance$DIMENSION4[i],"'",",",
                                             "'",sales_forecast_models_performance$DIMENSION5[i],"'",",",
                                             sales_forecast_models_performance$SMA[i],",",
                                             sales_forecast_models_performance$ETS[i],",",
                                             sales_forecast_models_performance$HOLT_WINTERS[i],",",
                                             sales_forecast_models_performance$ARIMA[i],",",
                                             "'",sales_forecast_models_performance$RECOMMENDED_MODEL[i],"'",
                                             ")",sep=""))}
    print("Data Loading into Sales Forecast Validation Results Table is completed Successfully")
  } else {
    print("Sales Forecast Validation Results doesn't contain any Records")
  }
  
  
  #######################################################################################
  ################# Loading the forecasted data into DB AOL Table
  #######################################################################################
  
  final_sales_forecast_data <- sqldf("select x.*
                                     from
                                     (select a.* from sales_forecast_recommended a
                                     inner join Valid_Data b
                                     on a.DIMENSION1=b.DIMENSION1
                                     and a.DIMENSION2=b.DIMENSION2
                                     and a.DIMENSION3=b.DIMENSION3
                                     and a.DIMENSION4=b.DIMENSION4
                                     and a.DIMENSION5=b.DIMENSION5
                                     where (a.YEAR=b.MAX_YEAR and a.MONTH>b.CURRENT_YEAR_MAX_MONTH)
                                     union
                                     select a.* from sales_forecast_recommended a
                                     inner join Valid_Data b
                                     on a.DIMENSION1=b.DIMENSION1
                                     and a.DIMENSION2=b.DIMENSION2
                                     and a.DIMENSION3=b.DIMENSION3
                                     and a.DIMENSION4=b.DIMENSION4
                                     and a.DIMENSION5=b.DIMENSION5
                                     where (a.YEAR>b.MAX_YEAR)) x
                                     order by x.DIMENSION1,x.DIMENSION2,x.DIMENSION3,x.DIMENSION4,x.DIMENSION5,x.YEAR,x.MONTH")
  
  aol_table_name <- as.character(AI_Metadata_Details$AOL_TABLE)
  
  
  if(exists("final_sales_forecast_data") & nrow(final_sales_forecast_data)>0){
    print("Loading the Recommended Sales Forecast Output into AOL Table")
    dbSendQuery(mysql_db_connection,paste0("truncate table ",aol_table_name,sep=""))
    for(i in 1:nrow(final_sales_forecast_data)){
      dbSendQuery(mysql_db_connection,paste0("insert into ", aol_table_name,
                                             "(
                                             DIMENSION1,DIMENSION2,DIMENSION3,DIMENSION4,
                                             DIMENSION5,
                                             YEAR,MONTH,FORECASTED_SALES) ",
                                             "values(",
                                             "'",final_sales_forecast_data$DIMENSION1[i],"'",",",
                                             "'",final_sales_forecast_data$DIMENSION2[i],"'",",",
                                             "'",final_sales_forecast_data$DIMENSION3[i],"'",",",
                                             "'",final_sales_forecast_data$DIMENSION4[i],"'",",",
                                             "'",final_sales_forecast_data$DIMENSION5[i],"'",",",
                                             final_sales_forecast_data$YEAR[i],",",
                                             final_sales_forecast_data$MONTH[i],",",
                                             final_sales_forecast_data$FORECASTED_SALES[i],")",sep=""))}
    print("Data Loading into AOL Table is completed Successfully")
  } else {
    print("Recommended Sales Forecast Output Result doesn't contain any Records")
  }
  
  
  #######################################################################################
  ############## Conditional Execution End Statements
  #######################################################################################
  
                             } else {
                               
                               print("Source Query Result set doesn't have any Records")
                               
                               print("Please Check the Staging SQL Query in the Source Definition Table")
                               
                             }

##################################################################################################
############  Closing the Active Connections and clearing the workspace if needed
#######################################################################################
end_time <- Sys.time()

cat("The Execution Time for the Model is:", end_time-start_time, "Seconds")

print("Disconnecting the open MYSQL Connections")
dbDisconnect(mysql_db_connection)
dbDisconnect(mysql_staging_db_connection)
dbDisconnect(mysql_app_db_connection)

####### Turning off the Log Sinking
print("Closing the Log Sink Connection to the File")
sink()

#######################################################################################
##################################### End of the Code 
#######################################################################################