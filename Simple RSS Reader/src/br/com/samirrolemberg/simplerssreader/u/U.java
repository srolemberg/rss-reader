package br.com.samirrolemberg.simplerssreader.u;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class U {

	private U() {}
	
	public static boolean isConnected(Context  context) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
            context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
                    Log.d("LAN-STATE","Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
                    return true;
            } else if(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
                    Log.d("LAN-STATE","Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                    return true;
            } else {
                    Log.e("LAN-STATE","Status de conexão Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
                    Log.e("LAN-STATE","Status de conexão 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
                    return false;
            }
        } catch (Exception  e) {
                Log.e("LAN-STATE",e.getMessage());
                return false;
        }
    }

}
