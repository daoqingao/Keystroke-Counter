import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
public class trayIconSystem  {
    public static void main(String []args){
        keyAndMouseCounter k1 = new keyAndMouseCounter();
        k1.run();
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image  = null;
        try {
            image = ImageIO.read(trayIconSystem.class.getResource("Icon_47-512.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PopupMenu trayPopupMenu = new PopupMenu();
        MenuItem startingViewer = new MenuItem("View Statistics");
        startingViewer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class c1 = Class.forName("ViewerFormat");
                    ViewerFormat.myLaunch(c1);
                    System.out.println(k1.getKeyCounter());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        trayPopupMenu.add(startingViewer);
        MenuItem exitingCounter = new MenuItem("Exit");
        exitingCounter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                k1.saveExit();
            }
        });
        trayPopupMenu.add(exitingCounter);
        TrayIcon trayIcon = new TrayIcon(image, "KeyStrokeCounter", trayPopupMenu);
        trayIcon.setImageAutoSize(true);
        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
        System.out.println("end of main");

    }//end of main
}//end of class