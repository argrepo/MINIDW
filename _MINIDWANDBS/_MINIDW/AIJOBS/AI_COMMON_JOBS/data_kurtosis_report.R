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