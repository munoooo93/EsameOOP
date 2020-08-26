package it.esame.EsameOOP.controller;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import it.esame.EsameOOP.model.FileInfo;
import it.esame.EsameOOP.utils.Dropbox;

@RestController
public class APIController {
	@PostMapping("/data")
	public String getData(@RequestBody String body) {
		String requestPath = "";
		try {
			JSONObject bodyJson = new JSONObject(body);
			requestPath = bodyJson.getString("path");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String apiResponse = Dropbox.getData(requestPath);
		
		try {
			JSONArray entries = new JSONObject(apiResponse).getJSONArray("entries");
			
			LinkedList<FileInfo> list = FileInfo.listFromApiJson(entries);
		
			JSONArray result = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				result.put(list.get(i).getFileBasicInfo());
			}
			
			return result.toString();
			
		} catch (JSONException ex) {
			return ex.getMessage();
		}
	}
	
	@GetMapping("/metadata")
	public String getMetadata() {
		JSONArray metadata = new JSONArray();
		
		try {
			metadata.put(new JSONObject()
						.put("sourceField", "name")
						.put("type", "string"))
					.put(new JSONObject()
						.put("sourceField", "path")
						.put("type", "string"))
					.put(new JSONObject()
						.put("sourceField", "size")
						.put("type", "integer"))
					.put(new JSONObject()
						.put("sourceField", "deleted")
						.put("type", "boolean"))
					.put(new JSONObject()
						.put("sourceField", "downloadable")
						.put("type", "boolean"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return metadata.toString();
	}
	//@GetMapping("/stats/")
}