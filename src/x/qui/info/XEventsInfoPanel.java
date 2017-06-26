package x.qui.info;

import x.qui.format.XFormatter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by орда on 30.03.2017.
 */
public class XEventsInfoPanel extends JScrollPane {
    private JList<String> events = new JList<String> (new DefaultListModel<String> ());

    public void XEventsInfoPanel () {
        setupView();
        setupEvents();
        setVisible(true);
        this.update(1," TEST");
    }
    public void update(int days, String event) {
        ((DefaultListModel<String>)this.events.getModel()).addElement(formatEvent(days,event));
        int lastIndex = events.getModel().getSize()-1;
        if (lastIndex >= 0 ) {
            events.ensureIndexIsVisible(lastIndex);
        }
    }

    public String formatEvent ( int days, String event) {
        return "[" + XFormatter.formatDateShort(days) + "] "+event;
    }
    public void reset() {
        ((DefaultListModel<String>)this.events.getModel()).clear();
    }

    private void setupView() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Events info"));
        setPreferredSize(new Dimension(200,0));
    }
    private void setupEvents() {
        this.events.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.events.setLayoutOrientation(JList.VERTICAL);
        //this.events.setVisibleRowCount(-1);
        this.events.setBackground(new Color(0xEEEEEE));
        setViewportView(this.events); // привязка скролла и events
    }
}
