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