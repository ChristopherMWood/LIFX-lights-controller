package lifx;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.Response;

public class LIFXHttpService implements LIFXService
{
	private HTTPRequestService requestService;
	
	public LIFXHttpService(HTTPRequestService requestService)
	{
		this.requestService = requestService;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<LIFXZStrip> GetAvailableZStrips()
	{		
		ArrayList<LIFXZStrip> smartStrips = new ArrayList<LIFXZStrip>();
		
		try
		{
			Response response = requestService.Get("https://api.lifx.com/v1/lights/all");
			
			JSONParser parser = new JSONParser();
			JSONArray deviceList = (JSONArray) parser.parse(response.body().string());
			ArrayList<JSONObject> allDevices = (ArrayList<JSONObject>)deviceList;
			
			for (JSONObject device : allDevices)
			{
				JSONObject light = (JSONObject)device.get("product");
				JSONObject zones = (JSONObject)device.get("zones");
				//Long zoneCount =  zones.get("count");
				if (((String)light.get("identifier")).equals("lifx_z2") && (boolean)device.get("connected"))
					smartStrips.add(new LIFXZStrip((String)device.get("id"), (String)device.get("label"),
							32, device.toJSONString(), requestService));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return smartStrips;
	}
}
