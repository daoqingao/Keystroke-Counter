import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class counterCollection implements Serializable {
    private static final long serialVersionUID = 2L;
    public counterCollection()
    {
        
        keyCounter.put("total",0);
        mouseCounter.put(0,0);
        computerDataCounter.put("upTime", (long) 0);
        computerDataCounter.put("write", (long) 0);
        computerDataCounter.put("delete", (long) 0);
        
        TotalkeyCounter.put("total",0);
        TotalmouseCounter.put(0,0);
        TotalcomputerDataCounter.put("upTime", (long) 0);
        TotalcomputerDataCounter.put("write", (long) 0);
        TotalcomputerDataCounter.put("delete", (long) 0);

        yesterdaykeyCounter.put("total",0);
        yesterdaymouseCounter.put(0,0);
        yesterdaycomputerDataCounter.put("upTime", (long) 0);
        yesterdaycomputerDataCounter.put("write", (long) 0);
        yesterdaycomputerDataCounter.put("delete", (long) 0);
    }

    public int getDayOfTheYear() {
        return dayOfTheYear;
    }

    public void setDayOfTheYear(int dayOfTheYear) {
        this.dayOfTheYear = dayOfTheYear;
    }

    private int dayOfTheYear;

    //Everything here are getters and setters, don't worry about it
    //Its an object containing all the Map counters



    public Map<String, Integer> getKeyCounter() {
        return keyCounter;
    }

    public void setKeyCounter(Map<String, Integer> keyCounter) {
        this.keyCounter = keyCounter;
    }

    public Map<Integer, Integer> getMouseCounter() {
        return mouseCounter;
    }

    public void setMouseCounter(Map<Integer, Integer> mouseCounter) {
        this.mouseCounter = mouseCounter;
    }

    public Map<String, Long> getComputerDataCounter() {
        return computerDataCounter;
    }

    public void setComputerDataCounter(Map<String, Long> computerDataCounter) {
        this.computerDataCounter = computerDataCounter;
    }

    public Map<String, Integer> getTotalkeyCounter() {
        return TotalkeyCounter;
    }

    public void setTotalkeyCounter(Map<String, Integer> totalkeyCounter) {
        TotalkeyCounter = totalkeyCounter;
    }

    public Map<Integer, Integer> getTotalmouseCounter() {
        return TotalmouseCounter;
    }

    public void setTotalmouseCounter(Map<Integer, Integer> totalmouseCounter) {
        TotalmouseCounter = totalmouseCounter;
    }

    public Map<String, Long> getTotalcomputerDataCounter() {
        return TotalcomputerDataCounter;
    }

    public void setTotalcomputerDataCounter(Map<String, Long> totalcomputerDataCounter) {
        TotalcomputerDataCounter = totalcomputerDataCounter;
    }
    public Map<String, Integer> getYesterdaykeyCounter() {
        return yesterdaykeyCounter;
    }

    public void setYesterdaykeyCounter(Map<String, Integer> yesterdaykeyCounter) {
        this.yesterdaykeyCounter = yesterdaykeyCounter;
    }

    public Map<Integer, Integer> getYesterdaymouseCounter() {
        return yesterdaymouseCounter;
    }

    public void setYesterdaymouseCounter(Map<Integer, Integer> yesterdaymouseCounter) {
        this.yesterdaymouseCounter = yesterdaymouseCounter;
    }

    public Map<String, Long> getYesterdaycomputerDataCounter() {
        return yesterdaycomputerDataCounter;
    }

    public void setYesterdaycomputerDataCounter(Map<String, Long> yesterdaycomputerDataCounter) {
        this.yesterdaycomputerDataCounter = yesterdaycomputerDataCounter;
    }
    private Map<String, Integer> keyCounter = new HashMap();
    private Map<Integer, Integer> mouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private Map<String, Long> computerDataCounter = new HashMap(); //upTime, write, delete
    private Map<String, Integer> TotalkeyCounter = new HashMap();
    private Map<Integer, Integer> TotalmouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private Map<String, Long> TotalcomputerDataCounter = new HashMap(); //upTime, write, delete
    private Map<String, Integer>    yesterdaykeyCounter = new HashMap();
    private Map<Integer, Integer>   yesterdaymouseCounter = new HashMap(); //1 for left, 2 for right, 0 for
    private Map<String, Long>       yesterdaycomputerDataCounter = new HashMap(); //upTime, write, delete
}

