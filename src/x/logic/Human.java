package x.logic;

//import x.logic.force.XForce;
import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.prism.impl.ps.CachingEllipseRep;
import x.logic.force.XForce;
//import x.logic.random.XRandomGenerator;
import x.logic.random.XRandomGenerator;
import x.logic.statistic.XStatistic;
import x.qui.XMapPanel;
import x.qui.info.XEventsInfoPanel;
import x.qui.main.XMainPanel;
import x.ucf.XUcfCoder;

import java.util.Random;

import static x.logic.force.XForce.date;
import static x.qui.format.XFormatter.MONTH_PERIOD;
import static x.qui.main.XMainPanel.mapPanel;

/**
 * Created by орда on 03.04.2017.
 */

public class Human {
    public static final int START_PEOPLE = 120; //300;
    public static final int START_AGE = 30;//*10*1;
    public static final int MAX_AGE = 30000;

    public static final int START_LAYERS = 5;
    public static final int START_ENERGY = 50;
    public static final int START_SATIETY = 50;
    public static final int MAX_ENERGY = 70;
    public static final int MAX_SATIETY = 70;
    //public static final int MAX_DAYS = 33000;
    public static final int PREGNANCY_PERIOD = 7;

    public static final boolean woman_sex = true;
    public static final boolean man_sex = false;


    public Neural brain; // our NN
    public final boolean sex;  // 0 - MAN , 1 - WOMAN
    private int days;
    private int generation;
    private int energy;
    private int satiety;
    private int pregnancy;
    private Neural childBrain;
    private float treeDist;
    private Human partner; // potential partner (closest one)

    public int direction;

    public int x;
    public int y;

    public Human(int xC, int yC) { // normal one-layer man
        Random rnd = new Random(System.currentTimeMillis());
        this.sex = rnd.nextBoolean();
        this.days = 0;
        this.energy = START_ENERGY;
        this.satiety = START_SATIETY;
        this.pregnancy = 0;
        this.x = xC;
        this.y = yC;
        this.partner = null;
        this.generation = 1;
        this.direction = 0;
        this.brain = new Neural(START_LAYERS); // 1 layer here
    }
    public Human(int xC, int yC, boolean Sex) {
        this.sex = Sex;
        this.days = 0;
        this.energy = START_ENERGY;
        this.satiety = START_SATIETY;
        this.pregnancy = 0;
        this.x = xC;
        this.y = yC;
        this.partner = null;
        this.generation = 1;
        this.direction = 0;
        this.brain = new Neural(START_LAYERS); // 1 layer
    }
    public Human(int xC, int yC, int layers) { // normal; can use for 2-3-4layer NN
        Random rnd = new Random(System.currentTimeMillis());
        this.sex = rnd.nextBoolean();
        this.days = 0;
        this.energy = START_ENERGY;
        this.satiety = START_SATIETY;
        this.pregnancy = 0;
        this.x = xC;
        this.y = yC;
        this.partner = null;
        this.direction = 0;
        this.brain = new Neural(layers); // 1 layer
    }
    public Human(int xC, int yC, Neural brain_from_parents, int gen) {
        Random rnd = new Random(System.currentTimeMillis());
        this.sex = rnd.nextBoolean();
        this.days = 0;
        this.energy = START_ENERGY;
        this.satiety = START_SATIETY;
        this.pregnancy = 0;
        this.x = xC;
        this.y = yC;
        this.partner = null;
        this.generation = gen+1;
        this.direction = 0;
        this.brain = brain_from_parents;
    }
    public boolean ifWoman() {
        return this.sex;
    }

