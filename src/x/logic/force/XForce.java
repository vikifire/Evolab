package x.logic.force;

import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import x.logic.Cell;
import x.logic.Human;
import x.logic.Neural;
import x.logic.Plant;
import x.logic.statistic.XStatistic;
import x.qui.XMapPanel;
import x.qui.info.XCellInfoPanel;
import x.qui.main.XMainPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static x.qui.main.XMainPanel.mapPanel;
//import static x.logic.statistic.XStatistic;

/**
 * Created by орда on 08.04.2017.
 */
public class XForce {

    public static int date = 1;// время
    private static Timer timer;
    private static int timerDelay = 500;
    public ArrayList<Human> men_c;

    public void start() {
        if (timer == null) {
            //mapPanel.setupHumans();
            //stat = new XStatistic();

            timer = new Timer(timerDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    XMapPanel map = mapPanel;

                    men_c = new ArrayList<>();
                    men_c.addAll(map.men);

                    for (Plant tree: map.trees) {
                        tree.tryToMakeFruits();
                    }

                    for (Plant tree: map.trees) {
                        if (act(tree)) break;
                    }

                    checkForDead();

                    for (Human man: men_c) {//act() for each human:men ...
                        if (man != null) man.tryToDie(); // check at the start if he dead
                        if (man != null)
                            if ( !man.tryToGiveBirth() ) act(man); // if woman haven't gave birth then make move
                        map.repaint();
                    }

                    int y = map.getSelectedRow();
                    int x = map.getSelectedColumn();
                    XCellInfoPanel cellInfo = XMainPanel.cellInfoPanel;
                    if (y != -1 && x != -1) {
                        cellInfo.update(y,x);
                        }
                    else { cellInfo.reset();  }

                            date++;
                    map.repaint();
                }
            });
        // time is out - need to do sth
        }
        timer.start();
    }
    public static void changeDelay(int change) {
        if (timer.getDelay() > 0  && timer.getDelay() < 3000) timer.setDelay( timer.getDelay()+change);
    }
    public static int getDelay() {
        return timer.getDelay();
    }

    public static void pause() {
        if (timer != null) {
            timer.stop();
        }
    }
    public static void stop() {
        if (timer != null) timer.stop();
        date = 1; // start one more time

    }
    public static void printAction(Human man,int option) {
        System.out.print("GEN"+man.getGeneration()+" [Day "+date+"] "+man.x + ":" + man.y + " " + man.transformSex());
        switch (option) {
//            case Neural.O_GIVE_BIRTH: {
//                System.out.println(" Give Birth");
//                break;
//            }
            case Neural.O_MAKE_CHILD: {
                System.out.println(" Make Child");
                break;
            }
//            case Neural.O_MOVE: {
//                System.out.println(" Move");
//                break;
//            }
            case Neural.O_EAT: {
                System.out.println(" Eat");
                break;
            }
            case Neural.O_SLEEP: {
                System.out.println(" Sleep");
                break;
            }
            default: {
                // do nothing
                break;
            }
        }
    }
    public static void doOption(Human man, int option) {

//        System.out.print("[Day "+date+"] ");
        if (man.getGeneration()>10 ) printAction(man,option);

        switch(option){
//            case Neural.O_GIVE_BIRTH: {
//                man.tryToGiveBirth();
//                break;
//            }
            case Neural.O_MAKE_CHILD: {
                man.tryToMakeChild();
                break;
            }
//            case Neural.O_MOVE: {
//                man.tryToMove(0,0);
//                break;
//            }
            case Neural.O_EAT: {
                man.tryToEat();
                break;
            }
            case Neural.O_SLEEP: {
                man.tryToSleep();
                break;
            }
//            case Neural.O_DIE: {
//                man.tryToDie();
//                //man.tryToMove(0,0); // чтоб не пытались суицидничать
//                break;
//            }
            default: {
                // do nothing

                break;
            }
        }
    }

    public static void act(Human man) { // вот здесь нужноооо работааааааааать
       //float[] choices = man.brain.think(man);
        int option_num = man.brain.think(man);
        doOption(man,option_num);
//        if (!man.tryToDie())
//            if (!man.tryToGiveBirth())
//                if (!man.tryToSleep() )
//                    if (!man.tryToEat())
//                        if (!man.tryToMakeChild())
//                            if (!man.tryToMove(0,0) ){
//                            // do nothing
//            }
//        // !!! NS: РАНДОМНОЕ НАПРАВЛЕНИЕ ЕГО ВЫБОР
    }

    public static boolean act(Plant tree) {
        return tree.tryToDropFruit();
    }
    public static void clearHuman (Human man) {
        man.setSatiety(0);
        man.setEnergy(0);
        mapPanel.getCell(man.y, man.x).setHuman(null);

        // looking for man in list, then erase
        for (Human curr: mapPanel.men) {
            if (curr == man) {mapPanel.men.remove(curr);
                                break;
            }
        }
        //this.finalize();
        // delete people from list
    }

    public static void checkForDead(){
        for (int yTemp = 0; yTemp < mapPanel.MAP_SIZE; yTemp++) { // run all around the array
            for (int xTemp = 0; xTemp < mapPanel.MAP_SIZE; xTemp++) {
                Cell cellTarget = mapPanel.getCell(yTemp, xTemp);
                if (cellTarget.getHuman() != null && (cellTarget.getHuman().getSatiety() <= 0 || cellTarget.getHuman().getEnergy() <= 0)) {
                    cellTarget.setHuman(null);
                }
            }
        }
    }
}
