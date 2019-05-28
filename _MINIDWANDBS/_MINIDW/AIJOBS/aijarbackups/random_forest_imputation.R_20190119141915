#################################################################################
################ Function for Imputing Missing Values using MICE #########
#############################################################################

random_forest_imputation <- function(input_dataframe,max_iterations,fixed_seed) {
  
  if(!require("mice")) install.packages("mice"); library(mice)
  
  random_forest_imputation_method <- mice(data=input_dataframe,maxit = max_iterations,method="rf",
                                          seed=fixed_seed)
  
  random_forest_imputed_data <- as.data.frame(mice::complete(random_forest_imputation_method,max_iterations))
  
  return(random_forest_imputed_data)
  
}


########################### End of Function ##############################