    // детородные функции. можно усовершенствовать tryToMakeChild и
    // добавить как в treeDistance дистанцию до ближайшей женщины для мужика, чтоб тоже не бездействовал
    public boolean tryToGiveBirth() {
        if (pregnancy >= PREGNANCY_PERIOD ) {
           for (int yShift = -1; yShift < 2; yShift++) {
                for (int xShift = -1; xShift < 2; xShift++) {
                    int xTarget = x + xShift;
                    int yTarget = y + yShift;
                    if (mapPanel.isCellinMapRange(yTarget, xTarget)) {
                        Cell cellTarget = mapPanel.getCell(yTarget, xTarget);
                        if (cellTarget.getHuman() == null) {

                            Human human = new Human(xTarget, yTarget, childBrain,this.generation);

                            childBrain = null;
                            pregnancy = 0;
                            setEnergy(getEnergy() - 2);
                            setSatiety(getSatiety() - 2);
                            getOlder();
                            
                            cellTarget.setHuman(human);
                             mapPanel.men.add(human);
//                            mapPanel.men.add(mapPanel.men.indexOf(this)+1,human); // чтоб ребенок вставлялся перед или после? или -1
                            XStatistic.ChildrenBorn++;
                           // System.out.println("[Day"+date+"] "+this.x+":"+this.y+" Child was born ("+human.x+":"+human.y+")");
                            return true;
                        }
                    }
                }
            }
            setEnergy(getEnergy() - 2);
            setSatiety(getSatiety() - 2);
            getOlder();
            childBrain = null;
            pregnancy = 0;
            XStatistic.ChildrenDied++;
            //System.out.println("[Day"+date+"] "+this.x+":"+this.y+" Child died ");
            // XMainPanel.EventsInfoPanel.update (date,"Child died");
            // если кто-то небеременный попытался родить ребенка - защита от имбицилов с кашей в голове
            return true;
        }

        else{
            return false;
        }
    }
    public boolean tryToMakeChild(){
        XMapPanel map = XMainPanel.mapPanel;

        Human partner_now = findOutPartner();

        int yD = partner_now.y - y;
        int xD = partner_now.x - x;

        if (pregnancy == 0 && partner_now.pregnancy == 0) {

            if (Math.abs(xD) <= 1 &&  Math.abs(yD) <= 1) { // если партнер в пределах шаговой доступности
                    if (this.sex != Human.woman_sex ) { //ifWoman()) {
                        //System.out.println("[Day"+date+"] "+this.x+":"+this.y+" sex with "+partner_now.x+":"+partner_now.y);
                        if (this.sex == Human.man_sex) {
                            this.sexWithWoman();
                            partner_now.getPregnant(this);
                        }
                        else { // если действуем от лица женщины
                            partner_now.sexWithWoman();
                            this.getPregnant(partner_now);
                        }
                        this.getOlder();
                        return true;
                    }
            }
//            else { // если партнер чутка подальше
//                return Move(xD,yD);
//            }
        }
        return Move(xD, yD); // или просто на месте стоим, и отнять у нее энергию (мастурбация)
// что делаем? если беременная женщина хочет размножаться
//        if (ifWoman() && getPregnancy() == 0) {
//            for (int yShift = -1; yShift < 2; yShift++) {
//                for (int xShift = -1; xShift < 2; xShift++) {
//                    int yTarget = y + yShift;
//                    int xTarget = x + xShift;
//
//                    if (map.isCellinMapRange(yTarget, xTarget)) {
//                        Cell cellTarget = map.getCell(yTarget, xTarget);
//
//                        if (cellTarget.getHuman() != null) { // если мужчина
//                            // && и еще если не спит
//                            if (!cellTarget.getHuman().ifWoman()) {
//                                    getPregnant(cellTarget.getHuman()); // забеременеть от конкретного мужчины
//                                    getOlder();
//                                    cellTarget.getHuman().sexWithWoman();
//                                    return true;
//                                    //XMainPanel.eventsInfoPanel.update (date, "Woman got pregnant");
//                                                            }
//                        }
//                    }
//                }
//            }
//        }
//        // если невозможно сейчас заделать ребенка: стареем, голодаем, устаем
//        getOlder();
//        setSatiety(getSatiety()-1);
//        setEnergy(getEnergy()-1);
        //return false;
    }

    public void getPregnant(Human father){
        pregnancy = 1;
        satiety -=2;
        energy -=2;
        this.childBrain = new Neural(this.brain, father.brain); // совмещение мозгов
    }
    public void sexWithWoman() {
        satiety -=3;
        energy -=3;
    }

