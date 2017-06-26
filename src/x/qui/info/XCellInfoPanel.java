package x.qui.info;

import x.logic.Cell;
import x.logic.Human;
import x.logic.Plant;
import x.qui.format.XFormatter;
import x.qui.main.XMainPanel;
import x.ucf.XUcfCoder;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static x.logic.force.XForce.date;

/**
 * Created by орда on 30.03.2017.
 */
public class XCellInfoPanel extends JPanel {
    private JLabel info;

    public XCellInfoPanel(){
        setupView();
        setupInfo();
        setVisible(true);
    }

    private void setupView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Cell Info"));
        //min & max sizes
        setPreferredSize(new Dimension(500,0));
    }
    private void setupInfo() {
        this.info = new JLabel("-", SwingConstants.LEFT);
        add(this.info);
    }
    public void update(int y, int x){
        Cell current = XMainPanel.mapPanel.getCell(y,x);

        String info = String.format(//"<html>Raw: %s"+
                "<html> x: %s  y: %s"
                        +"<br>"+ "<br> Date: %s"
                + "<br> Landscape type: %s" +
                "<br>",
                x,y,
                date, current.getLandscape());

        Plant currPlant = current.getPlant();
        if (currPlant != null) info+=String.format( "<br> Plant type: %s" +
                        "<br> Plant fruits: %s",
                current.getPlant().getType(),
                current.getPlant().getFruits());
        info += String.format("<br>"+"<br> Apples: %s", current.getFruit());

        Human currMan = current.getHuman();
        if (currMan!= null) info+=String.format("<br> Generation: %s" +
                                                    "<br> Human sex: %s" +
                                                    "<br> Human age: %s" +
                                                    "<br> Human energy: %s" +
                                                    "<br> Human satiety: %s" +
                                                    "<br> Human pregnancy: %s" +
                                                    " days <br>",
                                                    current.getHuman().getGeneration(),
                                                    current.getHuman().transformSex(),
                                                    XFormatter.formatDate(current.getHuman().getDays()),
                                                    current.getHuman().getEnergy(),
                                                    current.getHuman().getSatiety(),
                                                    current.getHuman().getPregnancy());

        this.info.setText(info);
    }
    public void reset() {
        this.info.setText("-");
    }
}
