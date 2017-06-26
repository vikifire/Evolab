package x.qui;

import x.logic.force.XForce;
import x.qui.main.XMainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.String.*;

import static x.qui.main.XMainPanel.mapPanel;

/**
 * Created by орда on 30.03.2017.
 */
public class XControlPanel extends JPanel {

    private JButton startButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JButton lowerButton;
    private JButton fasterButton;
    private JButton saveButton;


    public static final int MANBUTTON_PEOPLE = 2;


    public XControlPanel() {
        setupView();
        setupButtons();
        setVisible(true);
    }
    private void setupView() {
        setLayout(new FlowLayout(FlowLayout.LEFT)); // выстраиваем все по левому с учетом переноса
    }

    private void setupButtons() {
        // start
        this.startButton = new JButton();
        startButton.setIcon(new ImageIcon("src/resources/gui/icons/play.jpg"));
        startButton.setPreferredSize(new Dimension(25,25));
        startButton.setEnabled(true);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XMapPanel map_now = mapPanel;
                XForce force_now = mapPanel.force;
                force_now.start();
                startButton.setEnabled(false);
                pauseButton.setEnabled(true);
                stopButton.setEnabled(false);
                lowerButton.setEnabled(true);
                fasterButton.setEnabled(true);
                saveButton.setEnabled(false);
            }
        });
        add(startButton);

        // PAUSE
        this.pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon("src/resources/gui/icons/pause.jpg"));
        pauseButton.setPreferredSize(new Dimension(25,25));
        pauseButton.setEnabled(false);

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XForce.pause();
                startButton.setEnabled(true);
                pauseButton.setEnabled(false);
                stopButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        });
        add(pauseButton);

        // STOP - People - +2 new people : man & woman
        this.stopButton = new JButton();
        stopButton.setIcon(new ImageIcon("src/resources/gui/icons/stop.jpg"));
        stopButton.setPreferredSize(new Dimension(25,25));
        stopButton.setEnabled(false);

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //XForce.pause();
                mapPanel.setupNewHumans();

                startButton.setEnabled(true );
                pauseButton.setEnabled(false);
                stopButton.setEnabled(true);

            }
        });
        add(stopButton);


// LOWER
        this.lowerButton = new JButton();
        lowerButton.setIcon(new ImageIcon("src/resources/gui/icons/lower.png"));
        lowerButton.setPreferredSize(new Dimension(25,25));
        lowerButton.setEnabled(false);

        lowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (XForce.getDelay() < 2000) XForce.changeDelay(50);
                else fasterButton.setEnabled(false);
            }
        });
        add(lowerButton);

        // FASTER
        this.fasterButton = new JButton();
        fasterButton.setIcon(new ImageIcon("src/resources/gui/icons/faster.png"));
        fasterButton.setPreferredSize(new Dimension(25,25));
        fasterButton.setEnabled(false);

        fasterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (XForce.getDelay() >= 50) XForce.changeDelay(-50);
                else lowerButton.setEnabled(false);
            }
        });
        add(fasterButton);

        // SAVE BUTTON
        this.saveButton = new JButton();
        saveButton.setIcon(new ImageIcon("src/resources/gui/icons/save1.jpg"));
        saveButton.setPreferredSize(new Dimension(25,25));
        saveButton.setEnabled(false);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //XForce.pause();
//                try {
//                    String pathname2 = "src/resources/data/save-brain.dat";
//                    FileOutputStream stream2 = new FileOutputStream(pathname2);
//
//                    StringBuilder sb = new StringBuilder();
//                    String oldFile = stream2.read(pathname2);
//                    sb.append(oldFile);
//                    //sb.append();
//                    stream2.write(nameFile, sb.toString());
//
//
//                    }
//                catch (IOException ex) {
//                    ex.printStackTrace();
//                }

            }
        });
        add(saveButton);
    }
}
