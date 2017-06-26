package x.qui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import x.logic.Cell;
import x.logic.Human;
import x.logic.Plant;
import x.logic.force.XForce;
import x.logic.statistic.XStatistic;
import x.qui.main.XMainPanel;
import x.ucf.XUcfCoder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Random;

import static x.logic.Human.START_AGE;
import static x.logic.Human.START_PEOPLE;
import static x.logic.Human.woman_sex;
import static x.logic.Plant.START_PLANTS;
import static x.qui.XControlPanel.MANBUTTON_PEOPLE;

/**
 * Created by орда on 28.03.2017.
 */
public class XMapPanel extends JTable {

    public ArrayList<Cell> cells = new ArrayList<Cell>();
    public ArrayList<Human> men = new ArrayList<Human>();
    public ArrayList<Plant> trees = new ArrayList<Plant>();
    public static XStatistic stat;
    public XForce force = new XForce();

    Random random = new Random();
    public final static int MAP_SIZE = 65; //129;
    // mapSize=2^n+1 для успешного метода генерации
    private final int CELL_SIZE = 10; //6;
    // метод генерации карт высот 2мерных карт
    //DefaultTableModel mapTable;


    public XMapPanel() {
        setupView();
        setupCells();
        setupListeners();
        setVisible(true);
    }
    public Cell getCell(int y, int x) {
        //TODO : FIND CELL IN ArrayList<cell>  //return (long) getValueAt(y,x);
        Cell current = cells.get(y*MAP_SIZE+x);
        return current;
    }

