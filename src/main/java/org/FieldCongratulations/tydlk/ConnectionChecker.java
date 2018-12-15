package org.FieldCongratulations.tydlk;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.content.Context;

public class ConnectionChecker
{
	public Boolean isWifiConnected = false;
	public Boolean isGsmConnected = false;
	public Boolean isConnected = false;
	private Context context;

	//API版本23以下时调用此方法进行检测
	//因为API23后getNetworkInfo(int networkType)方法被弃用
	private void checkState_21() {
		//步骤1：通过Context.getSystemService(Context.CONNECTIVITY_SERVICE)获得ConnectivityManager对象
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//步骤2：获取ConnectivityManager对象对应的NetworkInfo对象
		//NetworkInfo对象包含网络连接的所有信息
		//步骤3：根据需要取出网络连接信息
		//获取WIFI连接的信息
		NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		isWifiConnected = networkInfo.isConnected();
		//获取移动数据连接的信息
		networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		isGsmConnected = networkInfo.isConnected();
		isConnected = isWifiConnected | isGsmConnected;
		//tv_WiFistate.setText("Wifi是否连接:" + isWifiConn);
		//tv_Network_state.setText("移动数据是否连接:" + isMobileConn);
	}

	//API版本23及以上时调用此方法进行网络的检测
	//步骤非常类似
	private void checkState_21orNew() {
		isWifiConnected = isGsmConnected = isConnected = false;
		//获得ConnectivityManager对象
		//Log.i("fcdbg","connmanager");
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//获取所有网络连接的信息
		//Log.i("fcdbg","network");
		Network[] networks = connMgr.getAllNetworks();
		//用于存放网络连接信息
		//StringBuilder sb = new StringBuilder();
		NetworkInfo networkInfo;
		//通过循环将网络信息逐个取出来
		for (int i=0; i < networks.length; i++) {
			//Log.i("fcdbg","ntinfo");
			//获取ConnectivityManager对象对应的NetworkInfo对象
			networkInfo = connMgr.getNetworkInfo(networks[i]);
			if (networkInfo.isConnected()) {
				isConnected = true;
				switch (networkInfo.getType()) {
					case 0:
						isGsmConnected = true;
						break;
					case 1:
						isWifiConnected = true;
				}
			}
		}
	}

	ConnectionChecker(Context c) {
		context = c;
		refresh();
	}

	public void refresh() {
		if (Build.VERSION.SDK_INT >= 21)
			checkState_21orNew();
		else
			checkState_21();
	}
}
