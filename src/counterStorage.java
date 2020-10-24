import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class counterStorage implements Serializable,Cloneable {
    private static final long serialVersionUID = 1L;
    private Map<String,Integer> keyCounter = new HashMap();
    private Map<Integer,Integer> mouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private Map<String,Long> computerDataCounter= new HashMap(); //upTime, write, delete
    private int savedDay;
    private counterStorage todayStatistics;
    private counterStorage yesterdayStatistics;
    private counterStorage TotalStatistics;
    public counterStorage()
    {
        keyCounter.put("total",0);
        mouseCounter.put(0,0);
        computerDataCounter.put("upTime", (long) 0);
        computerDataCounter.put("write", (long) 0);
        computerDataCounter.put("delete", (long) 0);

    }
    public counterStorage(Map<String,Integer> a,Map<Integer,Integer> b,Map<String,Long> c)
    {
        keyCounter=a;
        mouseCounter=b;
        computerDataCounter=c;
    }
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

    public counterStorage getTodayStatistics() {
        return todayStatistics;
    }

    public void setTodayStatistics(counterStorage todayStatistics) {
        this.todayStatistics = todayStatistics;
    }

    public counterStorage getYesterdayStatistics() {
        return yesterdayStatistics;
    }

    public void setYesterdayStatistics(counterStorage yesterdayStatistics) {
        this.yesterdayStatistics = yesterdayStatistics;
    }
    public counterStorage getTotalStatistics() {
        return TotalStatistics;
    }

    public void setTotalStatistics(counterStorage totalStatistics) {
        TotalStatistics = totalStatistics;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getSavedDay() {
        return savedDay;
    }

    public void setSavedDay(int savedDay) {
        this.savedDay = savedDay;
    }
}
