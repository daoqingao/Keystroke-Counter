import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.*;
import java.util.Date;
import java.util.*;
import java.util.logging.LogManager;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.*;

public class keyAndMouseCounter implements NativeKeyListener,Serializable,NativeMouseInputListener {
    private static final long serialVersionUID = 1L;
    private static String objectFileName = "keyAndMouseSaveFile.obj";
    private static Date initialTime;
    private static Long initialFreeDiskSpace;
    private static Map<String, Integer> keyCounter = new HashMap();
    private static Map<Integer, Integer> mouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private static Map<String, Long> computerDataCounter = new HashMap(); //upTime, write, delete
    private static Map<String, Integer> totalkeyCounter = new HashMap();
    private static Map<Integer, Integer> totalmouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private static Map<String, Long> totalcomputerDataCounter = new HashMap(); //upTime, write, delete
    private static Map<String, Integer> yesterdaykeyCounter = new HashMap();
    private static Map<Integer, Integer> yesterdaymouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private static Map<String, Long> yesterdaycomputerDataCounter = new HashMap(); //upTime, write, delete
    private static int frequencyOfUpdate = 5000; //milliseconds
    private static int initialDay;
    private static counterCollection counterCollectionSaveDataAsObject= new counterCollection();
    public static void main(String[] args) {
        keyAndMouseCounter currentKeyAndMouseCounterObject = new keyAndMouseCounter();
        currentKeyAndMouseCounterObject.run();
    }

    public void run() {
        LogManager.getLogManager().reset();
        Calendar c1 = Calendar.getInstance();
        initialTime = new Date();
        initialDay = c1.get(Calendar.DAY_OF_YEAR);
        initialFreeDiskSpace = new File("/").getFreeSpace();

        try {
            readingCountingObj();
            GlobalScreen.registerNativeHook();
        } catch (Exception e) {
            e.printStackTrace();


            System.out.println("Either save file corrupted or missing");
        }
        GlobalScreen.addNativeMouseListener(new keyAndMouseCounter());
        GlobalScreen.addNativeKeyListener(new keyAndMouseCounter());
    }
    public void nativeKeyReleased(NativeKeyEvent arg0) {
        String letter = NativeKeyEvent.getKeyText(arg0.getKeyCode());
        try {
            keyCounter.put(letter, keyCounter.get(letter) + 1);
            keyCounter.put("total", keyCounter.get("total") + 1);
            totalkeyCounter.put(letter, totalkeyCounter.get(letter) + 1);
            totalkeyCounter.put("total", totalkeyCounter.get("total") + 1);
        } catch (Exception e) {
            e.printStackTrace();
            if(!(keyCounter.containsKey(letter)))
            keyCounter.put(letter, 0);
            if(!(totalkeyCounter.containsKey(letter)))
            totalkeyCounter.put(letter, 0);
        }
//        System.out.println("today");
//        System.out.println(keyCounter);
//        System.out.println("yesterday");
//        System.out.println(yesterdaykeyCounter);
    }
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        Integer click = nativeMouseEvent.getButton();