    public int getDays() {
        return this.days;
        // formatDate(human.getDays());
    }
    public void setAge(int Days) {
        this.days = Days;
    }
    public void getOlder() {
        this.days = mapPanel.getValueAtRange(this.days + 1,0, MAX_AGE );
        //this.days += 1;
        if (ifWoman() && pregnancy>0) pregnancy++;
    }
    public int getEnergy() {
        return this.energy;
    }
    public void setEnergy(int en) {
        this.energy = mapPanel.getValueAtRange(en,0,MAX_ENERGY);
    }
    public int getSatiety() {
        return this.satiety;
    }
    public void setSatiety(int sat) {
        this.satiety = mapPanel.getValueAtRange(sat,0,MAX_SATIETY);
    }
    public int getPregnancy() {
        return this.pregnancy;
        // formatDate(human.getDays());
    }
    // Neural Network choosing the ways
    public int getGeneration(){
        return generation;
    }
    public int getPrevDirection() { return direction;}
    public float getTreeDist(){
        return findOutTreeDist(); // тут нужно где-то ее задавать еще ок
    }
    public void setTreeDist(float dist){
        treeDist = dist;
    }
    public Human findOutPartner() {
        int minTarget = 2 * (mapPanel.MAP_SIZE - 1) * (mapPanel.MAP_SIZE - 1); // максимально возможное расстояние
        Human partner_now = null;

        //смотрим всех мужчин
        for (Human person: mapPanel.men) {
            if (person.sex!=this.sex && pregnancy == 0 && person.pregnancy == 0) { // т.е. беременных как партнера не рассматриваем
                int yD = Math.abs(person.y - this.y);
                int xD = Math.abs(person.x - this.x);

                int minTemp = (yD*yD+xD*xD)/ this.getGeneration(); // по сути фитнес функция - означает к-во поколений
                if (minTemp<minTarget) {
                    partner_now = person;
                    minTarget = minTemp;
                }
            }
        }
        if (partner_now == null) partner_now = this; // если не нашли небеременных живых девушек, партнер - сам мужчина (тонко)
            setPartner(partner_now);
        return partner_now;
    }
    public float findOutPartnerDistance() {
        findOutPartner();
        int xD = Math.abs(this.partner.x-this.x);
        int yD = Math.abs(this.partner.y-this.y);
        double minDist = Math.sqrt(xD*xD+yD*yD);

        return (float) minDist;
    }

    public Human getPartner(){
        return findOutPartner(); // тут нужно где-то ее задавать еще ок
    }
    public void setPartner(Human person){
        partner = person;
    }
    public float findOutTreeDist(){
        int minTarget = 2 * (mapPanel.MAP_SIZE - 1) * (mapPanel.MAP_SIZE - 1); // максимально возможное расстояние
        int yTarget = y;
        int xTarget = x;
        //смотрим все клетки с деревьями
        for (int yTemp = 0; yTemp < mapPanel.MAP_SIZE; yTemp++) { // бежим по всем клеткам
            for (int xTemp = 0; xTemp < mapPanel.MAP_SIZE; xTemp++) {
                Cell cellTarget = mapPanel.getCell(yTemp, xTemp);
                if (cellTarget.getPlant() != null) {
                    Plant plantTarget = cellTarget.getPlant();
                    if (plantTarget.getFruits() != 0) { // дерево есть, фрукты есть
                        int yD = Math.abs(yTemp - y);
                        int xD = Math.abs(xTemp - x);

                        int minTemp = yD * yD + xD * xD;

                        if (minTemp < minTarget) {
                            minTarget = minTemp;
                            yTarget = yTemp;
                            xTarget = xTemp;
                        }
                    }

                }
            }
        }
        float yd = Math.abs(y-yTarget);
        float xd = Math.abs(x-xTarget);

        float distance = (float) Math.sqrt( yd*yd + xd*xd ); // гипотенузу нашли
        setTreeDist(distance);
        return distance;
    }

