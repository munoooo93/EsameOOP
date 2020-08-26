package it.esame.EsameOOP.utils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Dropbox {
	public static String getData(String path) {
		OkHttpClient client = new OkHttpClient.Builder()
				.build();
		
		
		String jsonApiBody = "{\"path\":\"" + path + "\", \"recursive\": true,\n" + 
				"    \"include_media_info\": true,\n" + 
				"    \"include_deleted\": true,\n" + 
				"    \"include_has_explicit_shared_members\": true,\n" + 
				"    \"include_mounted_folders\": true,\n" + 
				"    \"include_non_downloadable_files\": true}";
		
		RequestBody body = RequestBody.create(MediaType.parse("application"), jsonApiBody);
		Request request = new Request.Builder()
				.url("https://api.dropboxapi.com/2/files/list_folder")
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer hp2ex-qDDuQAAAAAAAAAAc_mzi1j7CIZ-C3pUTzm34N7tvyBtpSHLaT70SekZOrT")
				.post(body)
				.build();
		
		Response response;
		
		String res = null;
		try {
			response = client.newCall(request).execute();
			res = response.body().string();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
