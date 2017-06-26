package x.logic.statistic;

import x.logic.Cell;
import x.logic.Human;
import x.logic.Plant;
import x.qui.XMapPanel;
import x.qui.format.XFormatter;
import x.qui.main.XMainPanel;

import static x.qui.main.XMainPanel.mapPanel;

/**
 * Created by орда on 13.04.2017.
 */
public class XStatistic {
    // LANDSCAPE
    public static int CELLS;
    //public static final int CELLS = mapPanel.cells.size()-1;
    public int CELLS_WATER;
    public int CELLS_LAND;
    // HUMAN
    public static int people;
    public int peopleMen;
    public int peopleWomen;

    public int peopleWomenPregnant = 0; // !
    public int peopleAge;   // !
    public static int ChildrenBorn = 0;
    public static int ChildrenDied = 0;
    public static int peopleDied = 0;
    public static int peopleDiedByAge = 0;
    public static int peopleDiedByEnergy = 0;
    public static int peopleDiedBySatiety = 0;
    public static int peopleDiedByLost = 0;

    // PLANT
    public int plants;
    public int plantsFruits;

    public XStatistic(){

    }

    // DENSITY
    public float getPeopleLandDensity() {
        return (float) people/CELLS_LAND;
    }
    public float getPlantsLandDensity() {
        return (float) plants/CELLS_LAND;
    }

    // MEAN
    public float getPeopleAgeMean() {
        return people != 0 ? (float) peopleAge / people : 0;
    }
    public int getPeopleYearsMean() {
        return XFormatter.formatYears(Math.round(getPeopleAgeMean()) );
    }

    public float getPlantsFruitsMean() {
        return plants != 0 ? (float) plantsFruits / plants : 0;
    }

    // RATIO
    public float getPlantsFruitsPeopleRatio() {
        return people != 0 ? (float) plantsFruits / people : plantsFruits;
    }

    public int countType(int type) {
        int amount = 0;
        for (Cell current: mapPanel.cells) {
            if (current.getLandscape() == type)
                amount++;
        }
        return amount;
    }
    public int countTypeWater(){
        return countType(Cell.LANDSCAPE_TYPE_WATER_HIGH)+countType(Cell.LANDSCAPE_TYPE_WATER_LOW);
    }
    public int countTypePeople(boolean sex){
        int amount = 0;
        for (Human man: mapPanel.men) {
            if (man.ifWoman() == sex) amount++;
        }
        return amount;
    }
    public int countFruitsPlants(){
        int amount = 0;
        for (Plant curr: mapPanel.trees) {
            amount+=curr.getFruits();
        }
        return amount;
    }
    public void setPeopleAge(int age) {
        peopleAge = age;
    }
    public final float getPeopleRatio(){ return (float)peopleMen / (float)people;}

    public final void update() {
        XMapPanel map = XMainPanel.mapPanel;
        // pregnant women && average age
        CELLS = mapPanel.cells.size();
        CELLS_WATER = countTypeWater();
        CELLS_LAND = CELLS-CELLS_WATER;
        if (mapPanel.men.size() != 0 ) people  = mapPanel.men.size()-1;
        else people = 0;
        peopleMen = countTypePeople(Human.man_sex);
        peopleWomen = countTypePeople(Human.woman_sex);

        plants = mapPanel.trees.size()-1;
        plantsFruits = countFruitsPlants();

        int peopleWomenPregnantT = 0;
        peopleAge = 0;

        for (Human man: map.men) {
            if ( (man.ifWoman() == Human.woman_sex) && (man.getPregnancy() > 0) )
                peopleWomenPregnantT++;
            peopleAge+=man.getDays();
        }
        peopleWomenPregnant = peopleWomenPregnantT;
        if (people != 0) peopleAge /= people;

    }
}
