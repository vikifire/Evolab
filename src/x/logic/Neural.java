package x.logic;

import x.logic.random.XRandomGenerator;
import x.logic.statistic.XStatistic;
import x.qui.format.XFormatter;
import x.qui.main.XMainPanel;

import java.util.ArrayList;
import java.util.List;

import static x.logic.Human.man_sex;
import static x.logic.Human.woman_sex;
import static x.logic.random.XRandomGenerator.generateDeviation;
import static x.qui.main.XMainPanel.mapPanel;

/**
 * Created by орда on 20.04.2017.
 */
public class Neural {
    static int num_layers;
    public static final int input = 13; // pregnancy, men/people, ageMean, PeopleDensity, getFruitsRatio, satiety, age, energy, treeDistance, prevDirection
    public static final int output = 3; // GiveBirth , MakeChild, Move, Eat, Sleep// Die // probably KILL>??? // die function later
    public static final float start_zero_weight = 0.1f;
    public static final float start_weight = 0.5f;

    public static final int I_PREGNANCY = 0;
    public static final int I_PEOPLE_RATIO = 1;
    public static final int I_AGE_MEAN = 2;
    public static final int I_PEOPLE_DENSITY = 3;
    public static final int I_FRUITS_RATIO = 4;
    public static final int I_SATIETY = 5;
    public static final int I_AGE = 6;
    public static final int I_ENERGY = 7;
    public static final int I_TREE_DISTANCE = 8;
    public static final int I_SEX = 9;
    public static final int I_PARTNER_DISTANCE = 10;
    public static final int I_PREV_DIRECTION = 11;
    public static final int I_CHILDREN_DIED = 12;
    
    public static final int O_MAKE_CHILD = 0;
      public static final int O_EAT = 1;
    public static final int O_SLEEP = 2;

    //public static final int O_GIVE_BIRTH = 0;
    //public static final int O_MOVE = 1;
    //    public static final int O_DIE = 4;

    ArrayList<float[][]> layerList; // список слоев сети (сначала 1)

      public Neural(int layers){
        num_layers = layers; // сначала 1 делаем
        layerList = new ArrayList <float[][]>();

        for (int i = 0; i < num_layers; i++) {
            float[][] layer_now = new float [output][input]; // новый слой
//            for (int j = 0; j < output; j++) {
//                for (int k = 0; k < input; k++) {
//                    //layer_now[j][k] = start_zero_weight+ generateDeviation(start_zero_weight, 0.5f); // стандартные - 0.1 веса для нулевых связей +- E погрешность
//
//                    // т.е. от начального "нулевого" веса сейчас отклоняемся неболее чем на 50%
//                }
//            }
        // нужно заполнить ненулевые связи начальными весами (большими)
            layer_now = setWeights(i,layer_now);
            layerList.add(layer_now);
        }
    }
        public Neural (Neural brain1, Neural brain2) { // mother_brain (1)  , father_brain(2)
          num_layers = brain1.layerList.size(); // может здесь size()-1
          layerList = new ArrayList<float[][]>();

          boolean gen_choice = XRandomGenerator.generateBoolean();

            for (int i = 0; i < num_layers; i++) {
                float[][] layer_now = new float [output][input];
                for (int j = 0; j < output; j++) {
                    for (int k = 0; k < input; k++) {

                // AVERAGE NEUROWEIGHTS - усредняющее скрещивание
                        //layer_now[j][k] = (float) ( brain1.layerList.get(i)[j][k] + brain2.layerList.get(i)[j][k] ) / 2.0f; // ПРАВИЛЬНО ЗДЕСЬ

                        // TRUE CROSSOVER - усредняющее с отклонением (доминантность)
                        if (gen_choice == woman_sex) layer_now[j][k] = 0.8f * brain1.layerList.get(i)[j][k] + 0.2f * brain2.layerList.get(i)[j][k];
                        else if (gen_choice == man_sex) layer_now[j][k] = 0.2f * brain1.layerList.get(i)[j][k] + 0.8f * brain2.layerList.get(i)[j][k];

                // MUTATIONS - с мутациями
                        //layer_now[j][k] = Math.max(Math.min(
                        //        layer_now[j][k]+XRandomGenerator.generateDeviation(0.1f),1),0); // мутации ( 20%)

                // CROSSOVER - поочередное
                        //int neuron_num = (j*output+k) % 2;
//                        if (  neuron_num == 0) layer_now[j][k] =  brain1.layerList.get(i)[j][k];
//                        else if (neuron_num == 1) layer_now[j][k] = brain2.layerList.get(i)[j][k];


                    }
                }

                layerList.add(layer_now);
            }
        }
    