    public float calculateFitnessFunction(Human man){
    return 0.0f;
    }
    public boolean Move(int xD,int yD) { // важно! xD = xTarget - x;
        int yShift=0, xShift=0;

        if (yD < 0 ) { yShift = -1; }
        else if (yD > 0 ) { yShift = 1; }

        if (xD < 0 ) { xShift = -1; }
        else if (xD > 0 ) { xShift = 1; }

        return tryToMove(yShift, xShift); // и дальше -energy, satiety, age+ и т.п.
    }
    public boolean tryToMove(int yshift, int xshift) { // 1 0 -1
        //
//        if (yshift == 0 && xshift == 0 ) {
//            // убрать рандом нафиг!!!!!!!! пусть сдвиг тоже нейросеть определяет
//            // можно сделать отдельную опцию think , на вход приходят данные об окружающих клетках, на выходе сдвиг, и одна из них
//
//            yshift = XRandomGenerator.generateInteger(-1,1); // ns choice
//            xshift = XRandomGenerator.generateInteger(-1,1); // ns choice
//        }
//        if (yshift != 0 || xshift != 0) {
            int yTarget = y +yshift;
            int xTarget = x +xshift;

            if (yshift!= 0 || xshift !=0) {
            if (!XMapPanel.isCellinMapRange(yTarget,xTarget)) {
                XForce.clearHuman(this);

                XStatistic.peopleDied++;
               XStatistic.peopleDiedByLost++;
               // XMainPanel.eventsInfoPanel.update(days,"Human died (lost)");
                return true;
            }
            if (mapPanel.getCell(yTarget,xTarget).getHuman() == null) {
                moveHuman(y,x, yTarget, xTarget);

                if (mapPanel.getCell(yTarget,xTarget).getLandscape() == Cell.LANDSCAPE_TYPE_WATER_LOW ||
                        mapPanel.getCell(yTarget, xTarget).getLandscape() == Cell.LANDSCAPE_TYPE_WATER_HIGH) {
                    this.setEnergy(getEnergy()-3);
                    this.setSatiety(getSatiety()-2);
                }
                else {
                    this.setEnergy(getEnergy()-1);
                    this.setSatiety(getSatiety()-1);
                }
                this.getOlder();
                return true;
            }
            else {
                // проверяем соседние клетки
                int dir_now = getDirection(xshift,yshift);
                dir_now = getAvgDirection(dir_now); // усредняем направление кореллируем с предыдущим
                if ( checkNeighborCell(this,getLeftDirection(dir_now,1)) ) // проверили соседнюю слева, там никого
                        return tryToMove(getYShift(getLeftDirection(dir_now,1)), getXShift(getLeftDirection(dir_now,1)));
                else if (checkNeighborCell(this, getRightDirection(dir_now,1)) )
                        return tryToMove(getYShift(getRightDirection(dir_now,1)), getXShift(getRightDirection(dir_now,1)));
                    else if (checkNeighborCell(this, getLeftDirection(dir_now,2)) )// начинаем проверять не диагональные
                            return tryToMove(getYShift(getLeftDirection(dir_now,2)), getXShift(getLeftDirection(dir_now,2)));
                        else if (checkNeighborCell(this, getRightDirection(dir_now,2)) )
                            return tryToMove(getYShift(getRightDirection(dir_now,2)), getXShift(getRightDirection(dir_now,2)));
                            else if (checkNeighborCell(this, getLeftDirection(dir_now,3)) )// начинаем проверять не диагональные
                                return tryToMove(getYShift(getLeftDirection(dir_now,3)), getXShift(getLeftDirection(dir_now,3)));
                                    else if (checkNeighborCell(this, getRightDirection(dir_now,3)) )
                                        return tryToMove(getYShift(getRightDirection(dir_now,3)), getXShift(getRightDirection(dir_now,3)));
                                        else if (checkNeighborCell(this, getLeftDirection(dir_now,4)) )// начинаем проверять не диагональные
                                            return tryToMove(getYShift(getLeftDirection(dir_now,4)), getXShift(getLeftDirection(dir_now,4)));

                                            else return stay();
//                    if (xshift != 0  && yshift !=0 ) //(движение по диагонали)
//                    {
//                            if (tryToMove(yshift,0)) return true;
//                            else if (tryToMove(0,yshift)) return true;
//                    }
//                    else if ( (xshift == 0) ^ (yshift == 0) ) // движение вбок
//                    {
//                        if (xshift == 0) {
//                            if (tryToMove(yshift, 1)) return true; // пробуем пойти по диагонали но со сдвигом
//                            else if (tryToMove(yshift, -1)) return true;
//                        }
//                        if (yshift == 0) {
//                            if (tryToMove(1, xshift)) return true; // пробуем пойти по диагонали но со сдвигом
//                            else if (tryToMove(-1, xshift)) return true;
//                        }
//                    }
//                    // если и это не помогло и везде люди, делаем шаг назад, смотрим задние клетки
//                    if (tryToMove(-yshift,-xshift)) return true;
            }
         }
        return stay();
    }
    public boolean stay(){
        if (mapPanel.getCell(this.y,this.x).getLandscape() == Cell.LANDSCAPE_TYPE_WATER_LOW ||
                mapPanel.getCell(this.y,this.x).getLandscape() == Cell.LANDSCAPE_TYPE_WATER_HIGH) {
            this.setEnergy(getEnergy()-3);
            this.setSatiety(getSatiety()-2);
        }
        else {
            this.setEnergy(getEnergy()-1);
            this.setSatiety(getSatiety()-1);
        }
        this.getOlder();
        return true;
    }

