package com.foxconn.rfid.theowner.util.net;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

@SuppressLint("UseValueOf")
public class WifiAdmin
{
  @SuppressWarnings("unused")
private static final String TAG = "[WifiAdmin]";
  private WifiManager mWifiManager;
  private WifiInfo mWifiInfo;
  private List<ScanResult> mWifiList = null;
  private List<WifiConfiguration> mWifiConfiguration;
  private WifiManager.WifiLock mWifiLock;
  @SuppressWarnings("unused")
private DhcpInfo dhcpInfo;

  public WifiAdmin(Context context)
  {
    this.mWifiManager = ((WifiManager)context.getSystemService("wifi"));
    this.mWifiInfo = this.mWifiManager.getConnectionInfo();
  }

  public boolean openWifi() {
    if (!(this.mWifiManager.isWifiEnabled())) {
      Log.i("[WifiAdmin]", "setWifiEnabled.....");
      this.mWifiManager.setWifiEnabled(true);
      try {
        Thread.sleep(1000L);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      Log.i("[WifiAdmin]", "setWifiEnabled.....end");
    }
    return this.mWifiManager.isWifiEnabled();
  }

  public void closeWifi() {
    if (this.mWifiManager.isWifiEnabled())
      this.mWifiManager.setWifiEnabled(false);
  }

  public int checkState()
  {
    return this.mWifiManager.getWifiState();
  }

  public void acquireWifiLock() {
    this.mWifiLock.acquire();
  }

  public void releaseWifiLock() {
    if (this.mWifiLock.isHeld())
      this.mWifiLock.acquire();
  }

  public void creatWifiLock()
  {
    this.mWifiLock = this.mWifiManager.createWifiLock("Test");
  }

  public List<WifiConfiguration> getConfiguration() {
    return this.mWifiConfiguration;
  }

  public void connectConfiguration(int index) {
    if (index > this.mWifiConfiguration.size()) {
      return;
    }
    this.mWifiManager.enableNetwork(((WifiConfiguration)this.mWifiConfiguration.get(index)).networkId, true);
  }

  public void startScan() {
    boolean scan = this.mWifiManager.startScan();
    Log.i("[WifiAdmin]", "startScan result:" + scan);
    this.mWifiList = this.mWifiManager.getScanResults();
    this.mWifiConfiguration = this.mWifiManager.getConfiguredNetworks();

    if (this.mWifiList != null) {
      Log.i("[WifiAdmin]", "startScan result:" + this.mWifiList.size());
      for (int i = 0; i < this.mWifiList.size(); ++i) {
        ScanResult result = (ScanResult)this.mWifiList.get(i);
        Log.i("[WifiAdmin]", "startScan result[" + i + "]" + result.SSID + "," + result.BSSID);
      }
      Log.i("[WifiAdmin]", "startScan result end.");
    } else {
      Log.i("[WifiAdmin]", "startScan result is null.");
    }
  }

  public List<ScanResult> getWifiList()
  {
    return this.mWifiList;
  }

  public StringBuilder lookUpScan() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < this.mWifiList.size(); ++i) {
      stringBuilder.append("Index_" + new Integer(i + 1).toString() + ":");
      stringBuilder.append(((ScanResult)this.mWifiList.get(i)).toString());
      stringBuilder.append("/n");
    }
    return stringBuilder;
  }

  public String getMacAddress() {
    return ((this.mWifiInfo == null) ? "NULL" : this.mWifiInfo.getMacAddress());
  }

  public String getBSSID() {
    return ((this.mWifiInfo == null) ? "NULL" : this.mWifiInfo.getBSSID());
  }

  public DhcpInfo getDhcpInfo() {
    return (this.dhcpInfo = this.mWifiManager.getDhcpInfo());
  }

  public int getIPAddress() {
    return ((this.mWifiInfo == null) ? 0 : this.mWifiInfo.getIpAddress());
  }

  public int getNetworkId() {
    return ((this.mWifiInfo == null) ? 0 : this.mWifiInfo.getNetworkId());
  }

  public WifiInfo getWifiInfo() {
    this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    return this.mWifiInfo;
  }

  public boolean addNetwork(WifiConfiguration wcg) {
    int wcgID = this.mWifiManager.addNetwork(wcg);
    boolean b = this.mWifiManager.enableNetwork(wcgID, true);
    System.out.println("addNetwork==" + wcgID);
    System.out.println("enableNetwork==" + b);
    return b;
  }

  public void disconnectWifi(int netId) {
    this.mWifiManager.disableNetwork(netId);
    this.mWifiManager.disconnect();
  }

  public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
    Log.i("[WifiAdmin]", "SSID:" + SSID + ",password:" + Password);
    WifiConfiguration config = new WifiConfiguration();
    config.allowedAuthAlgorithms.clear();
    config.allowedGroupCiphers.clear();
    config.allowedKeyManagement.clear();
    config.allowedPairwiseCiphers.clear();
    config.allowedProtocols.clear();
    config.SSID = "\"" + SSID + "\"";

    WifiConfiguration tempConfig = IsExsits(SSID);

    if (tempConfig != null)
      this.mWifiManager.removeNetwork(tempConfig.networkId);
    else {
      Log.i("[WifiAdmin]", "IsExsits is null.");
    }

    if (Type == 1)
    {
      Log.i("[WifiAdmin]", "Type =1.");

      config.allowedKeyManagement.set(0);
    }
    else if (Type == 2)
    {
      Log.i("[WifiAdmin]", "Type =2.");
      config.hiddenSSID = true;
      config.wepKeys[0] = "\"" + Password + "\"";
      config.allowedAuthAlgorithms.set(1);
      config.allowedGroupCiphers.set(3);
      config.allowedGroupCiphers.set(2);
      config.allowedGroupCiphers.set(0);
      config.allowedGroupCiphers.set(1);
      config.allowedKeyManagement.set(0);
      config.wepTxKeyIndex = 0;
    } else if (Type == 3)
    {
      Log.i("[WifiAdmin]", "Type =3.");
      config.preSharedKey = "\"" + Password + "\"";

      config.hiddenSSID = true;
      config.allowedAuthAlgorithms.set(0);
      config.allowedGroupCiphers.set(2);
      config.allowedKeyManagement.set(1);
      config.allowedPairwiseCiphers.set(1);

      config.allowedGroupCiphers.set(3);
      config.allowedPairwiseCiphers.set(2);
      config.status = 2;
    }
    return config;
  }

  private WifiConfiguration IsExsits(String SSID) {
    List<WifiConfiguration> existingConfigs = this.mWifiManager.getConfiguredNetworks();
    for (WifiConfiguration existingConfig : existingConfigs) {
      if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
        return existingConfig;
      }
    }
    return null;
  }

  @SuppressWarnings("deprecation")
public String getWifiGateWay() {
    DhcpInfo dhcpInfo = getDhcpInfo();
    Log.v(" wifi ipAddress", Formatter.formatIpAddress(dhcpInfo.ipAddress));
    Log.v("wifi gateway", Formatter.formatIpAddress(dhcpInfo.gateway));
    Log.v("wifi dns1", Formatter.formatIpAddress(dhcpInfo.dns1));
    Log.v("wifi dns2", Formatter.formatIpAddress(dhcpInfo.dns2));
    String wifi_gateway = Formatter.formatIpAddress(dhcpInfo.gateway);
    return wifi_gateway;
  }

  public static boolean isWifiConnect(Context context)
  {
    ConnectivityManager connManager = (ConnectivityManager)context.getSystemService("connectivity");
    NetworkInfo ni = connManager.getActiveNetworkInfo();
    return ((ni != null) && (ni.isConnectedOrConnecting()));
  }

  public static boolean isNetworkConnected(Context context)
  {
    if (context != null) {
      ConnectivityManager mConnectivityManager = (ConnectivityManager)context
        .getSystemService("connectivity");
      NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
      if (mNetworkInfo != null) {
        return mNetworkInfo.isAvailable();
      }
    }
    return false;
  }
}