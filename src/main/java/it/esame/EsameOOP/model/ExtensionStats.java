package it.esame.EsameOOP.model;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Emanuele Ballarini
 */

/**
 * Classe per definire le statistiche per una data estensione
 */
public class ExtensionStats {
	private String	ext;
	private int		count;
	private long	minSize;
	private long	maxSize;
	private long	totalDimensions;
	
	public ExtensionStats(String ext) {
		this.setExt(ext);
		this.setCount(1);
		this.setMinSize(Long.MAX_VALUE);
		this.setMaxSize(Long.MIN_VALUE);
		this.totalDimensions = 0; 
	}

	/**
	 * Restituisce l'estensione sui cui vengono effettuate le statistiche
	 * @return estensione file
	 */
	public String getExt() {
		return ext;
	}

	protected void setExt(String ext) {
		this.ext = ext;
	}
	
	/**
	 * Restituisce il numero di file con estensione {@link #ext}
	 * @return Numero di file con estensione {@link #ext}
	 */
	public int getCount() {
		return count;
	}

	protected void setCount(int count) {
		this.count = count;
	}

	/**
	 * Restituisce la dimensione del file più piccolo
	 * @return la dimensione del file più piccolo
	 */
	public long getMinSize() {
		return minSize;
	}

	protected void setMinSize(long minSize) {
		this.minSize = minSize;
	}

	/**
	 * Restituisce la dimensione del file più grande
	 * @return la dimensione del file più grande
	 */
	public long getMaxSize() {
		return maxSize;
	}

	protected void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	
	protected long getTotalDimensions() {
		return totalDimensions;
	}

	/**
	 * Incrementa di 1 il numero di file con estensione {@link #ext}
	 */
	public void incrementCount() {
		this.count += 1;
	}
	
	/**
	 * Aggiunge la dimensione del file per poi calcolarne le statistiche
	 * @param dim Dimensione del file
	 */
	public void addDimension(long dim) {
		this.totalDimensions += dim;
		
		if (dim > this.maxSize)
			this.setMaxSize(dim);
		if (dim < this.minSize)
			this.setMinSize(dim);
	}
	
	/**
	 * Ottiene un oggetto JSON contenente le informazioni sulle statistiche
	 * @return Istanza JSONObject contenente le statistiche per l'estensione {@link #ext}
	 * @throws JSONException
	 */
	public JSONObject toJSONObject() throws JSONException {
		JSONObject obj = new JSONObject();
		
		obj.put("ext", this.getExt());
		obj.put("count", this.getCount());
		obj.put("min-size", this.getMinSize());
		obj.put("max-size", this.getMaxSize());
		obj.put("avg-size", this.getTotalDimensions() / this.getCount());
		
		return obj;
	}
	
	/**
	 * Ottiene una lista di statistiche dalla lista dei file
	 * @param list Lista di FileInfo
	 * @param includeDeleted true per includere i file cancellati nelle statistiche, false altrimenti
	 * @return Lista delle statistiche basate sull'estensione
	 */
	public static LinkedList<ExtensionStats> getStatsFromFiles(LinkedList<FileInfo> list, boolean includeDeleted) {
		LinkedList<ExtensionStats> collection = new LinkedList<>();
		
		for (FileInfo f: list) {
			/* 
			 * Se non bisogna includere gli elementi e l'elemento corrente è eliminato,
			 * viene saltato l'elemento.
			 */
			if ((includeDeleted == false) && (f.isDeleted())) {
				continue;
			} else {
				boolean contained = false;
				ExtensionStats subject = null;
				
				for (ExtensionStats s: collection) {
					if (s.getExt().equals(f.getExt())) {
						contained = true;
						subject = s;
					}
				}
				
				
				if (contained) {
					subject.incrementCount();
					subject.addDimension(f.getSize());
				} else {
					subject = new ExtensionStats(f.getExt());
					subject.addDimension(f.getSize());
					collection.add(subject);
				}
			}
		}
		
		return collection;
	}
	
	/**
	 * Ottiene le statistiche legate ad una cartella
	 * @param list Lista di FileInfo
	 * @param includeDeleted Flag per includere i file cancellati
	 * @param folder Cartella di cui si vogliono conoscere le statistiche
	 * @return Lista delle statistiche
	 */
	public static LinkedList<ExtensionStats> getStatsFromFolder(LinkedList<FileInfo> list, boolean includeDeleted, String folder) {
		LinkedList<ExtensionStats> collection = new LinkedList<>();
		
		for (FileInfo f: list) {
			String path = (folder.isEmpty()) ? "/" : folder;
			if (!f.getPath().equals(path + "/" + f.getName()))
				continue;
			if ((includeDeleted == false) && (f.isDeleted())) {
				continue;
			} else {
				boolean contained = false;
				ExtensionStats subject = null;
				
				for (ExtensionStats s: collection) {
					if (s.getExt().equals(f.getExt())) {
						contained = true;
						subject = s;
					}
				}
				
				if (contained) {
					subject.incrementCount();
					subject.addDimension(f.getSize());
				} else {
					subject = new ExtensionStats(f.getExt());
					subject.addDimension(f.getSize());
					collection.add(subject);
				}
			}
		}
		
		return collection;
	}
}