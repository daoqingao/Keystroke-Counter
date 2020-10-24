import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class ViewerFormat extends Application implements Initializable {
    private int limit=300;
    private static List<Map.Entry<String, Integer>> GlobalSortedList;
    //LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
    private static Map<String, Integer> keyCounter = new HashMap();
    private static Map<Integer, Integer> mouseCounter = new HashMap(); //1 for left, 2 for right, 0 for total
    private static Map<String, Long> computerDataCounter = new HashMap(); //upTime, write, delete
    private counterCollection counterStatistics = keyAndMouseCounter.getCounterCollectionSaveDataAsObject();
    @FXML PieChart pieChart1;
    @FXML LineChart <String,Number>chart1;
    @FXML TableView<keyToCount> tableView1;
    @FXML TableColumn<keyToCount, String> key;
    @FXML TableColumn<keyToCount, Integer> count;
    @FXML Label keyLabel1;
    @FXML Label labelLeftClick;
    @FXML Label labelRightClick;
    @FXML Label labelTotalClick;
    @FXML Label labelMiddleClick;
    @FXML AnchorPane background1;
    @FXML Label labelUptime;
    @FXML Label labelWrite;
    @FXML Label labelDelete;
    @FXML Label dataDay;

    public static void main(String[] args) {
        launch(args);
    }

    public void btn(javafx.event.ActionEvent actionEvent)
    {
        sort();
        generateAll();;
    }
    public void refresh(KeyEvent event)
    {
        pieChart1.setAnimated(false);
        sort();
        generateAll();
        pieChart1.setAnimated(true);
    }
    public void refresh2(MouseEvent event)
    {
        pieChart1.setAnimated(false);
        sort();
        generateAll();
        pieChart1.setAnimated(true);
    }
    public void today(javafx.event.ActionEvent actionEvent){
        keyCounter=counterStatistics.getKeyCounter();
        mouseCounter=counterStatistics.getMouseCounter();
        computerDataCounter=counterStatistics.getComputerDataCounter();
        dataDay.setText("Today");
        generateAll();
    }
    public void yesterday(javafx.event.ActionEvent actionEvent){
        keyCounter=counterStatistics.getYesterdaykeyCounter();
        mouseCounter=counterStatistics.getYesterdaymouseCounter();
        computerDataCounter=counterStatistics.getYesterdaycomputerDataCounter();
        dataDay.setText("Yesterday");
        generateAll();
    }
    public void total(javafx.event.ActionEvent actionEvent){
        //counterStatistics=keyAndMouseCounter.getWritingStorage();
        //counterStatistics=counterStatistics.getTotalStatistics();
        keyCounter=counterStatistics.getTotalkeyCounter();
        mouseCounter=counterStatistics.getTotalmouseCounter();
        computerDataCounter=counterStatistics.getTotalcomputerDataCounter();
        dataDay.setText("Past Total");
        generateAll();
    }
    public void generateLineChart()
    {
        limit=17;
        chart1.getData().clear();
        chart1.layout();
        XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
        for (Map.Entry<String, Integer> entry : GlobalSortedList) {
            if(entry.getKey().equals("total"))
                continue;
            series1.getData().add(new XYChart.Data<String,Number>(entry.getKey(),entry.getValue()));
            limit--;
            if(limit==0)
                break;
        }
        series1.setName("Key");
        chart1.getData().add(series1);
    }
    public void generatePieChart()
    {
        limit=25;
        pieChart1.getData().clear();
        ObservableList pieDataList = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : GlobalSortedList) {
            if(entry.getKey().equals("total"))
                continue;
            pieDataList.add(new PieChart.Data(entry.getKey(), entry.getValue()));
            limit--;
            if(limit==0)
                break;
        }

        pieChart1.setData(pieDataList);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root3 = FXMLLoader.load(getClass().getResource("/viewerFormat.fxml"));
        Scene s1 = new Scene(root3);
        primaryStage.setTitle("My Statistics");
        primaryStage.setScene(s1);
        primaryStage.show();
    }
    private static boolean javaFxLaunched = false;
    public static void myLaunch(Class<? extends Application> applicationClass) {
        if (!javaFxLaunched) { // First time
            Platform.setImplicitExit(false);
            new Thread(()->Application.launch(applicationClass)).start();
            javaFxLaunched = true;
        } else { // Next times
            Platform.runLater(()->{
                try {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        counterStatistics=keyAndMouseCounter.getCounterCollectionSaveDataAsObject();
        //counterStatistics=counterStatistics.getTotalStatistics();
        keyCounter=counterStatistics.getKeyCounter();
        mouseCounter=counterStatistics.getMouseCounter();
        computerDataCounter=counterStatistics.getComputerDataCounter();

        generateAll();
    }
    public void generateAll()
    {
        sort();
        ObservableList<keyToCount> chartList = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : GlobalSortedList) {
            if(entry.getKey().equals("total"))
            {
                keyToCount k1 = new keyToCount("Total",entry.getValue());
                chartList.add(k1);
                continue;
            }
            keyToCount k1= new keyToCount(entry.getKey(),entry.getValue());
            chartList.add(k1);
        }
        key.setCellValueFactory(new PropertyValueFactory<keyToCount,String>("key"));
        count.setCellValueFactory(new PropertyValueFactory<keyToCount,Integer>("count"));
        tableView1.setItems(chartList);

        generatePieChart();
        generateLineChart();
        //Map<String, Integer> keycounter = keyAndMouseCounter.getKeyCounter();
        keyLabel1.setText       ("0");
        labelTotalClick.setText ("0");
        labelLeftClick.setText  ("0");
        labelRightClick.setText ("0");
        labelMiddleClick.setText("0");
        labelUptime.setText     ("0");
        labelWrite.setText      ("0");
        labelDelete.setText     ("0");
        Calendar c1 = Calendar.getInstance();
        c1.clear();
        //System.out.println(computerDataCounter.get("upTime"));
        labelUptime.setText(new DecimalFormat("#.##").format((double)(computerDataCounter.get("upTime"))/3600000)+" Hours");
        labelWrite.setText(((double)(computerDataCounter.get("write"))/1000000000+" GB"));
        labelDelete.setText(((double)(computerDataCounter.get("delete"))/1000000000+" GB"));
        keyLabel1.setText(Integer.toString(keyCounter.get("total")));
        try{

            labelTotalClick.setText(Integer.toString(mouseCounter.get(0)));
            labelLeftClick.setText(Integer.toString(mouseCounter.get(1)));
            labelRightClick.setText(Integer.toString(mouseCounter.get(2)));
            labelMiddleClick.setText(Integer.toString(mouseCounter.get(3)));

        }
        catch (Exception e)
        {

        }

    }

    public void sort(){
        List<Map.Entry<String, Integer>> list2 = new LinkedList<Map.Entry<String, Integer>>(keyCounter.entrySet());
        list2.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (o1.getValue()<o2.getValue()) {
                    // compare two object and return an integer
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        //System.out.println(list2);
        GlobalSortedList=list2;
    }

    public class keyToCount{
        public Integer getCount() {
            return count.get();
        }
        public String getKey() {
            return key.get();
        }

        private SimpleIntegerProperty count;
        private SimpleStringProperty key;
        public keyToCount(String key,Integer count)
        {
            super();
            this.key=new SimpleStringProperty(key);
            this.count= new SimpleIntegerProperty(count);
        }
    }
}
