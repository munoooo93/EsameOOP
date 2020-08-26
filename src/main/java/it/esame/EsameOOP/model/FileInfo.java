package it.esame.EsameOOP.model;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FileInfo {
	private String	name;
	private String	path;
	private long	size;
	private boolean	deleted;
	private boolean downloadable;
	
	public FileInfo(String name, String path, long size, boolean deleted, boolean downloadable) {
		super();
		this.name = name;
		this.path = path;
		this.size = size;
		this.deleted = deleted;
		this.downloadable = downloadable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}
	
	
	public JSONObject getFileBasicInfo() throws JSONException {
		JSONObject basicInfo = new JSONObject();
		
		/*
		 * presenza
		 * dimensioni
		 * 
		 * cancellati (relative dimensioni)
		 * 
		 **/
		
		
		basicInfo.put("name", this.getName());
		basicInfo.put("path", this.getPath());
		basicInfo.put("size", this.getSize());
		basicInfo.put("deleted", this.isDeleted());
		
		return basicInfo;
	}
	
	private static FileInfo fromApiJson(JSONObject o) {
		
		FileInfo result = null;
		
		try {
			result = new FileInfo(
						o.getString("name"),
						o.getString("path_display"),
						o.getLong("size"),
						(o.getString(".tag").equals("deleted")) ? true : false,
						o.getBoolean("is_downloadable")				
					);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static LinkedList<FileInfo> listFromApiJson(JSONArray entries) {
		LinkedList<FileInfo> collection = new LinkedList<>();
		
		for (int i = 0; i < entries.length(); i++) {
			FileInfo item = null;
			
			try {
				item = fromApiJson(entries.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			if (item != null)
				collection.add(item);
		}
		
		return collection;
	}
}
