package com.ebizon.fluid.model;

import android.graphics.Bitmap;

/**
 * 
 * @author Digvijay.s
 *
 */

class SlideMenuItem {
	private Bitmap image;
	private String title;
	
	public SlideMenuItem(Bitmap image, String title) {
		super();
		this.image = image;
		this.title = title;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
