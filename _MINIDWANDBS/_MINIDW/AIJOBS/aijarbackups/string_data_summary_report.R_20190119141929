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