    public float[][] setWeights (int num_layer, float[][] layer){
//            float dev_now = 0.2f;
//
//          switch (num_layer) {
//              case 1: {
//                    // 1 - giveBirth neuron
//                    layer[O_GIVE_BIRTH][I_PREGNANCY]=start_weight+ generateDeviation(start_weight,dev_now); // pregnancy
//                    layer[O_GIVE_BIRTH][I_PEOPLE_RATIO]=start_weight+ generateDeviation(start_weight,dev_now); // men/people
//                  // 2 - MakeChild
//                    layer[O_MAKE_CHILD][I_AGE_MEAN]=start_weight+ generateDeviation(start_weight,dev_now); // ageMean
//                    layer[O_MAKE_CHILD][I_PEOPLE_DENSITY]=start_weight+ generateDeviation(start_weight,dev_now);; // peopleDensity
//                    layer[O_MAKE_CHILD][I_FRUITS_RATIO]=start_weight+ generateDeviation(start_weight,dev_now);; // fruitsRatio
//                    layer[O_MAKE_CHILD][I_AGE]=start_weight+ generateDeviation(start_weight,dev_now);; // age
//
//                  // 3 - Move
//                  layer[O_MOVE][I_PREGNANCY]=start_weight+ generateDeviation(start_weight,dev_now);; // pregnancy
//                  layer[O_MOVE][I_PEOPLE_DENSITY]=start_weight+ generateDeviation(start_weight,dev_now);; // peopledensity
//                  layer[O_MOVE][I_SATIETY]=start_weight+ generateDeviation(start_weight,dev_now);; // satiety
//                  layer[O_MOVE][I_ENERGY]=start_weight+ generateDeviation(start_weight,dev_now);; // energy
//
//                  // 4 - Eat
//                  layer[O_EAT][I_FRUITS_RATIO]=start_weight+ generateDeviation(start_weight,dev_now);; // fruitsRatio
//                  layer[O_EAT][I_SATIETY]=start_weight+ generateDeviation(start_weight,dev_now);; // Satiety
//                  layer[O_EAT][I_TREE_DISTANCE]=start_weight+ generateDeviation(start_weight,dev_now);; // TreeDistance
//
//                  // 5 - Sleep
//                  layer[O_SLEEP][I_SATIETY]=start_weight+ generateDeviation(start_weight,dev_now);; // Satiety
//                  layer[O_SLEEP][I_AGE]=start_weight+ generateDeviation(start_weight,dev_now);; // Age
//                  layer[O_SLEEP][I_ENERGY]=start_weight+ generateDeviation(start_weight,dev_now);; // Energy
//                  layer[O_SLEEP][I_TREE_DISTANCE]=start_weight+ generateDeviation(start_weight,dev_now);; // TreeDistance
//
//                  // 6 - Die
//                  layer[O_DIE][I_SATIETY]=start_weight+ generateDeviation(start_weight,dev_now);; // satiety
//                  layer[O_DIE][I_AGE]=start_weight+ generateDeviation(start_weight,dev_now);; // Age
//                  layer[O_DIE][I_ENERGY]=start_weight+ generateDeviation(start_weight,dev_now);; // energy
//
//                  break;
//              }
//                // case 2, 4:  нечетные 1, 3, 5 первого нейрона , и 2,4,6... 2го нейрона
//              // case 3:  четные 2, 4, 6 первого нейрона , и 1,3,5... 2го нейрона
//
//              default: {
//                  break;
//              }
//          }
        for (int i = 0; i < output; i++) {
            for (int j = 0; j < input; j++) {
                layer[i][j] = (float) XRandomGenerator.generateInteger(1,100) / 100.0f;
            }
        }
          return layer;
    } // тут нужно доделать

    public float[] getInputs(Human man){ // возвращает входные значения нейросети
        XStatistic stat_now = mapPanel.stat;
        stat_now.update();

        float[] s = new float[input];

        s[I_PREGNANCY]=(float) man.getPregnancy() / (float) Human.PREGNANCY_PERIOD;
        s[I_PEOPLE_RATIO]= stat_now.getPeopleRatio();
        s[I_AGE_MEAN]=stat_now.getPeopleAgeMean()/Human.MAX_AGE ;
        s[I_PEOPLE_DENSITY]=stat_now.getPeopleLandDensity();
        s[I_FRUITS_RATIO]=stat_now.getPlantsFruitsPeopleRatio();
        s[I_SATIETY]=(float) man.getSatiety()/ (float) Human.MAX_SATIETY;
        s[I_AGE]= (float) man.getDays()/ (float) Human.MAX_AGE;
        s[I_ENERGY]= (float) man.getEnergy()/ (float) Human.MAX_ENERGY;
        s[I_SEX] = (float) man.getSexInput();
        float maxDistance = mapPanel.MAP_SIZE*mapPanel.MAP_SIZE; // максимально возможное расстояние
        s[I_TREE_DISTANCE]= man.getTreeDist() / maxDistance; // так, здесь нужно в tryToEat записывать в параметр
        s[I_PARTNER_DISTANCE] = man.findOutPartnerDistance() / maxDistance;
        s[I_PREV_DIRECTION] = man.getPrevDirection() / Cell.DIR_NORTH_WEST;
        if (stat_now.ChildrenBorn == 0) s[I_CHILDREN_DIED] = 0;
        else s[I_CHILDREN_DIED] = stat_now.ChildrenDied/stat_now.ChildrenBorn;

        return s;
    }

    public int think( Human man){        // возвращает вектор с вероятностями действий (выходные значения)
        float[] inputs = getInputs(man);
        float[] outputs = new float[output]; // выходной массив
        for (int i = 0; i < output; i++) {
            outputs[i]=0;
        } // пока вероятности равны нулю

        // умножение матриц (вызывается метод из XForce.act(man)
        // сначала для одного слоя
        for (float[][] layer: layerList) {
            for (int i = 0; i < output; i++) { // пробегаемся по матрице  слева направо (одна строка - один нейрон
                for (int j = 0; j < input; j++) {
                    outputs[i]+=inputs[j]*layer[i][j];
                }
            }
        }

        return getFinalChoice(outputs);
        //return outputs;
    }
    public int getFinalChoice(float[] options){ // функция активации
        //System.out.println(options[0]+" "+ options[1]+" "+options[2]+" " + options[3] + " " + options[4]);
// выбираем наибольшую вероятность ??

        int choice = 0;

        for (int i = 1; i < output; i++) { // ищем максимальную вероятность
            if ( options[i] >= options[choice] )
                    choice = i;
        }
        return choice;
    }
}
