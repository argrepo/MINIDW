####################################################
###################### Handling Outliers ###############
###############################################

data_outliers_impute <- function(input_column,impute_method) {
  library(outliers)
  negative_outlier <- outliers::outlier(input_column)
  positive_outlier <- outliers::outlier(input_column,opposite = TRUE)
  in_mean <- mean(input_column,na.rm = TRUE)
  in_median <- median(input_column,na.rm=TRUE)
  if(impute_method=="mean") {
    input_column[input_column==negative_outlier] <- in_mean
    input_column[input_column==positive_outlier] <- in_mean
    return(input_column)
  } else if (impute_method=="median") {
    input_column[input_column==negative_outlier] <- in_median
    input_column[input_column==positive_outlier] <- in_median
    return(input_column)
  } else {
    print("Please provide a valid Imputation Method:mean/median")
  }
}


############################################################################
###############################################################################