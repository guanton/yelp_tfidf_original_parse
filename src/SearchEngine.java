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
    private boolean noResult;


    public SearchEngine() {
        //search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOperation();
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOperation();
            }
        });
    }

    public void searchOperation() {
        double time1 = System.currentTimeMillis();
        yp = new YelpAnalysis();
        query = textField.getText();
        yp.init(false);
        yp.txtToString(query);
//        double time2 = System.currentTimeMillis();
//        System.out.println(time2-time1);
        try {
            yp.secondPass(query);
            double time3 = System.currentTimeMillis();
//            System.out.println(time3-time2);
        } catch (NullPointerException err) {
            noResult = true;
            JOptionPane.showMessageDialog(f, "No businesses found with the keywords: " + "\"" + query + "\"");
        }
        sp = new searchPanel(yp.getBusinesses());
        if (!noResult) {
            displayResults();
        }
//        System.out.println(System.currentTimeMillis()-time1);

    }

    public void display() {
        f = new JFrame("Yelp Search");
        f.setContentPane(new SearchEngine().pan);
        f.setSize(800, 600);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }


    private class searchPanel extends javax.swing.JPanel {
        private searchPanel(MinMaxPriorityQueue businesses) {
            this.setLayout(new GridLayout(10, 2));
            for (int i = 1; i <= 10; i++) {
                if (yp.getBusinesses().size() != 0) {
                    Business b = yp.getBusinesses().removeFirst();
                    JLabel label = new JLabel();
                    String text = i + ". " + b.businessName + "\n" + b.businessAddress + " ";
                    label.setText("<html>" + text.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
                    this.add(label);
                    noResult = false;
                }
            }

        }


    }

    public void displayResults() {

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