    public boolean tryToEat() {

        // пробегаемся взглядом рядом = есть фрукты - едим
            for (int yShift = -1; yShift < 2; yShift++) {
                for (int xShift = 0; xShift < 2; xShift++) {
                    int yTarget = y + yShift;
                    int xTarget = x + xShift;
                    if (mapPanel.isCellinMapRange(yTarget,xTarget)) {
                        Cell cellTarget = mapPanel.getCell(yTarget, xTarget);
                        int fruitsTarget = cellTarget.getFruit();
                        if (fruitsTarget != 0) {

                            // нужно установить treeDist
                            setTreeDist(Math.abs(this.x-xTarget)+Math.abs(this.y-yTarget) );

                            cellTarget.eatFruit();
                            setSatiety(getSatiety() + 12);
                            setEnergy(getEnergy() - 1);
                            getOlder();
                            // MainPanel.eventsInfoPanel.update(date,"Eat");
                            return true;
                        }
                    }

                }
            }
            // need to find tree: пробегаемся по деревьям,находим оптимальное расстояние
            int minTarget = 2 * (mapPanel.MAP_SIZE - 1) * (mapPanel.MAP_SIZE - 1); // максимально возможное расстояние
            int yTarget = y;
            int xTarget = x;
            //for (Plant tree: mapPanel.trees) {
            for (int yTemp = 0; yTemp < mapPanel.MAP_SIZE; yTemp++) { // бежим по всем клеткам
                for (int xTemp = 0; xTemp < mapPanel.MAP_SIZE; xTemp++) {
                    Cell cellTarget = mapPanel.getCell(yTemp, xTemp);
                    if (cellTarget.getPlant() != null) {
                        Plant plantTarget = cellTarget.getPlant();
                        if (plantTarget.getFruits() != 0) { // дерево есть, фрукты есть
                            int yD = Math.abs(yTemp - y);
                            int xD = Math.abs(xTemp - x);

                            int minTemp = yD * yD + xD * xD;

                            if (minTemp < minTarget) {
                                minTarget = minTemp;
                                yTarget = yTemp;
                                xTarget = xTemp;
                            }
                        }

                    } else if (cellTarget.getFruit() != 0) { // дерева нет, но фрукты есть
                        int yD = Math.abs(yTemp - y);
                        int xD = Math.abs(xTemp - x);

                        int minTemp = yD * yD + xD * xD;
                        if (minTemp < minTarget) {
                            minTarget = minTemp;
                            yTarget = yTemp;
                            xTarget = xTemp;
                        }
                    }
                }
            }
            // можно было вместо координат запоминать клетку, ну фиг с ним
            // теперь начинаем движение к этой клетке

            if (yTarget != y || xTarget !=x) {
                int yShift = 0, xShift = 0;
                int yD = yTarget - y, xD = xTarget - x;
                setTreeDist( (float) Math.sqrt( yD*yD+xD*xD) ); // здесь сохраняем дистанцию до ближайшего дерева

                if (yD < 0 ) { yShift = -1; }
                else if (yD > 0 ) { yShift = 1; }

                if (xD < 0 ) { xShift = -1; }
                else if (xD > 0 ) { xShift = 1; }

                return tryToMove(yShift, xShift); // и дальше -energy, satiety, age+ и т.п.
            }
            else setTreeDist(0.0f);

            return false;
    }

