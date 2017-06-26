package x.qui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import x.logic.Cell;
import x.logic.Human;
import x.logic.Plant;
import x.qui.main.XMainPanel;
import x.ucf.XUcfCoder;

import static x.logic.Human.MAX_SATIETY;

/**
 * Created by орда on 02.04.2017.
 */
public class XCellRenderer extends DefaultTableCellRenderer {
    private final static int COMMON_COLOR_EMPTY = 0xF0F0F0;
    private final static int COMMON_COLOR_UNDEFINED = 0xC22E35;
    private final static int LANDSCAPE_TYPE_COLOR_WATER_LOW = 0x68ADBA;
    private final static int LANDSCAPE_TYPE_COLOR_WATER_HIGH = 0x8EBAC8;
    private final static int LANDSCAPE_TYPE_COLOR_GROUND_LOW = 0xDDB985;
    private final static int LANDSCAPE_TYPE_COLOR_GROUND_HIGH = 0xD1AF7D;
    private final static int LANDSCAPE_TYPE_COLOR_GRASS_LOW = 0xB3D77E;
    private final static int LANDSCAPE_TYPE_COLOR_GRASS_HIGH = 0xA8C976;

    private final static int HUMAN_TYPE_COLOR_MAN_GREAT = 0x3053AF;
    private final static int HUMAN_TYPE_COLOR_MAN = 0x0000FF;
    private final static int HUMAN_TYPE_COLOR_MAN_DYING = 0x000060;


    private final static int HUMAN_TYPE_COLOR_WOMAN_GREAT = 0xFF00FF;
    private final static int HUMAN_TYPE_COLOR_WOMAN = 0xB82C8F;
//    private final static int HUMAN_TYPE_COLOR_WOMAN = 0xFF0000;
    private final static int HUMAN_TYPE_COLOR_WOMAN_DYING = 0x800080;
    private final static int HUMAN_TYPE_COLOR_BABY = 0xFFFFFF;

    private final static int TREE_TYPE_COLOR_X = 0x23C451D;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    super.getTableCellRendererComponent(table, "",isSelected, hasFocus, row, column); // было - Object ;   // (long) value
    setHorizontalAlignment(SwingConstants.CENTER);
        Cell now = XMainPanel.mapPanel.getCell(row,column);
        int color_main = colorIfHumanPlant(now, getBackgroundColor((int) value));

        setBackground(new Color(color_main));
        //setForeground(new Color(color_main)); // trees humans
//    if(isSelected)
//    { setBorder(BorderFactory.createLineBorder(Color.black)); }
    return this;
    }
    private int colorIfHumanPlant(Cell cell, int color) {
        int colorTransformed = color;

        if (cell.getPlant() != null) {
            if (cell.getPlant().getType() == Plant.PLANT_TYPE_X) {
                colorTransformed = TREE_TYPE_COLOR_X;
            }
            else  colorTransformed = COMMON_COLOR_UNDEFINED;
        }
        if (cell.getHuman() != null) {

            if (cell.getHuman().getDays() < 3) {
                colorTransformed = HUMAN_TYPE_COLOR_BABY;
            }

            else if (!cell.getHuman().ifWoman()) {
                if (cell.getHuman().getSatiety()> 2.0f*(float) Human.MAX_SATIETY/3.0f
                        && cell.getHuman().getEnergy()> 2.0f * (float) Human.MAX_ENERGY/3.0f )
                        colorTransformed = HUMAN_TYPE_COLOR_MAN_GREAT;
                else if (cell.getHuman().getSatiety()< 1.0f*(float) Human.MAX_SATIETY/3.0f
                        || cell.getHuman().getEnergy() < 1.0f *(float) Human.MAX_ENERGY/3.0f)
                            colorTransformed = HUMAN_TYPE_COLOR_MAN_DYING;
                    else colorTransformed = HUMAN_TYPE_COLOR_MAN;
            }
            else {
                if (cell.getHuman().getSatiety()> 2.0f*(float) Human.MAX_SATIETY/3.0f
                        && cell.getHuman().getEnergy()> 2.0f * (float) Human.MAX_ENERGY/3.0f )
                    colorTransformed = HUMAN_TYPE_COLOR_WOMAN_GREAT;
                else if (cell.getHuman().getSatiety()< 1.0f*(float) Human.MAX_SATIETY/3.0f
                        || cell.getHuman().getEnergy() < 1.0f *(float) Human.MAX_ENERGY/3.0f)
                    colorTransformed = HUMAN_TYPE_COLOR_WOMAN_DYING;
                else colorTransformed = HUMAN_TYPE_COLOR_WOMAN;
            }
        }

        return colorTransformed;
    }

    private int getBackgroundColor(int value) {
        int color = COMMON_COLOR_EMPTY;
        //int landscapeType = current.getLandscape();
        switch (value) {
            case Cell.LANDSCAPE_TYPE_WATER_LOW: {
                color = LANDSCAPE_TYPE_COLOR_WATER_LOW;
                break;
            }
            case Cell.LANDSCAPE_TYPE_WATER_HIGH: {
                color = LANDSCAPE_TYPE_COLOR_WATER_HIGH;
                break;
            }
            case Cell.LANDSCAPE_TYPE_GROUND_LOW: {
                color = LANDSCAPE_TYPE_COLOR_GROUND_LOW;
                break;
            }
            case Cell.LANDSCAPE_TYPE_GROUND_HIGH: {
                color = LANDSCAPE_TYPE_COLOR_GROUND_HIGH;
                break;
            }
            case Cell.LANDSCAPE_TYPE_GRASS_LOW: {
                color = LANDSCAPE_TYPE_COLOR_GRASS_LOW;
                break;
            }
            case Cell.LANDSCAPE_TYPE_GRASS_HIGH: {
                color = LANDSCAPE_TYPE_COLOR_GRASS_HIGH;
                break;
            }
//            case Cell.PLANT_TYPE_X: {
//                color = TREE_TYPE_COLOR_X;
//                break;
//            }
            default: {
                color = COMMON_COLOR_UNDEFINED;
                break;
            }
        }

       return color;
    }
//    private static final int getForegroundColor(int value) {
//        int color = COMMON_COLOR_EMPTY;
//        boolean humanType = current.getHuman().ifWoman();
//        Human humanHere = current.getHuman();
//        Plant plantHere = current.getPlant();
//        int plantType = current.getPlant().getType();
//        if (humanHere == null || plantHere == null) { // !!!
//            if (humanHere != null) {
//               if (humanType == Human.man_sex) {
//                        color = HUMAN_TYPE_COLOR_MAN;
//                    }
//                    else if (humanType == Human.woman_sex) {
//                        color = HUMAN_TYPE_COLOR_WOMAN;
//                    }
//                    else {
//                        color = COMMON_COLOR_UNDEFINED;
//                    }
//                }
//            }
//            else if (plantHere != null) {
//                switch (plantType) {
//                    case Plant.PLANT_TYPE_X: {
//                        color = TREE_TYPE_COLOR_X;
//                        break;
//                    }
//                    default: {
//                        color = COMMON_COLOR_UNDEFINED;
//                        break;
//                    }
//                }
//            }
//        return color;
//    }
}
