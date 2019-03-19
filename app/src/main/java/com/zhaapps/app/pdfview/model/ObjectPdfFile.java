package com.zhaapps.app.pdfview.model;

public class ObjectPdfFile {
	private String name;
	private String path;
	public ObjectPdfFile(String name, String path) {
		super();
		this.name = name;
		this.path = path;
	}
	public ObjectPdfFile() {
		super();
		// TODO Auto-generated constructor stub
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
	
}