    public String transformSex() {
        String text;
        if (sex == woman_sex) text=" Woman ";
        else text = " Man ";
        if (this == null) text =" No sex ";
        //else text = "UNDEFINED";
        return text;
    }
    public int getSexInput() {
        if (this.ifWoman()) return 1;
        else return 0;
    }

    private void moveHuman (int yFrom, int xFrom, int yTo, int xTo) {
        mapPanel.getCell(yFrom,xFrom).setHuman(null); // заменили ссылку - теперь пустая клетка)
        this.x = xTo;
        this.y = yTo;
        this.direction = getDirection(xTo-xFrom, yTo-yFrom);
        mapPanel.getCell(yTo, xTo).setHuman(this);

        //XMainPanel.eventsInfoPanel.update(days, yFrom + "," + xFrom +">" + yTo + "," + xTo +": MOVE");

    }
    public boolean tryToSleep() {
           setEnergy(getEnergy()+5);
            setSatiety(getSatiety()-1);
            getOlder();
        return true;
    }
    public boolean tryToDie () {
        if (getEnergy() == 0 ) {
            //mapPanel.getCell(this.y, this.x).setHuman(null);
            XForce.clearHuman(this);
            XStatistic.peopleDied++;
            XStatistic.peopleDiedByEnergy++;
            //events.update(date, "Human died by [Low Energy]");
            return true;
        }
        if (getSatiety() == 0) {
            XForce.clearHuman(this); // тут может баг возникнуть. т.к. экземпляр уже удалили wtf
            XStatistic.peopleDied++;
            XStatistic.peopleDiedBySatiety++;
            //events.update(date, "Human died by [Low Satiety]");
            return true;
        }
        // p = kx+b;  k = (MAX_DAYS-MONTH_PERIOD); b = MONTH_PERIOD
        // Every year + 2%
//        if (this.days > START_AGE) {
//            float k = 1.0f / (MAX_AGE - START_AGE);
//            float p = 0.01f* k * days;
//            boolean decision = XRandomGenerator.generateBoolean(Math.round(p * 100)); // линейная вероятность смерти
//            // boolean decision = XRandomGenerator.generateBoolean(2* days/ 300 - 50); // экспоненциальная вероятность смерти
//
//            if (decision) {
//                XForce.clearHuman(this);
//                XStatistic.peopleDied++;
//                XStatistic.peopleDiedByAge++;
//                return true;
//            }
//        }
        return false;
    }

    public int getDirection(int xshift, int yshift) { // дает направление основываясь на сдвиге
        int direction = Cell.DIR_STAY;
        int controlSum = xshift*10+yshift;

        switch (controlSum) {
            case (-11): { // -1, -1: -10 - 1 = -11
                direction = Cell.DIR_NORTH_WEST;
                break;
            }
            case (-10): { // -1, 0: -10+0 = -10
                direction = Cell.DIR_WEST;
                break;
            }
            case (-9): { // -1, 1: -10+1 = -9
                direction = Cell.DIR_SOUTH_WEST;
                break;
            }
            case (1): { // 0, 1: 0+1 = 1
                direction = Cell.DIR_SOUTH;
                break;
            }
            case (11): { // 1, 1: 10+1 = 11
                direction = Cell.DIR_SOUTH_EAST;
                break;
            }
            case (10): { // 1, 0: 10+0 = 10
                direction = Cell.DIR_EAST;
                break;
            }
            case (9): { // 1, -1: 10-1 = 9
                direction = Cell.DIR_NORTH_EAST;
                break;
            }
            case (-1): { // 0, -1: -1 = -1
                direction = Cell.DIR_NORTH;
                break;
            }
            default: {  // stay = do nothing
                break;
            }
        }
        return direction;
    }

