package com.fifthgen.trafficsim.gui.controlpanels;

import com.fifthgen.trafficsim.localization.Messages;
import com.fifthgen.trafficsim.map.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public final class LevelOfServiceControlPanel extends JPanel implements ActionListener, ChangeListener, ItemListener {
    public final static Color LOS_A_COLOR = Color.green;
    public final static Color LOS_B_COLOR = new Color(0, 192, 15);
    public final static Color LOS_C_COLOR = Color.yellow;
    public final static Color LOS_D_COLOR = Color.orange;
    public final static Color LOS_E_COLOR = new Color(255, 157, 12);
    public final static Color LOS_F_COLOR = Color.red;
    public final static Color DEFAULT_COLOR = LOS_A_COLOR;

    public static JLabel AVERAGE_SPEED;
    public static JLabel GREEN_PHASE;
    public static JLabel RED_PHASE;
    public static JLabel YELLOW_PHASE;

    public static JLabel TOTAL_VEHICLES;
    public static JTable VEHICLES;

    private JRadioButton addItem_;

    public static JLabel LOS_A_COUNT;
    public static JLabel LOS_B_COUNT;
    public static JLabel LOS_C_COUNT;
    public static JLabel LOS_D_COUNT;
    public static JLabel LOS_E_COUNT;
    public static JLabel LOS_F_COUNT;

    class Rectangle extends JComponent {
        private Color color;

        public Rectangle(Color color) {
            this.color = color;
            this.setPreferredSize(new Dimension(32, 32));
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillRect(164, 0, 32, 32);
        }
    }

    public LevelOfServiceControlPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5, 5, 5, 5);

        Rectangle r = new Rectangle(LOS_A_COLOR);
        c.weightx = 0.1;
        add(r, c);

        JLabel jLabel1 = new JLabel("<html><b>" + Messages.getString("LevelOfServiceControlPanel.losA") + "</b></html>", SwingConstants.LEFT);
        c.weightx = 0.9;
        c.gridx = 1;
        add(jLabel1, c);
        c.weightx = 1;
        LOS_A_COUNT = new JLabel("0");
        c.gridx += 1;
        add(LOS_A_COUNT, c);
        c.gridx -= 1;

        r = new Rectangle(LOS_B_COLOR);
        c.gridx = 0;
        ++c.gridy;
        add(r, c);
        jLabel1 = new JLabel("<html><b>" + Messages.getString("LevelOfServiceControlPanel.losB") + "</b></html>", SwingConstants.LEFT);
        c.gridx += 1;
        c.weightx = GridBagConstraints.REMAINDER;
        add(jLabel1, c);
        LOS_B_COUNT = new JLabel("0");
        c.gridx += 1;
        add(LOS_B_COUNT, c);
        c.gridx -= 1;

        r = new Rectangle(LOS_C_COLOR);
        c.gridx -= 1;
        ++c.gridy;
        add(r, c);
        jLabel1 = new JLabel("<html><b>" + Messages.getString("LevelOfServiceControlPanel.losC") + "</b></html>");
        c.gridx += 1;
        add(jLabel1, c);
        LOS_C_COUNT = new JLabel("0");
        c.gridx += 1;
        add(LOS_C_COUNT, c);
        c.gridx -= 1;

        r = new Rectangle(LOS_D_COLOR);
        c.gridx -= 1;
        ++c.gridy;
        add(r, c);
        jLabel1 = new JLabel("<html><b>" + Messages.getString("LevelOfServiceControlPanel.losD") + "</b></html>");
        c.gridx += 1;
        add(jLabel1, c);
        LOS_D_COUNT = new JLabel("0");
        c.gridx += 1;
        add(LOS_D_COUNT, c);
        c.gridx -= 1;

        r = new Rectangle(LOS_E_COLOR);
        c.gridx -= 1;
        ++c.gridy;
        add(r, c);

        jLabel1 = new JLabel("<html><b>" + Messages.getString("LevelOfServiceControlPanel.losE") + "</b></html>");
        c.gridx += 1;
        add(jLabel1, c);
        c.weightx = 1;
        LOS_E_COUNT = new JLabel("5");
        c.gridx += 1;
        add(LOS_E_COUNT, c);
        c.gridx -= 1;

        r = new Rectangle(LOS_F_COLOR);
        c.gridx -= 1;
        ++c.gridy;
        add(r, c);
        jLabel1 = new JLabel("<html><b>" + Messages.getString("LevelOfServiceControlPanel.losF") + "</b></html>");
        c.gridx += 1;
        add(jLabel1, c);
        c.weightx = 1;
        LOS_F_COUNT = new JLabel("5");
        c.gridx += 1;
        add(LOS_F_COUNT, c);
        c.gridx -= 1;

        jLabel1 = new JLabel("Average speed of all vehicles");
        ++c.gridy;
        --c.gridx;
        add(jLabel1, c);

        AVERAGE_SPEED = new JLabel("<html><b>" + 0 + "km/h</b></html>");
        ++c.gridx;
        add(AVERAGE_SPEED, c);

        jLabel1 = new JLabel("Vehicles");
        ++c.gridy;
        --c.gridx;
        add(jLabel1, c);

        TOTAL_VEHICLES = new JLabel("<html><b>" + 0 + "</b></html>");
        ++c.gridx;
        add(TOTAL_VEHICLES, c);

//       setLayout(new FlowLayout());
//       String[] columnNames = {"Street ID", "Total Vehicles"};
//       Object[][] data = {};
//
//       VEHICLES = new JTable(data, columnNames);
//       VEHICLES.setPreferredScrollableViewportSize(new Dimension(20, 5));
//       VEHICLES.setFillsViewportHeight(true);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
    }
}