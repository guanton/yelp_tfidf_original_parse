import com.google.common.collect.MinMaxPriorityQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchEngine {
    private JFrame f;
    private JButton searchButton;
    private JPanel panelMain;
    private JTextField textField;
    private JPanel pan;
    private YelpAnalysis yp;
    private JLabel[] outputs;
    private String query;
    private searchPanel sp;


    public SearchEngine() {
        //search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yp = new YelpAnalysis();
                query = textField.getText();
                yp.init(false);
                yp.txtToString(query);
                yp.secondPass(query);
                sp = new searchPanel(yp.getBusinesses());
                displayResults();
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query = textField.getText();
            }
        });
    }

    public void display(){
        f = new JFrame("Yelp Search");
        f.setContentPane(new SearchEngine().pan);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private class searchPanel extends javax.swing.JPanel {
        private searchPanel(MinMaxPriorityQueue businesses) {
            //initialize empty board with extra row for buttons
            this.setLayout(new GridLayout(10,2));

            //make row with only Play button (or no play button if there is no human)
            for (int i = 1; i <= 10; i++) {
                Business b = yp.getBusinesses().removeFirst();
                JLabel label = new JLabel();
                String text = i + ". " + b.businessName + "\n" + b.businessAddress + " ";
                label.setText("<html>" + text.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
                this.add(label);

            }

        }


    }

    public void displayResults(){
        f = new JFrame("Results for " + query);
        f.setContentPane(sp);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SearchEngine s = new SearchEngine();
        s.display();
    }
}
