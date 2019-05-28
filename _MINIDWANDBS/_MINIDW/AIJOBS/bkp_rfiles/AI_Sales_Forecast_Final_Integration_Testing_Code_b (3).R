##################### Connection to DB Function

#################### Input to the Function #######################
########### Reading the DB connection parameters

#Rscript.exe D:\arg_test.R "192.168.0.143" 4475 "datamodel_2_1_0_1009009_staging" "datamodel" "dm_2_0_0_etl"
#Rscript.exe D:\arg_test.R param.csv

args <- commandArgs(TRUE)

#db_host <- "192.168.0.143"
#db_port <- 4475
#db_name <- "datamodel_2_1_0_1009009_staging"
#db_user_name <- "datamodel"
#db_password <- "dm_2_0_0_etl"

if(!require(RMySQL)) install.packages("RMySQL"); library(RMySQL)
if(!require(DBI)) install.packages("DBI"); library(DBI)

parameter_file_name <- args[1]


##### Reading the Parameter File
param_input <- read.csv(parameter_file_name,header=TRUE,stringsAsFactors = FALSE)

print("Preview of the Parameter File")
head(param_input)

db_host <- as.character(param_input$VALUE[param_input$PARAM=="db_host"])
db_port <- as.integer(as.character(param_input$VALUE[param_input$PARAM=="db_port"]))
db_name <- as.character(param_input$VALUE[param_input$PARAM=="db_name"])
db_user_name <- as.character(param_input$VALUE[param_input$PARAM=="db_user_name"])
db_password <- as.character(param_input$VALUE[param_input$PARAM=="db_password"])

#db_host <- args[1]
#db_port <- as.integer(args[2])
#db_name <- args[3]
#db_user_name <- args[4]
#db_password <- args[5]

##################################################################

####################################################################
################### connect_mysql_db function ######################
##########################################################################

connect_mysql_db <- function(db_host,
                             db_port,
                             db_name,
                             db_user_name,
                             db_password) {

  ########### Establishing the DB Connection 
  
  mysql_db_conn <-  dbConnect(MySQL(), 
            user=db_user_name, 
            password=db_password, 
            dbname=db_name, 
            host=db_host, 
            port=db_port)
  
  
  ########### Checking the DB connection
  
  if(dbIsValid(mysql_db_conn)==TRUE){
    print(paste0("DB Connection to ", db_name," Successfully Established"))
  } else {
    print(paste0("DB Connection to ", db_name," couldn't be Established"))
    print("Please check the Input DB Parameters")
  }
  
  return(mysql_db_conn)
  
}


########################## End of the Function ##############################

###################################################################
################### Testing the DB Connect Function 

mysql_db_conn <- connect_mysql_db(db_host,db_port,db_name,db_user_name,db_password)

############################################################################
