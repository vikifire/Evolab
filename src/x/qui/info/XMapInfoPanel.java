package x.qui.info;

import x.qui.format.XFormatter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by орда on 30.03.2017.
 */
public class XMapInfoPanel extends JPanel {
    private JLabel info;

    public XMapInfoPanel() {
        setupView();
        setupInfo();
        setVisible(true);
    }

    public final void update(int days) {
//        String info = String.format("<html>Date: %s"+
//                        "<br>" +
//                        "<br> Common area: %s" +
//                        "<br> &nbsp Water area: %s" +
//                        "<br> &nbsp Land area: %" +
//                        "<br> People: $s" +
//                        "<br> &nbsp Men: %s" +
//                "<br> &nbsp Women: %s" +
//                "<br> &nbsp &nbsp Pregnant: %s" +
//                "<br> Children were born: %s" +
//                "<br> Children died: %s" +
//                "<br> People died: %s" +
//                "<br> &nbsp Low energy: %s" +
//                "<br> &nbsp Low satiety: %s" +
//                "<br> &nbsp Age: %s" +
//                "<br> &nbsp Lost: %s" +
//                "<br> People density: %.2f" +
//                "<br> People age average: %s" +
//                "<br> Plants: %s" +
//                "<br> Fruits: %s" +
//                "<br> Plants density: %.2f" +
//                //"<br> Plants fruits avg: %.2f" +
//                "<br> Plants fruits / People ratio: %.2f<html/>",
//                XFormatter.formatDateShort(days),
//
//        )
    }

    public void reset() {
        this.info.setText("-");
    }
    private void setupView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Map info"));
        setPreferredSize(new Dimension(200,0));
    }
    private void setupInfo() {
        this.info = new JLabel("-", SwingConstants.LEFT);
        add(this.info);
    }
}