    public boolean checkNeighborCell(Human man, int dir){ // проверяет клетку по направлению dir от позиции человека
        boolean ifEmpty = false;
        // надо проверить в диапазоне ли клетка
        int yTarget =man.y+getYShift(dir);
        int xTarget =man.x+getXShift(dir);

        if (XMapPanel.isCellinMapRange(yTarget,xTarget))
            if (mapPanel.getCell(man.y+getYShift(dir), man.x+getXShift(dir)).getHuman() == null) ifEmpty = true;
//        switch (dir) {
//            case Cell.DIR_NORTH: {
//                if (mapPanel.getCell(man.y-1  , man.x).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_NORTH_EAST: {
//                if (mapPanel.getCell(man.y-1  , man.x+1).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_EAST: {
//                if (mapPanel.getCell(man.y  , man.x+1).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_SOUTH_EAST: {
//                if (mapPanel.getCell(man.y+1  , man.x+1).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_SOUTH: {
//                if (mapPanel.getCell(man.y+1  , man.x).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_SOUTH_WEST: {
//                if (mapPanel.getCell(man.y+1  , man.x-1).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_WEST: {
//                if (mapPanel.getCell(man.y  , man.x-1).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            case Cell.DIR_NORTH_WEST: {
//                if (mapPanel.getCell(man.y-1  , man.x-1).getHuman() == null) ifEmpty = true;
//                break;
//            }
//            default: break;
//        }
        return ifEmpty;
    }
    public int getLeftDirection (int dir, int how_far) { // отсчитывая против часовой на how_far шагов, получим нужное направление
        int d = dir;
        if (d!= 0)
            while (how_far > 0) {// будем уменьшать шаг
                if (d == Cell.DIR_NORTH) d = Cell.DIR_NORTH_WEST;
                else d--;

                how_far--;
            }
            return d;
    }
    public int getRightDirection (int dir, int how_far) {// отсчитывая по часовой на how_far шагов, получим нужное направление
        int d = dir;
        if (d!= 0)
            while (how_far > 0) {
                if (d == Cell.DIR_NORTH_WEST) d = Cell.DIR_NORTH;
                else d++;
                how_far--;
            }
        return d;
    }
    public int getAvgDirection (int dir) {
        int old_dir = this.getPrevDirection();
        int difference = Math.abs(dir - old_dir);
        switch (difference) {
            case 1: return dir;
            case 2: { if (old_dir > dir) return getRightDirection(dir,1);
                        else return getLeftDirection(dir,1);
                    }
            case 3: { if (old_dir > dir) return getRightDirection(dir,1);
                     else return getLeftDirection(dir,1);
            }
            case 4: {}
            case 5: { if (old_dir > dir) return getLeftDirection(dir,1);
            else return getRightDirection(dir,1);
            }
            case 6: {if (old_dir > dir) return getLeftDirection(dir,1);
            else return getRightDirection(dir,1);
            }
            case 7: return dir;
            default: return dir;
        }
    }
    public int getXShift(int dir) {
        int xs=0;
        switch (dir) {
            case Cell.DIR_NORTH_EAST: {
                xs = 1;
                break;
            }
            case Cell.DIR_EAST: {
                xs = 1;
                break;
            }
            case Cell.DIR_SOUTH_EAST: {
                xs = 1;
                break;
            }
            case Cell.DIR_SOUTH_WEST: {
                xs = -1;
                break;
            }
            case Cell.DIR_WEST: {
                xs = -1;
                break;
            }
            case Cell.DIR_NORTH_WEST: {
                xs = -1;
                break;
            }
//            default: {
//                xs = 0;
//                break;
//            }
        }
        return xs;
    }
    public int getYShift(int dir) {
        int ys=0;
        switch (dir) {
            case Cell.DIR_NORTH: {
                ys = -1;
                break;
            }
            case Cell.DIR_NORTH_EAST: {
                ys = -1;
                break;
            }
            case Cell.DIR_SOUTH_EAST: {
                ys = 1;
                break;
            }
            case Cell.DIR_SOUTH: {
                ys = 1;
                break;
            }
            case Cell.DIR_SOUTH_WEST: {
                ys = 1;
                break;
            }
            case Cell.DIR_NORTH_WEST: {
                ys = -1;
                break;
            }
//            default: {
//                ys = 0;
//                break;
//            }
        }
        return ys;
    }

}
