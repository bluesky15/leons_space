package com.greenlabs.nasadailyimage;

import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Handler handler;
	Bitmap image;
	String imageName=null;
	IotdHandler iotdhandler=new IotdHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new Handler();
		refreshFromFeed();
	}

	private void refreshFromFeed() {
		final ProgressDialog dialog= ProgressDialog.show(this,"Loading","Loading the image of the Day");
		    new Thread(new Runnable() {
		        public void run() {
		        	if(iotdhandler == null){
		        	iotdhandler = new IotdHandler();
		        }
		            
				    iotdhandler.processFeed();
				    image= iotdhandler.getImage();
				    imageName =iotdhandler.getImageName();
		            handler.post(new Runnable() {
		                public void run() {
		                    resetDisplay(iotdhandler.getTitle(), iotdhandler.getDate(), iotdhandler.getImage(), iotdhandler.getDescription());
		                    dialog.dismiss();
		                }
		            });
		        }
		    }).start();
		    
		}
	public void onSetWall(View view){
		Thread th = new Thread(){
		
		public void run(){
			WallpaperManager wallpapermanager = WallpaperManager.getInstance(MainActivity.this);
			try{ 
				wallpapermanager.setBitmap(image);
				
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(MainActivity.this,"wallpaper set",Toast.LENGTH_SHORT).show();
					}
				});
			}
			catch(Exception e) {
				e.printStackTrace();
					handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(MainActivity.this,"Error setting wallpaper",Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	};
	th.start();
}
	public void onSaveImage(View view) throws IOException{
		new Thread(new Runnable(){

			@Override
			public void run() {
				StorePic sp = new StorePic();
				try {
					if(sp.savePic(image, imageName)==true);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(MainActivity.this,"image saved",Toast.LENGTH_SHORT).show();
							
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}).start();
		
	}
	public void onRefresh(View view)
	{
		refreshFromFeed();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void resetDisplay(String title, String date,Bitmap image, StringBuffer description) {
		TextView titleView =  (TextView)findViewById(R.id.imageTitle);
		titleView.setText(title);
		TextView dateView =(TextView)findViewById(R.id.imageDate);
		dateView.setText(date);
		ImageView imageView = (ImageView)findViewById(R.id.imageDisplay);
		imageView.setImageBitmap(image);
		imageView.setContentDescription(title);
		TextView descriptionView = (TextView)findViewById(R.id.imageDescription);
		descriptionView.setText(description);
	}

}
