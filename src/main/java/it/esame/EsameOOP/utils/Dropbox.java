package it.esame.EsameOOP.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author		Emanuele Ballarini
 */

/**
 * Classe che espone i metodi di connessione alle API di Dropbox
 * 
 * @implNote	Al fine del progetto è stato creato un account di Dropbox ed ottenuto il token per effettuare le chiamate
 */
public class Dropbox {
	/**
	 * Metodo statico che effettua una chiamata alle API di Dropbox
	 * al fine di ottenere la lista del contenuto di una cartella
	 * 
	 * @param	path	Cartella da analizzare, se il parametro è una stringa vuota, si cercherà nella root
	 * @return	Risposta delle API di Dropbox <i><b>list_folder</b></i>
	 */
	public static String getData(String path) {
		// Crea un oggetto OkHttpClient per effettuare la chiamata alle API di Dropbox
		OkHttpClient client = new OkHttpClient.Builder()
				.build();
		
		// Genera il body della richiesta
		String jsonApiBody = "{\"path\":\"" + path + "\", \"recursive\": true,\n" +
				"    \"include_deleted\": true,\n" + 
				"    \"include_has_explicit_shared_members\": true,\n" + 
				"    \"include_mounted_folders\": true,\n" + 
				"    \"include_non_downloadable_files\": true}";
		
		// Crea la richiesta vera e propria
		RequestBody body = RequestBody.create(MediaType.parse("application"), jsonApiBody);
		Request request = new Request.Builder()
				.url("https://api.dropboxapi.com/2/files/list_folder")
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer hp2ex-qDDuQAAAAAAAAAAc_mzi1j7CIZ-C3pUTzm34N7tvyBtpSHLaT70SekZOrT")
				.post(body)
				.build();
		
		String res = null;
		try {
			// Effettua la richiesta al server
			Response response;
			response = client.newCall(request).execute();
			res = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * Metodo statico che effettua una chiamata alle API di Dropbox
	 * al fine di ottenere i metadati di un file
	 * 
	 * @apiNote	Utilizzato solo per ottenere i metadati dei file cancellati
	 * 
	 * @param	filepath	Percorso del file di cui si vogliono sapere i metadati
	 * @return	Risposta delle API di Dropbox <i><b>list_revisions</b></i>
	 */
	public static String getDeletedMetadata(String filepath) {
		OkHttpClient client = new OkHttpClient.Builder()
				.build();
		
		
		String jsonApiBody = "{\"path\":\"" + filepath + "\",\n" +
				"	 \"mode\": \"path\",\n" + 
				"    \"limit\": 1}";
		
		RequestBody body = RequestBody.create(MediaType.parse("application"), jsonApiBody);
		Request request = new Request.Builder()
				.url("https://api.dropboxapi.com/2/files/list_revisions")
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer hp2ex-qDDuQAAAAAAAAAAc_mzi1j7CIZ-C3pUTzm34N7tvyBtpSHLaT70SekZOrT")
				.post(body)
				.build();
		
		String res = null;
		try {
			Response response;
			response = client.newCall(request).execute();
			res = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
