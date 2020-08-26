package it.esame.EsameOOP.controller;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import it.esame.EsameOOP.model.FileInfo;
import it.esame.EsameOOP.utils.Dropbox;

@RestController
public class APIController {
	@PostMapping("/data")
	public String getData() {
		String apiResponse = Dropbox.getData("");
		
		try {
			JSONArray entries = new JSONObject(apiResponse).getJSONArray("entries");
			
			LinkedList<FileInfo> list = FileInfo.listFromApiJson(entries);
		
			JSONArray result = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				result.put(list.get(i).getFileBasicInfo());
			}
			
			return result.toString();
			
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	//@PostMapping("/metadata")
	//@GetMapping("/stats/")
}