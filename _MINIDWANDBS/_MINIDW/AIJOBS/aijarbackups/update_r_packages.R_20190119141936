#################################################################################
############ R job to update the outdated Packages #########################
#####################################################################################

############# Reading the Input Parameters

Business_Problem <- "Updating_R_Packages"
Model_Name <- "R_Packages_Update"

staging_db_host <- "172.25.1.233"
staging_db_port <- 4475
staging_db_name <- "AI_Internal_1009187_staging"
staging_db_user_name <- "alinternal62"
staging_db_password <- "al6262"


############# Setting the Options

options(repos=structure(c(CRAN="https://cran.rstudio.com/")))

cores <- parallel::detectCores()

options(Ncpus = cores)

pdf(NULL)

rm(list=ls())

##############################################################################
############# Function to detach all the packages

detachAllPackages <- function(keep = NULL, keep.basic = TRUE) {
  # function for detaching all attached packages (except basic ones)
  basic.packages <- c("package:stats","package:graphics","package:grDevices",
                      "package:utils","package:datasets","package:methods",
                      "package:base")
  package.list <- search()[ifelse(unlist(gregexpr("package:", search())) == 1,
                                  TRUE, FALSE)]
  if (!is.null(keep)){
    package.list <- setdiff(package.list, paste("package", keep, sep = ":"))
  }
  if (keep.basic){
    package.list <- setdiff(package.list, basic.packages)
  }
  if (length(package.list) > 0) {
    for (package in package.list) detach(package, character.only = TRUE)
  }
}
######################## End of the Function ################################

###################################################################################
######################## Creating a Log File with the entire console execution
#############################################################################################

timestamp <- gsub(pattern=" ","",gsub(pattern = "[[:punct:]]","",as.character(Sys.time())))

log_file_name <- paste(gsub(pattern = " ","_",Business_Problem),'_',gsub(pattern = " ","_",Model_Name),"_",timestamp,".log",sep="")

#### Sinking the console output to the Log File
#sink(paste(log_file_path,"/",log_file_name,sep=""))

print(paste0("Log File Name: ",log_file_name,sep=""))

#### Capturing the start time of the code execution

start_time <- Sys.time()


##################################################################
#### Getting the List of Installed R Packages

get_installed_packages <- as.data.frame(installed.packages())

row.names(get_installed_packages) <- NULL

get_installed_packages <- unique(select(get_installed_packages,Package,Version))

### Printing the Number of Packages Installed

print(paste("Number of Installed Packages: ",nrow(get_installed_packages),sep=""))

print(get_installed_packages)

#################################################################
#### Getting the List of outdated Packages

get_outdated_packages <- as.data.frame(old.packages())

row.names(get_outdated_packages) <- NULL

get_outdated_packages <- unique(select(get_outdated_packages,Package,Installed,ReposVer))

colnames(get_outdated_packages) <- c("PACKAGE","INSTALLED_VERSION","AVAILABLE_VERSION")

print(paste("Number of Packages to be updated: ",nrow(get_outdated_packages),sep=""))

print(get_outdated_packages)

##########################################################################
######### Detaching all the packages to ensure no issues while updating

detachAllPackages(keep.basic = TRUE)

#########################################################################
############ Updating the Packages

print("Process of updating the outdated Packages Started")

for(i in 1:nrow(get_outdated_packages[1:2,]))
  
{
  
  in_package <- get_outdated_packages$PACKAGE[i]
  
  install.packages(in_package,type="source")
  
  print(paste(in_package," Package Updated Successfully"))
  
  
}

#######################################################################################

end_time <- Sys.time()

execution_duration <- round(as.numeric(difftime(end_time, start_time,units="mins")),2)

cat("The Execution Time(in Minutes) for the Model is:", execution_duration)

sink()

################ End of the Code ##########################################