package gburkl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Georg Burkl
 * @version 2019-10-03
 */
public class CSVViewer extends JFrame {
    private final JButton next;
    private final JButton prev;
    private final List<JLabel> attributes;
    private final List<JLabel> values;
    private CSVReader model;
    private int index;

    /**
     * the main method for this program
     * @param args the run args
     */
    public static void main(String[] args) {
        new CSVViewer();
    }

    /**
     * Generates the Window for viewing the files content
     */
    private CSVViewer() {
        this.model = new CSVReader();

        this.attributes = new ArrayList<>();
        this.values = new ArrayList<>();

        this.setLayout(new BorderLayout());

        this.next = new JButton(Constants.NEXT);
        this.prev = new JButton(Constants.PREV);

        this.next.addActionListener(this::next);
        this.prev.addActionListener(this::prev);

        this.add(this.prev, BorderLayout.WEST);
        this.add(this.next, BorderLayout.EAST);

        Container container = new Container();
        container.setLayout(new GridLayout(0, 2));

        for (int i = 0; i < this.model.getHeader().length; i++) {
            JLabel aLabel = new JLabel("");
            JLabel vLabel = new JLabel("");
            container.add(aLabel);
            container.add(vLabel);
            this.attributes.add(aLabel);
            this.values.add(vLabel);
        }
        this.add(container, BorderLayout.CENTER);
        this.setAttributes(this.model.getHeader());
        this.setValues(this.model.get(this.index));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Sets the text of the attributes
     * @param attributes the attributes to set
     */
    public void setAttributes(String[] attributes) {
        for (int i = 0; i < attributes.length; i++) {
            this.attributes.get(i).setText(attributes[i]);
        }
    }

    /**
     * Sets the text of the values
     * @param values the values to set
     */
    public void setValues(String[] values) {
        for (int i = 0; i < values.length; i++) {
            this.values.get(i).setText(values[i]);
        }
    }

    /**
     * Handles the press of the next Button
     * @param actionEvent the event
     */
    public void next(ActionEvent actionEvent) {
        this.index++;
        this.prev.setEnabled(true);
        String[] values = this.model.get(this.index);
        if (values.length == 0) {
            if (JOptionPane.showConfirmDialog(this, Constants.END_OF_FILE) == JOptionPane.OK_OPTION) {
                this.index = 0;
                values = this.model.get(this.index);
            } else {
                this.index--;
                return;
            }
        }
        this.setValues(values);
    }

    /**
     * Handles the press of the prev Button
     * @param actionEvent the event
     */
    public void prev(ActionEvent actionEvent) {
        this.index--;
        if (this.index < 0) {
            if (JOptionPane.showConfirmDialog(this, Constants.BEGIN_OF_FILE) == JOptionPane.OK_OPTION) {
                this.index = this.model.size();
            } else {
                this.index++;
                return;
            }
        }
        this.setValues(this.model.get(this.index));
    }
}
