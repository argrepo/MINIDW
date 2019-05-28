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