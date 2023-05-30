package Model;

// TODO: TrafficSignal
public class TrafficSignal {
    //private int countTime;
    private int signalState;
    
    //想將上方的countTime拆分成如下之各燈號時長屬性，原因：
    //1.判斷後壅塞狀況後，做的調整是eg.NS更塞 會增加NS向綠燈時間 && 減少WE向綠燈時間，表示紅燈也該相應的被調整到
    //2.緊急車輛出現時，路口中各個燈號的時長處理是更複雜的，拆開會比較好操作
    private int greenLightDuration;
    private int yellowLightDuration;
    private int redLightDuration;
    
    public TrafficSignal() {}

    public int getSignalState() {return 0;}
    
    //採取預設值（不確定大家共識的各燈號時間長度，暫參考張庭碩的word檔）
    public void adoptDefault() {
        greenLightDuration = 30;
        yellowLightDuration = 5;
        redLightDuration = 30;
    }
    
    //還在思考燈號拆開後的變換和循序圖中想要做到的NS/WE時間調整
    public void changeSignal(int roadID, int time, int signalState) {}
}
