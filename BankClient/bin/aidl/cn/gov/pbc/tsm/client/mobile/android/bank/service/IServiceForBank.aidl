package cn.gov.pbc.tsm.client.mobile.android.bank.service;


interface IServiceForBank
{
	byte[] openSEChannel(in byte[] aid);
	byte[] sendAPDU(in byte[] command);
	String sendAPDUStr(String command);
	boolean closeSEChannel(); 
	boolean isSEConnected();
	Map callServer(String dealType,String params);
}