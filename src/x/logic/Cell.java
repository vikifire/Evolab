package x.logic;

import x.qui.XMapPanel;

/**
 * Created by орда on 04.04.2017.
 */
public class Cell {
    public static final int LANDSCAPE_TYPE_EMPTY = 0x0;
    public static final int LANDSCAPE_TYPE_WATER_LOW = 0x1; // deep water
    public static final int LANDSCAPE_TYPE_WATER_HIGH = 0x2;
    public static final int LANDSCAPE_TYPE_GROUND_LOW = 0x3;
    public static final int LANDSCAPE_TYPE_GROUND_HIGH = 0x4;
    public static final int LANDSCAPE_TYPE_GRASS_LOW = 0x5;
    public static final int LANDSCAPE_TYPE_GRASS_HIGH = 0x6;

    public static final int PLANT_TYPE_EMPTY = 0x0;
    public static final int PLANT_TYPE_X = 0x7;
    public static final int PLANT_TYPE_APPLE = 0x8;

    public static final int DIR_STAY = 0;
    public static final int DIR_NORTH = 1;
    public static final int DIR_NORTH_EAST = 2;
    public static final int DIR_EAST = 3;
    public static final int DIR_SOUTH_EAST = 4;
    public static final int DIR_SOUTH = 5;
    public static final int DIR_SOUTH_WEST = 6;
    public static final int DIR_WEST = 7;
    public static final int DIR_NORTH_WEST = 8;


    private int landscape_type;
    private Human human;
    private Plant plant;
    private int fruits;

    //private
    public Cell() {
        landscape_type = LANDSCAPE_TYPE_EMPTY;
        human = null;
        plant = null;
        fruits = 0;
    }
    public Cell(int land) {
        landscape_type = land;
    }
    public Cell(int land, Human man, Plant tree) {
        landscape_type = land;
        human = man;
        plant = tree;
    }

    public int getLandscape() {
        return this.landscape_type;
    }
    public void setLandscape(int land) {
        landscape_type = land;
    }
    public Human getHuman() {
        return this.human;
    }
    public void setHuman(Human man) {
        this.human = man;
    }
    public int getFruit() { if (plant==null) return this.fruits;
                            else return plant.getFruits();
    }
    public void setFruit(int apples) {
       this.fruits=apples; // fruits number = apples
    }
    public void eatFruit () {
        if (fruits>0) fruits--;
        else if (fruits == 0) {
            if (plant!=null) plant.setFruits(plant.getFruits()-1);
        }
        else setFruit(0);
    }
    public Plant getPlant() {
        return this.plant;
    }
    public void setPlant(Plant tree) {
        this.plant = tree;
    }
    public boolean isHabitable() {
        if (landscape_type!=LANDSCAPE_TYPE_WATER_HIGH && landscape_type!=LANDSCAPE_TYPE_WATER_LOW
                && plant == null && human == null)
            return true;
        else return false;
    }


}
