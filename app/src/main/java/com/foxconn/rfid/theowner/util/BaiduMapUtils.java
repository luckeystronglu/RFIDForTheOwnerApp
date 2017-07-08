package com.foxconn.rfid.theowner.util;

public class BaiduMapUtils {

	 private static final double EARTH_RADIUS = 6378137;  
	 private static double rad(double d) {  
	        return d * Math.PI / 180.0;  
	    } 
	 public static double DistanceOfTwoPoints(double lat1,double lng1,   
             double lat2,double lng2) {  
        double radLat1 = rad(lat1);  
        double radLat2 = rad(lat2);  
        double a = radLat1 - radLat2;  
        double b = rad(lng1) - rad(lng2);  
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
                + Math.cos(radLat1) * Math.cos(radLat2)  
                * Math.pow(Math.sin(b / 2), 2)));  
        s = s * EARTH_RADIUS;  
        s = Math.round(s * 10000) / 10000;  
        return s;  
    }  
	 public static float getZoomLevel(double maxlat,double minlat,double maxlng,double minlng)
	 {
		 int zoomLevel[]={2000000,1000000,500000,200000,100000,50000,25000,20000,10000,5000,2000,1000,500,100,100,50,20,0};

		 int jl=(int)DistanceOfTwoPoints(minlat,minlng,maxlat,maxlng);
		 int i=0;  
		 for(i=0;i<17;i++)
		 {
			 if(zoomLevel[i]<jl)
				 break;
		 }
		 float zoom=i+3;
		 return zoom;
	 }
}
