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

#######################################################################