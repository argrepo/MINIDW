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