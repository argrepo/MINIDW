package com.anvizent.client.data.to.csv.path.converter.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.anvizent.client.data.to.csv.path.converter.exception.QueryParcingException;
import com.anvizent.minidw.client.jdbc.utils.ClientJDBCUtil;
import com.datamodel.anvizent.service.model.ILConnectionMapping;

public class SQLUtil {
	public static Connection getConnectionForConnectorID(int connectorID, ILConnectionMapping iLConnectionMapping)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection connection = null;

		String server = null;
		String userName = null;
		String password = null;
        String driverName = null;
		String protocal = null;
     	boolean sslEnable = false;
		String sslParams = null; 
		
		if (iLConnectionMapping != null) {
			protocal = 	iLConnectionMapping.getiLConnection().getDatabase().getProtocal();
			userName = iLConnectionMapping.getiLConnection().getUsername();
			password = iLConnectionMapping.getiLConnection().getPassword();
			driverName = iLConnectionMapping.getiLConnection().getDatabase().getDriverName();
			server = iLConnectionMapping.getiLConnection().getServer();
			sslEnable=iLConnectionMapping.getiLConnection().isSslEnable();
			sslParams=iLConnectionMapping.getiLConnection().getSslTrustKeyStoreFilePaths(); 
			connection = ClientJDBCUtil.getClientDataBaseConnection(connectorID, server, userName, password,driverName,protocal ,sslEnable,sslParams );

		}
		return connection;
	}

	public static String[] getColumnLabelNames(ResultSet rs) throws SQLException {
		List<String> names = new ArrayList<>();
		ResultSetMetaData metadata = rs.getMetaData();

		for (int i = 0; i < metadata.getColumnCount(); i++) {
			names.add(metadata.getColumnLabel(i + 1));
		}

		String[] nameArray = new String[names.size()];
		return names.toArray(nameArray);
	}

	public static String replaceDateValue(String incrementalUpdateDate, String ilQuery) throws QueryParcingException {
		if (incrementalUpdateDate == null) {
			return ilQuery;
		} else {

			int startIndex = ilQuery.indexOf("/*");
			int endIndex = 0;
			boolean dateFound = false;

			while (startIndex != -1) {
				endIndex = ilQuery.indexOf("*/", startIndex);

				if (endIndex == -1) {
					ilQuery += "*/";
					endIndex = ilQuery.length() - 2;
					break;
				}
				if (ilQuery.substring(startIndex, endIndex).contains("{date}")) {
					dateFound = true;
					String commentedQuery = ilQuery.substring(startIndex + 2, endIndex).replace("{date}",
							"'" + incrementalUpdateDate + "'");
					ilQuery = ilQuery.substring(0, startIndex) + commentedQuery + ilQuery.substring(endIndex + 2);
				}
				startIndex = ilQuery.indexOf("/*", endIndex);
			}

			if (!dateFound) {
				throw new QueryParcingException("Commented incremental query part not found");
			}

			return ilQuery;
		}
	}
}
