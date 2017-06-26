package x.qui.main;
/**
 * Created by орда on 28.03.2017.
 */

import javax.swing.*;

public class XMainWindow extends JFrame {

    public XMainWindow() {
        setupView();

        add(new XMainPanel());

    }
    private void setupView(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // настройка интерфейса  (скина)
        } catch (Exception e) {
            e.printStackTrace();
        }
        //setIconImage(new ImageIcon("s").getImage());
        setTitle("Evolution Modelling");
       // setExtendedState(JFrame.MAXIMIZED_BOTH); // авто расширение
        setBounds(250,100,1000,800);

        /* Настройки*/
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //this.add(new XMapPanel());
        setVisible(true); // отрисовали окно
    }

}
