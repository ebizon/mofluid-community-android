package com.mofluid.magento2.downlod.images;

import android.content.Context;

import java.io.File;

class FileCache {
    
   private final File cacheDir;
    
   public FileCache(Context context){
        
       //Find the dir at SDCARD to save cached images
        
       if (android.os.Environment.getExternalStorageState().equals(
                                    android.os.Environment.MEDIA_MOUNTED))
       {
           //if SDCARD is mounted (SDCARD is present on device and mounted)
           cacheDir = new File(
                      android.os.Environment.getExternalStorageDirectory(),"LazyList");
       }
       else
       {
           // if checking on simulator the create cache dir in your application context
           cacheDir=context.getCacheDir();
       }
        
       if(!cacheDir.exists()){
           // create cache dir in your application context
           cacheDir.mkdirs();
       }
   }
    
   public File getFile(String url){
       //Identify images by hashcode or encode by URLEncoder.encode.
       String filename=String.valueOf(url.hashCode());

       return new File(cacheDir, filename);
        
   }
    
   public void clear(){
       // list all files inside cache directory
       File[] files=cacheDir.listFiles();
       if(files==null)
           return;
       //delete all cache directory files
       for(File f:files)
           f.delete();
   }

}