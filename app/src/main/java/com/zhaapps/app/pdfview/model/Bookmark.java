package com.zhaapps.app.pdfview.model;

public class Bookmark {
	private String name;
	private String path;
	private int page;
	public Bookmark(String name, String path, int page) {
		super();
		this.name = name;
		this.path = path;
		this.page = page;
	}
	
	public Bookmark() {
		super();
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
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
}
