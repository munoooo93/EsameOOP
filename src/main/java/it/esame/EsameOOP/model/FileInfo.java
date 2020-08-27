package it.esame.EsameOOP.model;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.esame.EsameOOP.utils.Dropbox;

/**
 * @author Emanuele Ballarini
 */

/**
 * Classe rappresentante i metadati di un file remoto su {@link Dropbox}
 */
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

	/**
	 * Restituisce il nome del file
	 * @return	Il nome del file
	 */
	public String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Restituisce il percorso del file
	 * @return	Il percorso del file
	 */
	public String getPath() {
		return path;
	}

	protected void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Restituisce l'estensione del file
	 * @return	L'estensione del file
	 */
	public String getExt() {
		return ext;
	}

	/**
	 * Setta il metadato inerente all'estensione del file
	 * @implNote	se il file non termina come '.<estensione>' viene riportato 'unknown'
	 */
	protected void setExt() {
		String filename = this.getName();
		
		String[] split = filename.split("\\.");
		
		String ext = (split.length <= 1) ? "unknown" : split[split.length - 1];
		
		this.ext = ext;
	}

	/**
	 * Restituisce la dimensione del file
	 * @return	La dimensione del file in byte
	 */
	public long getSize() {
		return size;
	}
	
	protected void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return	Il nome del file
	 */
	public boolean isDeleted() {
		return deleted;
	}

	protected void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return	Il nome del file
	 */
	protected boolean isDownloadable() {
		return downloadable;
	}

	protected void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	/**
	 * @return	Istanza {@link JSONObject} rappresentante metadati del file
	 * @throws	JSONException
	 */
	public JSONObject getInfo() throws JSONException {
		JSONObject info = new JSONObject();
		
		info.put("name", this.getName());
		info.put("path", this.getPath());
		info.put("ext", this.getExt());
		info.put("size", this.getSize());
		info.put("deleted", this.isDeleted());
		info.put("downloadable", this.isDownloadable());
		
		return info;
	}
	
	/**
	 * Effettua il parsing del json restituito dall'API di Dropbox generando un oggetto {@link FileInfo}
	 */
	private static FileInfo fromApiJson(JSONObject o) {
		
		FileInfo result = null;
		
		try {
			/*
			 * Se l'oggetto è eliminato, si effettua un'ulteriore chiamata a Dropbox per ottenere i metadati
			 */
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
	
	/**
	 * Effettua il parsing del json contenente i metadati, creando una lista di {@link FileInfo}
	 * 
	 * @param	entries Array JSON contenente i metadati di tutti i file
	 * @return	Lista di {@link FileInfo}
	 */
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
