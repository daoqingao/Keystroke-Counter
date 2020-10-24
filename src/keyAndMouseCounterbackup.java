import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.io.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.Date;
import java.util.*;
import java.util.logging.LogManager;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.*;

public class keyAndMouseCounterbackup implements NativeKeyListener,Serializable,NativeMouseInputListener {
    private static final long serialVersionUID = 1L;
    private static String objectFileName = "keyAndMouseSaveFile.obj";
    private static Date initialTime;
    private static Long initialFreeDiskSpace;
    private static Map<String, Integer> keyCounter = new HashMap();
    private static Map<Integer, Integer> mouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private static Map<String, Long> computerDataCounter = new HashMap(); //upTime, write, delete
    private static int frequencyOfUpdate = 5000; //milliseconds
    private static int initialDay;
    private static counterStorage writingStorage= new counterStorage();
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
            initialWritingObj();
            readingCountingObj();
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
        } catch (Exception e) {
            e.printStackTrace();
            keyCounter.put(letter, 0);
        }
    }
    public void settingNewDay(){
        System.out.println(computerDataCounter);
        Map<String, Integer> keyCounter1 = new HashMap();
        Map<Integer, Integer> mouseCounter1 = new HashMap(); //1 for left, 2 for right, 0 for total
        Map<String, Long> computerDataCounter1 = new HashMap(); //upTime, write, delete
        keyCounter1.putAll(keyCounter);
        mouseCounter1.putAll(mouseCounter);
        computerDataCounter1.putAll(computerDataCounter);
        writingStorage.setYesterdayStatistics(new counterStorage(keyCounter1,mouseCounter1,computerDataCounter1));

        Map<String, Integer> keyCounterTotal=new HashMap();
        Map<Integer, Integer> mouseCounterTotal=new HashMap(); //1 for left, 2 for right, 0 for total
        Map<String, Long> computerDataCounterTotal=new HashMap(); //upTime, write, delete
        try{
            keyCounterTotal= writingStorage.getTotalStatistics().getKeyCounter();
            mouseCounterTotal=writingStorage.getTotalStatistics().getMouseCounter();
            computerDataCounterTotal=writingStorage.getTotalStatistics().getComputerDataCounter();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for (Map.Entry<String, Integer> entry : keyCounter1.entrySet()) {
            try{
                keyCounterTotal.put(entry.getKey(),
                        entry.getValue()+keyCounterTotal.get(entry.getKey()));
            }
            catch (Exception e)
            {
                if(keyCounterTotal.containsKey(entry.getKey()))
                    continue;
                keyCounterTotal.put(entry.getKey(),entry.getValue());
            }
        }
        for (Map.Entry<Integer, Integer> entry : mouseCounter1.entrySet()) {
            try{
                mouseCounterTotal.put(entry.getKey(),
                        entry.getValue()+mouseCounterTotal.get(entry.getKey()));
            }
            catch (Exception e)
            {
                if(mouseCounterTotal.containsKey(entry.getKey()))
                    continue;
                mouseCounterTotal.put(entry.getKey(),entry.getValue());
            }
        }
        for (Map.Entry<String, Long> entry : computerDataCounter1.entrySet()) {
            try{
                computerDataCounterTotal.put(entry.getKey(),
                        entry.getValue()+computerDataCounterTotal.get(entry.getKey()));
            }
            catch (Exception e)
            {
                if(computerDataCounterTotal.containsKey(entry.getKey()))
                    continue;
                computerDataCounterTotal.put(entry.getKey(),entry.getValue());
            }
        }
        writingStorage.setTotalStatistics(new counterStorage(keyCounterTotal,mouseCounterTotal,computerDataCounterTotal));

        clear();
        writingCountingObj();
    }
    public void clear()
    {
        keyCounter.clear();
        mouseCounter.clear();
        computerDataCounter.clear();
        keyCounter.put("total",0);
        mouseCounter.put(0,0);
        computerDataCounter.put("upTime", (long) 0);
        computerDataCounter.put("write", (long) 0);
        computerDataCounter.put("delete", (long) 0);
    }
    public void saveExit() {
        writingCountingObj();
        System.exit(0);
    }

    public void writingCountingObj() {
        try {
            FileOutputStream file = new FileOutputStream(objectFileName);
            ObjectOutputStream fout = new ObjectOutputStream(file);
            counterStorage anotherWriterStorage = new counterStorage();
            anotherWriterStorage=writingStorage;
            anotherWriterStorage.setKeyCounter(keyCounter);
            anotherWriterStorage.setMouseCounter(mouseCounter);
            anotherWriterStorage.setComputerDataCounter(computerDataCounter);
            anotherWriterStorage.setSavedDay(initialDay);
            fout.writeObject(anotherWriterStorage);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialWritingObj()
    {
        try {
            FileOutputStream file = new FileOutputStream(objectFileName);
            ObjectOutputStream fout = new ObjectOutputStream(file);
            counterStorage AnotherwritingStorage=new counterStorage();
            fout.writeObject(AnotherwritingStorage);

            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readingCountingObj() {
        try {
            FileInputStream f1 = new FileInputStream(objectFileName);
            ObjectInputStream fin = new ObjectInputStream(f1);
            counterStorage readingStorage = (counterStorage) fin.readObject();
            computerDataCounter=(readingStorage.getComputerDataCounter());
            keyCounter=(readingStorage.getKeyCounter());
            mouseCounter=(readingStorage.getMouseCounter());
            writingStorage=readingStorage;
            initialDay=readingStorage.getSavedDay();
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
            counterStorage c1 = new counterStorage();
            computerDataCounter=(c1.getComputerDataCounter());
            keyCounter=(c1.getKeyCounter());
            mouseCounter=(c1.getMouseCounter());
            writingStorage.setTotalStatistics(new counterStorage());
            writingStorage.setYesterdayStatistics(new counterStorage());
        }
    }

    public static counterStorage getWritingStorage() {
        return writingStorage;
    }

    public static void setWritingStorage(counterStorage writingStorage) {
        keyAndMouseCounterbackup.writingStorage = writingStorage;
    }
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        Integer click = nativeMouseEvent.getButton();

        try {
            mouseCounter.put(click, mouseCounter.get(click) + 1);
            mouseCounter.put(0, mouseCounter.get(0) + 1);
        } catch (Exception e) {
            e.printStackTrace();
            mouseCounter.put(click, 0);
        }
        if (new Date().getTime() - initialTime.getTime() > frequencyOfUpdate) {
            updateComputerDataCounter();
            writingCountingObj();
            Calendar c2 = Calendar.getInstance();
            //System.out.println("every"+initialDay);
            //System.out.println("where am i?"+c2.get(Calendar.DAY_OF_YEAR));
            if(c2.get(Calendar.DAY_OF_YEAR)!=initialDay)
            {
                settingNewDay();
                //System.out.println("before"+initialDay);
                initialDay=c2.get(Calendar.DAY_OF_YEAR);
                //System.out.println("after"+initialDay);
            }
        }
    }
    public void updateComputerDataCounter()
    {
        long currentDiskSize=new File("/").getFreeSpace();
        if(initialFreeDiskSpace>currentDiskSize)
        {
            computerDataCounter.put("write",computerDataCounter.get("write")+(initialFreeDiskSpace-currentDiskSize));
        }
        else
        {
            computerDataCounter.put("delete",computerDataCounter.get("delete")+(currentDiskSize-initialFreeDiskSpace));
        }
        initialFreeDiskSpace =currentDiskSize;
        computerDataCounter.put("upTime",computerDataCounter.get("upTime")+(new Date().getTime()-initialTime.getTime()));
        initialTime=new Date();
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