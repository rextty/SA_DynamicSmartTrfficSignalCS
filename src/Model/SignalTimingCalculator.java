package Model;

public class SignalTimingCalculator {

    // private int signalTime;
    // public int getSignalTime() {return 0;}
    
    // 照循序圖，調整過後的秒數（綠燈）我們是分成NS和WE兩個
    private int NSsignalTime;
    private int WEsignalTime;

    public int getNSsignalTime() {return NSsignalTime;}
	public int getWEsignalTime() {return WEsignalTime;}
    
    // 要怎麼做到基於預設值去進行秒數的增減調整
    public void calSignalTime(int NS, int WE) {}

}
