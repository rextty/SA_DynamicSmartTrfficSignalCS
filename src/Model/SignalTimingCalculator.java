package Model;

public class SignalTimingCalculator {

    // private int signalTime;
    // public int getSignalTime() {return 0;}
    
    // 照循序圖，調整過後的秒數（綠燈）我們是分成NS和WE兩個
    private int NSsignalTime;
    private int WEsignalTime;

    public int getNSsignalTime() {return NSsignalTime;}
    public int getWEsignalTime() {return WEsignalTime;}
    
    public void calSignalTime(int NS, int WE) {
    	int time;
	if(NS > WE){
	    time = (NS - 300)/5;
	    // 基於預設時間去做加減，暫直接寫入我們所定之預設值(30)
	    NSsignalTime = 30 + time;
	    WEsignalTime = 30 - time;
	} else{
	    time = (WE - 300)/5;
	    WEsignalTime = 30 + time;
	    NSsignalTime = 30 - time;
	}
    }

}