        try {
            mouseCounter.put(click, mouseCounter.get(click) + 1);
            mouseCounter.put(0, mouseCounter.get(0) + 1);
            totalmouseCounter.put(click, totalmouseCounter.get(click) + 1);
            totalmouseCounter.put(0, totalmouseCounter.get(0) + 1);
        } catch (Exception e) {
            e.printStackTrace();
            if(!(mouseCounter.containsKey(click)))
                mouseCounter.put(click, 0);
            if(!(totalmouseCounter.containsKey(click)))
            totalmouseCounter.put(click, 0);
        }
        //left off here
        if (new Date().getTime() - initialTime.getTime() > frequencyOfUpdate) {
            System.out.println("time to write");
            updateComputerDataCounter();
            writingCountingObj();
            Calendar c2 = Calendar.getInstance();
            if(c2.get(Calendar.DAY_OF_YEAR)!=initialDay)
            {
                //#TODO: settingNewDay();
                //#TODO: this method is for having yesterdays
                yesterdayProtocol();
                keyCounter.clear();
                mouseCounter.clear();
                computerDataCounter.clear();

                keyCounter.put("total",0);
                mouseCounter.put(0,0);
                computerDataCounter.put("upTime", (long) 0);
                computerDataCounter.put("write", (long) 0);
                computerDataCounter.put("delete", (long) 0);

                initialDay=c2.get(Calendar.DAY_OF_YEAR);
            }
        }
    }
    public void yesterdayProtocol(){
        yesterdaykeyCounter.putAll(keyCounter);
        yesterdaycomputerDataCounter.putAll(computerDataCounter);
        yesterdaymouseCounter.putAll(mouseCounter);
    }

    public void saveExit() {
            writingCountingObj();
            System.exit(0);
    }
    public void writingCountingObj() {
        try {
            FileOutputStream file = new FileOutputStream(objectFileName);
            ObjectOutputStream fout = new ObjectOutputStream(file);
            counterCollection c1 = new counterCollection();
            c1.setComputerDataCounter(computerDataCounter);
            c1.setKeyCounter(keyCounter);
            c1.setMouseCounter(mouseCounter);
            c1.setTotalcomputerDataCounter(totalcomputerDataCounter);
            c1.setTotalkeyCounter(totalkeyCounter);
            c1.setTotalmouseCounter(totalmouseCounter);
            c1.setYesterdaycomputerDataCounter(yesterdaycomputerDataCounter);
            c1.setYesterdaykeyCounter(yesterdaykeyCounter);
            c1.setYesterdaymouseCounter(yesterdaymouseCounter);
            c1.setDayOfTheYear(initialDay);
            counterCollectionSaveDataAsObject=c1;
            fout.writeObject(c1);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialWritingObj()
    {
        //for the first time the program is runned
        try {
            FileOutputStream file = new FileOutputStream(objectFileName);
            ObjectOutputStream fout = new ObjectOutputStream(file);
            counterCollection temp = new counterCollection();
            temp.setDayOfTheYear(initialDay);
            fout.writeObject(temp);
            System.out.println("The save file should now contain place Holders Map data structures");
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingCountingObj() {
        try {
            FileInputStream f1 = new FileInputStream(objectFileName);
            ObjectInputStream fin = new ObjectInputStream(f1);

            counterCollection tempReadingFromCounterCollectionSaveData = (counterCollection) fin.readObject();
            computerDataCounter=(tempReadingFromCounterCollectionSaveData.getComputerDataCounter());
            keyCounter=(tempReadingFromCounterCollectionSaveData.getKeyCounter());
            mouseCounter=(tempReadingFromCounterCollectionSaveData.getMouseCounter());
            totalcomputerDataCounter=(tempReadingFromCounterCollectionSaveData.getTotalcomputerDataCounter());
            totalkeyCounter=(tempReadingFromCounterCollectionSaveData.getTotalkeyCounter());
            totalmouseCounter=(tempReadingFromCounterCollectionSaveData.getTotalmouseCounter());
            yesterdaycomputerDataCounter=(tempReadingFromCounterCollectionSaveData.getYesterdaycomputerDataCounter());
            yesterdaykeyCounter=(tempReadingFromCounterCollectionSaveData.getYesterdaykeyCounter());
            yesterdaymouseCounter=(tempReadingFromCounterCollectionSaveData.getYesterdaymouseCounter());
            counterCollectionSaveDataAsObject=tempReadingFromCounterCollectionSaveData;
            initialDay=tempReadingFromCounterCollectionSaveData.getDayOfTheYear();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
            initialWritingObj();
            readingCountingObj();
        }
    }

    public static counterCollection getCounterCollectionSaveDataAsObject()
    {
        return counterCollectionSaveDataAsObject;
    }

    public void updateComputerDataCounter()
    {
        long currentDiskSize=new File("/").getFreeSpace();
        if(initialFreeDiskSpace>currentDiskSize)
        {
            computerDataCounter.put("write",computerDataCounter.get("write")+(initialFreeDiskSpace-currentDiskSize));
            totalcomputerDataCounter.put("write",totalcomputerDataCounter.get("write")+(initialFreeDiskSpace-currentDiskSize));
        }
        else
        {
            computerDataCounter.put("delete",computerDataCounter.get("delete")+(currentDiskSize-initialFreeDiskSpace));
            totalcomputerDataCounter.put("delete",totalcomputerDataCounter.get("delete")+(currentDiskSize-initialFreeDiskSpace));
        }
        initialFreeDiskSpace =currentDiskSize;
        computerDataCounter.put("upTime",computerDataCounter.get("upTime")+(new Date().getTime()-initialTime.getTime()));
        totalcomputerDataCounter.put("upTime",totalcomputerDataCounter.get("upTime")+(new Date().getTime()-initialTime.getTime()));
        initialTime=new Date();
//        OperatingSystemMXBean operatingSystemMXBean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
//        System.out.println("cpu usage" +operatingSystemMXBean.getSystemCpuLoad());
    }
    public static Map<String, Integer> getKeyCounter() {
        return keyCounter;
    }
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {}
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {}
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {}
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {}
    public void nativeKeyTyped(NativeKeyEvent arg0) {}
    public void nativeKeyPressed(NativeKeyEvent arg0) {}

}