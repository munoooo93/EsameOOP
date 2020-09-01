package it.esame.EsameOOP.controller;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.esame.EsameOOP.model.FileInfo;
import it.esame.EsameOOP.model.ExtensionStats;
import it.esame.EsameOOP.utils.Dropbox;

/**
 * @author Emanuele Ballarini
 */

/**
 * Classe che dichiara le route disponibili
 */
@RestController
public class APIController {
	/**
	 * Route che, data una cartella, ne elenca i file contenuti con relativi metadati, cercando anche nelle sottocartelle
	 * @param body	corpo della richiesta: <i>path</i>:
	 * 				percorso della cartella da analizzare. Per la root inserire una stringa vuota
	 * @return	JSON contenente i dati relativi ai file presenti
	 * 			e cancellati in una data cartella (e relative sottocartelle)
	 */
	@PostMapping("/data")
	public String getData(@RequestBody String body) {
		String requestPath = "";
		try {
			JSONObject bodyJson = new JSONObject(body);
			requestPath = bodyJson.getString("path");
			if ((requestPath.length() != 0) && (requestPath.charAt(requestPath.length() - 1) != '/')) {
				requestPath += "/";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String apiResponse = Dropbox.getData(requestPath);
		String response = "";
		try {
			JSONArray entries = new JSONObject(apiResponse).getJSONArray("entries");
			
			LinkedList<FileInfo> list = FileInfo.listFromApiJson(entries);
		
			JSONArray result = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				result.put(list.get(i).getInfo());
			}
			
			response = result.toString();
			
		} catch (JSONException ex) {
			response = "[]";
		} catch (NullPointerException n) {
			response = "{ \"error\": \"unable to retrieve informations from Dropbox\" }";
		}
		
		return response;
	}
	
	/**
	 * Route che permette di ricevere informazioni relative al formato del JSON restituito dalla route '/data'
	 * @return JSON contenente le informazioni relative agli attributi restituiti da '/data'
	 */
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
							.put("sourceField", "ext")
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
	
	/**
	 * Route che permette di ricevere statistiche relative alle varie estensioni di file
	 * @param includeDeleted parametro che permette di scegliere se escludere o no i file eliminati dalle statistiche
	 * @return JSON contenente le statistiche
	 */
	@GetMapping("/stats/overall")
	public String getStats(@RequestParam(name="includeDeleted", defaultValue="false") boolean includeDeleted) {
		String apiResponse = Dropbox.getData("");
		String response = "";
		
		try {
			JSONArray entries = new JSONObject(apiResponse).getJSONArray("entries");
			
			LinkedList<FileInfo> list = FileInfo.listFromApiJson(entries);
			LinkedList<ExtensionStats> stats = ExtensionStats.getStatsFromFiles(list, includeDeleted);
			
			JSONArray result = new JSONArray();
			for (ExtensionStats f: stats) {
				result.put(f.toJSONObject());
			}
			
			response = result.toString();
			
		} catch (JSONException ex) {
			response = "[]";
		} catch (NullPointerException n) {
			response = "{ \"error\": \"unable to retrieve informations from Dropbox\" }";
		}
		
		return response;
	}
	
	/**
	 * Route che permette di ricevere statistiche relative alle varie estensioni di file per una singola cartella
	 * @param body Corpo della richiesta
	 * @return JSON contenente le statistiche
	 */
	@PostMapping("/stats")
	public String getFolderStats(@RequestBody String body) {
		String requestPath = "";
		boolean includeDeleted = false;
		try {
			JSONObject bodyJson = new JSONObject(body);
			requestPath = bodyJson.getString("path");
			if ((requestPath.length() != 0) && (requestPath.charAt(requestPath.length() - 1) != '/')) {
				requestPath += "/";
			}
			includeDeleted = bodyJson.getBoolean("include-deleted");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		String apiResponse = Dropbox.getData(requestPath);
		String response = "";
		try {
			JSONArray entries = new JSONObject(apiResponse).getJSONArray("entries");
			
			LinkedList<FileInfo> list = FileInfo.listFromApiJson(entries);			
			LinkedList<ExtensionStats> stats = ExtensionStats.getStatsFromFolder(list, includeDeleted, requestPath);
			
			JSONArray result = new JSONArray();
			for (ExtensionStats f: stats) {
				result.put(f.toJSONObject());
			}
			
			response = result.toString();
			
		} catch (JSONException ex) {
			response = "[]";
		} catch (NullPointerException n) {
			response = "{ \"error\": \"unable to retrieve informations from Dropbox\" }";
		}
		
		return response;
	}
}