package com.anvizent.minidw.service.utils.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import com.datamodel.anvizent.helper.minidw.Constants;
import com.datamodel.anvizent.service.model.WebServiceApi;

public class WebServiceJSONWriter
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void writeObjectToJson(String filePath, List dataList) throws JsonGenerationException, JsonMappingException, IOException
	{
		if( dataList != null && dataList.size() > 0 )
		{
			int size = dataList.size();
			Iterator<Map<String, Object>> iterator = dataList.iterator();
			try ( FileWriter file = new FileWriter(filePath))
			{
				int i = 0;
				while (iterator.hasNext())
				{
					ObjectMapper mapperObj = new ObjectMapper();
					String jsonResp = "";
					if( i == 0 ) jsonResp += "[";
					jsonResp += mapperObj.writeValueAsString(iterator.next());
					jsonResp += size != i ? "," : "]";
					file.write(jsonResp);
					i++;
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void writeObjectToJson(FileWriter fileWriter, List dataList, boolean isResultSetCompleted) throws JsonGenerationException, JsonMappingException, IOException
	{
		if( dataList != null && dataList.size() > 0 )
		{
			int dataListSize = dataList.size();
			if( dataListSize == 1 )
			{
				Iterator<Map<String, Object>> iterator = dataList.iterator();
				while (iterator.hasNext())
				{
					ObjectMapper mapperObj = new ObjectMapper();
					String jsonResp = mapperObj.writeValueAsString(iterator.next());
					fileWriter.write(jsonResp);
				}
			}
			else
			{
				fileWriter.write("[");
				Iterator<Map<String, Object>> iterator = dataList.iterator();
				int i = 1;
				while (iterator.hasNext())
				{
					ObjectMapper mapperObj = new ObjectMapper();
					String jsonResp = mapperObj.writeValueAsString(iterator.next());
					if( !isResultSetCompleted )
					{
						if(i != dataListSize )
						{
							jsonResp += ",";
						}
					}
					else
					{
						jsonResp += ",";
					}
					fileWriter.write(jsonResp);
					i++;
				}
				fileWriter.write("]");
			}

		}
	}

	public static void writeObjectToJson(FileWriter fileWriter, Object object) throws JsonGenerationException, JsonMappingException, IOException
	{
		if( object != null )
		{
			if( object instanceof Map )
			{
				String jsonString = "";
				ObjectMapper mapperObj = new ObjectMapper();
				jsonString = mapperObj.writeValueAsString(object);
				jsonString += ",";
				fileWriter.write(jsonString);
			}
		}
	}

	public static void writeObjectToJson(String filePath, String character) throws JsonGenerationException, JsonMappingException, IOException
	{
		if( character != null )
		{
			try ( FileWriter file = new FileWriter(filePath))
			{
				file.write(character);
			}
		}
	}

	public static String getFilePathForWsApi(WebServiceApi webServiceApi, String filePath, String fileType)
	{

		if( StringUtils.isBlank(filePath) )
		{
			filePath = Constants.Temp.getTempFileDir();
		}
		String fileDir = CommonUtils.createDir(filePath + "fileMappingWithIL/");
		String newfilename = webServiceApi.getApiName().replaceAll("\\s+", "_") + "_" + CommonUtils.generateUniqueIdWithTimestamp();
		filePath = fileDir + newfilename + "." + fileType;
		File file = new File(filePath);
		if( !file.exists() )
		{
			try
			{
				file.createNewFile();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}

		return filePath;
	}

	public static String getFilePathForWsApi(String fileName, String fileDir, String fileType)
	{

		String filePath = null;
		String newfilename = fileName.replaceAll("\\s+", "_") + "_" + CommonUtils.generateUniqueIdWithTimestamp();
		filePath = fileDir + newfilename + "." + fileType;
		File file = new File(filePath);
		if( !file.exists() )
		{
			try
			{
				file.createNewFile();
			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}
		}

		return filePath;
	}

	public static long getFileFolderSize(File dir)
	{
		long size = 0;
		if( dir.isDirectory() )
		{
			for (File file : dir.listFiles())
			{
				if( file.isFile() )
				{
					size += file.length();
				}
				else size += getFileFolderSize(file);
			}
		}
		else if( dir.isFile() )
		{
			size += dir.length();
		}
		return size;
	}

	public static void closeJsonfileWriter(FileWriter jsonfileWriter)
	{
		try
		{
			if( jsonfileWriter != null )
			{
				jsonfileWriter.close();
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
}
