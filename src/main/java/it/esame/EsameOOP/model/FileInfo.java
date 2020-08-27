package it.esame.EsameOOP.model;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.esame.EsameOOP.utils.Dropbox;

public class FileInfo {
	private String	name;
	private String	path;
	private String	ext;
	private long	size;
	private boolean	deleted;
	private boolean downloadable;
	
	public FileInfo(String name, String path, long size, boolean deleted, boolean downloadable) {
		super();
		this.setName(name);
		this.setPath(path);
		this.setExt();
		this.setSize(size);
		this.setDeleted(deleted);
		this.setDownloadable(downloadable);
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
	
	public String getExt() {
		return ext;
	}

	public void setExt() {
		String filename = this.getName();
		
		String[] split = filename.split("\\.");
		
		String ext = (split.length <= 1) ? "unknown" : split[split.length - 1];
		
		this.ext = ext;
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



	public JSONObject getInfo() throws JSONException {
		JSONObject info = new JSONObject();
		
		/*
		 * presenza
		 * dimensioni
		 * 
		 * cancellati (relative dimensioni)
		 * 
		 **/
		
		
		
		info.put("name", this.getName());
		info.put("path", this.getPath());
		info.put("ext", this.getExt());
		info.put("size", this.getSize());
		info.put("deleted", this.isDeleted());
		info.put("downloadable", this.isDownloadable());
		
		return info;
	}
	
	private static FileInfo fromApiJson(JSONObject o) {
		
		FileInfo result = null;
		
		try {
			if (o.getString(".tag").equals("deleted")) {
				JSONObject metadataDeleted = new JSONObject(Dropbox.getDeletedMetadata(o.getString("path_display"))).getJSONArray("entries").getJSONObject(0);
				
				result = new FileInfo(
						metadataDeleted.getString("name"),
						metadataDeleted.getString("path_display"),
						metadataDeleted.getLong("size"),
						true,
						metadataDeleted.getBoolean("is_downloadable")				
					);
			} else {
				result = new FileInfo(
						o.getString("name"),
						o.getString("path_display"),
						o.getLong("size"),
						false,
						o.getBoolean("is_downloadable")				
					);
			
			}
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
