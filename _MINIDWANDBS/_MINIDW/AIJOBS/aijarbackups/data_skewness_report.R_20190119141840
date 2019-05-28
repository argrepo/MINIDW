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