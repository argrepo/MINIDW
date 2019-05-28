package minidwclientws;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.anvizent.minidw.service.utils.helper.ApiJsonFlatten;
import com.datamodel.anvizent.service.model.WebService;
 
/**
 * @author Crunchify.com
 */
 
public class WriteApiSampleDataToFile {
 
    public static void main(String[] args) throws org.json.simple.parser.ParseException { 
    {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        List<LinkedHashMap<String, Object>> formattedApiResponse = null;
        try (FileReader reader = new FileReader("C:\\Users\\mahender.alaveni\\Desktop\\response.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            
            JSONObject jSONObject = (JSONObject) obj;
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object>   responseMap = mapper.readValue(jSONObject.toJSONString(), new TypeReference<Map<String, Object>>(){});
            
            List<Object> dataList = new ArrayList<>();
			dataList.add(responseMap);
			formattedApiResponse = new ApiJsonFlatten(dataList).getFlattenJson();
		    formattedApiResponse = getResultsFromApiResponse(formattedApiResponse);
		    
		    WebService webService = new WebService();
		    webService.setApiName("Product");
		   // String filePath = CommonUtils.getFilePathForWebServiceApi(webService, formattedApiResponse);
		    
		   // System.out.println(filePath);
            System.out.println(formattedApiResponse.get(0));
            
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }
    }
    public static List<LinkedHashMap<String, Object>> getResultsFromApiResponse(List<LinkedHashMap<String, Object>> flatJson)
	{

		List<LinkedHashMap<String, Object>> formattedApiResponse = new ArrayList<>();
		List<String> headers = new ArrayList<>();
		LinkedHashMap<String, Object> mainHeaders = new LinkedHashMap<String, Object>();
		for (LinkedHashMap<String, Object> h : flatJson)
		{
			h.forEach((k, v) ->
			{
				if( !headers.contains(k) )
				{
					headers.add(k);
					mainHeaders.put(k, "");
				}
			});
		}
		formattedApiResponse.add(mainHeaders);
		int headersSize = headers.size();

		for (LinkedHashMap<String, Object> data1 : flatJson)
		{
			LinkedHashMap<String, Object> currentData = new LinkedHashMap<String, Object>();
			Object currentValue = null;
			int i = 0;
			do
			{
				String mainHeader = headers.get(i);
				for (Map.Entry<String, Object> entry : data1.entrySet())
				{

					String key = entry.getKey();
					Object value = entry.getValue();
					if( value != null && value.toString().contains("/Date(") )
					{
						String date = value.toString();
						date = date.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
						Long dateInMilliseconds = Long.parseLong(date);
						SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String s = f.format(new Date(dateInMilliseconds));
						value = s;
					}
					if( mainHeader.equals(key) )
					{
						currentValue = value;
					}

				}
				i++;
				currentData.put(mainHeader, currentValue);
				currentValue = "";
			}
			while (i < headersSize);

			formattedApiResponse.add(currentData);
		}

		return formattedApiResponse;
	}
    
    }