    private void setupView() {
        //mapTable = = new DefaultTableModel(MAP_SIZE,MAP_SIZE);
        setModel(new DefaultTableModel(MAP_SIZE,MAP_SIZE) {
                     @Override
                     public boolean isCellEditable(int row, int column) {
                         return false;
                     }
                 });
        setRowHeight(CELL_SIZE);
        for (int i = 0; i < MAP_SIZE; i++) {
            this.getColumnModel().getColumn(i).setMinWidth(CELL_SIZE);
            this.getColumnModel().getColumn(i).setMaxWidth(CELL_SIZE);
        }
        setBorder(BorderFactory.createLineBorder(new Color(0x333333)));
        setDefaultRenderer(Object.class, new XCellRenderer());

    }
    private void setupCells() {
        int[][] data = new int[MAP_SIZE][MAP_SIZE];
        // ArrayList<Cell> cells = new ArrayList<Cell>(); надо заполнить клетки значениями
        try {
            String pathname="src/resources/data/test-data.dat";
            FileInputStream stream = new FileInputStream(pathname);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(reader);//new FileReader(pathname));
//            for (int i = 0; i < MAP_SIZE; i++) {
//                buf = bufferedReader.readLine();
//                char mass[] = buf.toCharArray();
//                int k = 0, j=0;
//                while  (j < MAP_SIZE && k<mass.length) {
//                    if (mass[k]!=' ') {
//                        data[i][j] = Character.getNumericValue(mass[k]);
//                        j++;
//                        }
//                    k++;
//                }
//            }
            int symbol;
             for (int i = 0; i < MAP_SIZE; i++) {
                for (int j = 0; j < MAP_SIZE; j++) {
                    symbol = Character.getNumericValue(bufferedReader.read());
                    if (symbol != Character.getNumericValue(' ')) data[i][j]= symbol;
                    //System.out.println(symbol+" "+ Character.getNumericValue(' ')+Character.getNumericValue('\n'));
                }
                bufferedReader.readLine();
            }

            for (int i = 0; i < MAP_SIZE; i++) {
                for (int j = 0; j < MAP_SIZE; j++) {

                    setValueAt(data[i][j],i,j);
                    Cell cell = new Cell(data[i][j]);
                    cells.add(cell);
                }
            }

            // setupPlantTest();
            setupPlants();

            String pathname2="src/resources/data/save-brain.dat";
            FileInputStream stream2 = new FileInputStream(pathname2);
            InputStreamReader reader2 = new InputStreamReader(stream2);
            BufferedReader bufferedReader2 = new BufferedReader(reader2);//new FileReader(pathname));

            setupHumans(bufferedReader);
//            setupHumansTest(); // for fixing bug
        }
        catch (IOException e) {
            e.printStackTrace();
        }

//        FileInputStream fis =
//
//
//        ObjectInputStream ois = null;
//        try {
//            fis = new FileInputStream();
//            ois = new ObjectInputStream(fis);
//            data = (long[][]) ois.readObject();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (ois != null) {
//                try {
//                    ois.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        this.repaint();
        stat = new XStatistic();
    }

//    private final void generateLandscape(){
//        setValueAt(XUcfCoder.LANDSCAPE_TYPE_GROUND_HIGH, 0,0);
//        setValueAt(XUcfCoder.LANDSCAPE_TYPE_GROUND_HIGH, 0,MAP_SIZE-1);
//        setValueAt(XUcfCoder.LANDSCAPE_TYPE_GROUND_HIGH, MAP_SIZE-1,0);
//        setValueAt(XUcfCoder.LANDSCAPE_TYPE_GROUND_HIGH, MAP_SIZE-1,MAP_SIZE-1);
//
//        float landscapeShift = 15.0f;
//        for (int step = MAP_SIZE-1; step >=2; step/=2, landscapeShift /= 2.0) {
//            int smallStep = step / 2;
//
//            for (int x = smallStep; x < MAP_SIZE; x+= step) {
//                for (int y = smallStep; y < MAP_SIZE; y++ ) {
//                    int topLeftValue = (int) (getValueAt(y-smallStep,x-smallStep));
//                    int topRightValue = (int) (getValueAt(y-smallStep,x+smallStep));
//                    int bottomLeftValue = (int) (getValueAt(y+smallStep,x-smallStep));
//                    int bottomRightValue = (int) (getValueAt(y+smallStep,x+smallStep));
//                    float average = (topLeftValue+topRightValue+bottomLeftValue+bottomRightValue)/4;
//
//                    // shift = +/- 15
//                    float centralValue = average+random.nextInt(3)*landscapeShift-landscapeShift;
//                    setValueAt(centralValue,y,x);
//                }
//            }
//            //Square step
//            for (int x =0 ; x<MAP_SIZE; x += smallStep) {
//                for (int y = (x+smallStep)%step; y<MAP_SIZE; y+= step) {
//                    int topValue = (int) (getValueAt((y - smallStep + MAP_SIZE)%(MAP_SIZE-1),x));
//                }
//            }
//        }
//    }

@Override
public void setValueAt(Object aValue, int row, int column) {

        getModel().setValueAt(aValue, convertRowIndexToModel(row),
                convertColumnIndexToModel(column));
     //   this.fireTableDataChanged();

}
    public void setupPlants() {
//        newPlant(1, 20);
//        newPlant(MAP_SIZE/2, MAP_SIZE/2);
//        newPlant(MAP_SIZE/4, MAP_SIZE/4);
//        newPlant(3*MAP_SIZE/4, 3*MAP_SIZE/4);

        int habitableCells=0;
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (getCell(i,j).getLandscape() != Cell.LANDSCAPE_TYPE_WATER_LOW &&
                        getCell(i,j).getLandscape() != Cell.LANDSCAPE_TYPE_WATER_HIGH &&
                        getCell(i,j).getPlant() == null)
                    habitableCells++;
            }
        }
        int interval = habitableCells/ START_PLANTS; // интервал в котором будут генериться
        if (START_PLANTS < habitableCells)
        {  int i=0; //boolean Sex = random.nextBoolean();
            while (i<START_PLANTS) {
                int xC = random.nextInt(MAP_SIZE);
                int yC = random.nextInt(MAP_SIZE);
                Cell lookingCell = getCell(yC, xC);
                if (lookingCell.isHabitable()) {
//                    Human human = new Human(xC,yC, Sex);
                    newPlant(yC,xC);
                    i++;
                }
            }
        }
    }
    public void setupPlantTest() {
        int xC = 30;
        int yC = 30;
        Cell lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable())
            newPlant(yC, xC);
    }
    public void setupHumansTest() {
        int i = 0;
        boolean Sex = woman_sex;

        int xC = 29, yC = 30;
        Cell lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }
        xC = 28; yC = 30;
        lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }
        xC = 29; yC = 29;
        lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }

        xC = 29; yC = 31;
        lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }

        xC = 30; yC = 29;
        lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }
        xC = 31; yC = 29;
        lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }
        xC = 31; yC = 30;
        lookingCell = getCell(yC, xC);
        if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
            Human human = new Human(xC, yC, Sex);
            human.setAge(30);
            lookingCell.setHuman(human);
            men.add(human);
            System.out.println(xC + ":" + yC + " New " + human.transformSex() + " added");
            i++;

        }
    }
    public void setupNewHumans() {
        int habitableCells=0;
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (getCell(i,j).getLandscape() != Cell.LANDSCAPE_TYPE_WATER_LOW &&
                        getCell(i,j).getLandscape() != Cell.LANDSCAPE_TYPE_WATER_HIGH &&
                        getCell(i,j).getPlant() == null)
                    habitableCells++;
            }
        }
        int interval = habitableCells/ MANBUTTON_PEOPLE; // интервал в котором будут генериться
        //boolean sex = true; // !!!!!!! ЗДЕСЬ БУДЕТ ВЫБИРАТЬСЯ ПОЛ , можно разные значения энергии и сытости и живучести сделать и прочее
        //getCell(5,30).setHuman(new Human());

        if (MANBUTTON_PEOPLE < habitableCells)
        {  int i=0;
         boolean Sex = woman_sex;
            while (i<MANBUTTON_PEOPLE) {
                int xC = random.nextInt(MAP_SIZE);
                int yC = random.nextInt(MAP_SIZE);
                Cell lookingCell = getCell(yC, xC);
                if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
                    Human human = new Human(xC,yC,Sex);
                    human.setAge(START_AGE);
                    lookingCell.setHuman(human);
                    men.add(human);
                    System.out.println(xC+":"+yC+" New "+human.transformSex()+" added");
                    i++;
                    Sex = !Sex;
                }
            }
        }
    }

    public void setupHumans(BufferedReader bufferedReader) {
        //
        int habitableCells=0;
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                if (getCell(i,j).getLandscape() != Cell.LANDSCAPE_TYPE_WATER_LOW &&
                        getCell(i,j).getLandscape() != Cell.LANDSCAPE_TYPE_WATER_HIGH &&
                            getCell(i,j).getPlant() == null)
                    habitableCells++;
            }
        }
        int interval = habitableCells/ START_PEOPLE; // интервал в котором будут генериться
        //boolean sex = true; // !!!!!!! ЗДЕСЬ БУДЕТ ВЫБИРАТЬСЯ ПОЛ , можно разные значения энергии и сытости и живучести сделать и прочее
        //getCell(5,30).setHuman(new Human());

        if (START_PEOPLE < habitableCells)
        {  int i=0; boolean Sex = random.nextBoolean();
            while (i<START_PEOPLE) {
                int xC = random.nextInt(MAP_SIZE);
                int yC = random.nextInt(MAP_SIZE);
                Cell lookingCell = getCell(yC, xC);
                if (lookingCell.isHabitable() && lookingCell.getHuman() == null) {
                    Human human = new Human(xC,yC,Sex);
                    human.setAge(START_AGE);
                    lookingCell.setHuman(human);
                    men.add(human);
                    i++;
                    Sex = !Sex;
                }
            }
        }
    }
    public void newPlant(int r, int c) {
        Cell now = getCell(r, c);
            Plant newTree = new Plant(r, c);
            now.setPlant(newTree);
            trees.add(newTree);
    }
    public static boolean isCellinMapRange(int y, int x) {
        return y>=0 && y < XMapPanel.MAP_SIZE && x>=0 && x<XMapPanel.MAP_SIZE;
    }
    public final int getValueAtRange(int value, int min, int max) {
        return Math.max(Math.min(value,max),min);
    }

    public void setupListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                int x = rowAtPoint(e.getPoint());
                int y = columnAtPoint(e.getPoint());
                XMainPanel.cellInfoPanel.update(x,y);
            }
        });
    }
}
