package Model;

// TODO: TrafficSignal
// 如果有懂java的人看到ㄉ話，歡迎幫忙修改，我主要放我的思路供參考
public class TrafficSignal {
    //private int countTime;
    private int currentState;
    private int direction;  //資料型態參照類別圖
    
    //想將上方的countTime拆分成如下之各燈號時長屬性，原因：
    //1.判斷後壅塞狀況後，做的調整是eg.NS更塞 會增加NS向綠燈時間 && 減少WE向綠燈時間，表示紅燈也該相應的被調整到
    //2.緊急車輛出現時，路口中各個燈號的時長處理是更複雜的，拆開會比較好操作
    private int greenDuration;
    private int yellowDuration;
    private int redDuration;
    
    /* 若要考慮到各路口燈號時間長度不一定，則需要額外設置屬性儲存預設值
    private int defaultGreen;
    private int defaultRed;
    private int defaultYellow;
    */
	
    
    // 傳入的參數direction->以建立不同方向的紅綠燈物件
    public TrafficLight(char direction, int greenDuration, int yellowDuration, int redDuration) {
        this.direction = direction;
        this.greenDuration = greenDuration;
        this.yellowDuration = yellowDuration;
        this.redDuration = redDuration;
	    
	/*若要考慮到各路口燈號時間長度不一定，將傳入的參數設為預設值
	this.defaultGreen = greenDuration;
        this.defaultYellow = yellowDuration;
        this.defaultRed = redDuration;
	*/
	    
        this.currentState = "red"; // 初始狀態為紅燈
    }
    
    // 在我們的應用中紅綠燈一開始便是啟動狀態
	// start()和 TrafficLight物件的建立好像沒出現在我們的流程之中，這部分該怎麼處理
    public void start() {
        // 啟動紅綠燈，初始狀態為綠燈
        currentState = "green";
        performSignal();

        // 開始進行狀態切換
        scheduleNextTransition();
    }
	
    // 根據當前燈號狀態切換到下一個狀態
    public void transitionToNextState() {
        switch (currentState) {
            case "green":
                currentState = "yellow";
                break;
            case "yellow":
                currentState = "red";
                break;
            case "red":
                currentState = "green";
                break;
        }

        performSignal();
        scheduleNextTransition();
    }

    // 根據當前燈號狀態選擇相應的持續時間，然後通過 Thread.sleep(duration * 1000) 將程式暫停指定的時間，從而控制狀態切換的時間間隔
    public void scheduleNextTransition() {
        int duration;
        switch (currentState) {
            case "green":
                duration = greenDuration;
                break;
            case "yellow":
                duration = yellowDuration;
                break;
            case "red":
                duration = redDuration;
                break;
            default:
                duration = redDuration;
        }

        try {
            Thread.sleep(duration * 1000); // 單位轉換為毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        transitionToNextState();
    }

    public int getCurrentState() {return currentState;}
    
    // 車流跟緊急車輛的兩種功能好像都不會動到黃燈？
    public void setDurations(int green, int red) {
        this.greenDuration = green;
        //this.yellowDuration = yellow; 
        this.redDuration = red;
    }
    
    //採取預設值（不確定大家共識的各燈號時間長度，暫參考張庭碩的word檔）
    //不考慮各路口的燈號時間不一，直接將我們定的數字寫進去
    public void adoptDefault() {
        greenDuration = 30;
        yellowDuration = 5;
        redDuration = 30;
	    
	/*若要考慮到各路口燈號時間長度不一定，則是透過預設值屬性去賦值
        greenDuration = this.defaultGreen;
	yellowDuration = this.defaultYellow;
	redDuration = this.defaultRed;
	*/
    }
    
    // 循序圖中最後顯示給vehicle的帕，不確定我們java程式碼跟實際所展示的模擬畫面是否有關
    // public void performSignal(){}
    
    //原本的設置燈號函數
    //public void changeSignal(int roadID, int time, int signalState) {}
}
