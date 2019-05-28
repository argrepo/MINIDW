#########################################################
############## Getting the AI Model Metadata

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