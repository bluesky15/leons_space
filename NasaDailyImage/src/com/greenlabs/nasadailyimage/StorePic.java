package com.greenlabs.nasadailyimage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

public class StorePic {
	String imgName = "";
	String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
	public Boolean savePic(Bitmap image,String name) throws IOException{
	imgName = name;
	File imgfile = new File(filepath,imgName);
	FileOutputStream fout = new FileOutputStream(imgfile);
	image.compress(Bitmap.CompressFormat.JPEG, 100, fout);
	fout.close();
	return true;
}
}
