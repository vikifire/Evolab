package x.logic;

import x.logic.force.XForce;
import x.logic.random.XRandomGenerator;
import x.qui.XMapPanel;
import x.qui.main.XMainPanel;

import static x.logic.force.XForce.date;
import static x.qui.main.XMainPanel.mapPanel;

/**
 * Created by орда on 04.04.2017.
 */
public class Plant {

    public static final int PLANT_TYPE_EMPTY = 0x0;
    public static final int PLANT_TYPE_X = 0x7;

    public static final int START_PLANTS= 50; //160;
    private static final int START_FRUITS = 40;

    private int type;
    private int fruits;
    private int row; // y
    private int column; // x

    public Plant(int r, int c){
        type = PLANT_TYPE_X;
        fruits = START_FRUITS;
        row = r;
        column = c;
    }

    public void setFruits(int amount) {
        fruits = amount;
    }
    public int getFruits() {
        return fruits;
    }
    public void getRipe(int amount) {
        fruits += amount;
    }
    public int getType() {
        return type;
    }
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return column;
    }

    public void tryToMakeFruits(){
        if ( (XForce.date % 30) == 0 ) {
            this.setFruits(this.getFruits()+ XRandomGenerator.generateInteger(15,25) ); // 5-15 new fruits every month
        //XMainPanel.eventsInfoPanel.update(date, "+fruits");
        }
    }
    public boolean tryToDropFruit() {
        //XMapPanel map = XMainPanel.mapPanel;
        if (getFruits() != 0)  // нет фруктов на дереве- ничего не вырастет
        {
            boolean decision = XRandomGenerator.generateBoolean(1);
            if (decision) {
                setFruits(getFruits() - 1);
                // случайное направление падения плода
                int yTarget = row + XRandomGenerator.generateInteger(-1, 1);
                int xTarget = column + XRandomGenerator.generateInteger(-1, 1);
                if (mapPanel.isCellinMapRange(yTarget, xTarget) && (yTarget != row) && (xTarget != column)) {
                    Cell cellTarget = mapPanel.getCell(yTarget, xTarget);
                    if (cellTarget.getLandscape() != Cell.LANDSCAPE_TYPE_WATER_LOW
                            && cellTarget.getLandscape() != Cell.LANDSCAPE_TYPE_WATER_HIGH
                            && cellTarget.getHuman() == null
                            && cellTarget.getPlant() == null) { // добавляем дерево
                        Plant newTree = new Plant(yTarget, xTarget);
                        cellTarget.setPlant(newTree);

                        mapPanel.trees.add(newTree);
                        return true;

                    }

                }
            }
        }
        return false;
    }
}
