package it.esame.EsameOOP.model;

import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

public class FileStats {
	private String	ext;
	private int		count;
	private long	minSize;
	private long	maxSize;
	
	public FileStats(String ext) {
		this.setExt(ext);
		this.setCount(1);
		this.minSize = Long.MAX_VALUE;
		this.maxSize = Long.MIN_VALUE;
	}

	public String getExt() {
		return ext;
	}

	protected void setExt(String ext) {
		this.ext = ext;
	}

	public int getCount() {
		return count;
	}

	protected void setCount(int count) {
		this.count = count;
	}

	public long getMinSize() {
		return minSize;
	}

	protected void setMinSize(long minSize) {
		this.minSize = minSize;
	}

	public long getMaxSize() {
		return maxSize;
	}

	protected void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}
	
	public void incrementCount() {
		this.count += 1;
	}
	
	public void addDimension(long dim) {
		if (dim > this.maxSize)
			this.maxSize = dim;
		if (dim < this.minSize)
			this.minSize = dim;
	}
	
	public JSONObject toJSONObject() throws JSONException {
		JSONObject obj = new JSONObject();
		
		obj.put("ext", this.getExt());
		obj.put("count", this.getCount());
		obj.put("min-size", this.getMinSize());
		obj.put("max-size", this.getMaxSize());
		
		return obj;
	}
	
	public static LinkedList<FileStats> getStatsFromFiles(LinkedList<FileInfo> list, boolean includeDeleted) {
		LinkedList<FileStats> collection = new LinkedList<>();
		
		for (FileInfo f: list) {
			if ((includeDeleted == false) && (f.isDeleted())) {
				continue;
			} else {
				boolean contained = false;
				FileStats subject = null;
				
				for (FileStats s: collection) {
					if (s.getExt().equals(f.getExt())) {
						contained = true;
						subject = s;
					}
				}
				
				if (contained) {
					subject.incrementCount();
					subject.addDimension(f.getSize());
				} else {
					subject = new FileStats(f.getExt());
					subject.addDimension(f.getSize());
					collection.add(subject);
				}
			}
		}
		
		return collection;
	}
}