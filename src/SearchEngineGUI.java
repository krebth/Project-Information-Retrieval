import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchEngineGUI implements ActionListener {
    private JTextField searchField1;
    private JTextField searchField2;
    private JButton resultButton;
    private JTable resultTable;
    private JButton historyButton;
    private JButton nextPageButton;
    private JButton prevPageButton;
    private JButton yearButton;
    private JButton numButton;
    private ArrayList<String> searchHistory;
    private ArrayList<ArrayList<String>> allResults;
    private int currentPage;
    private int totalPages;
    private int totalResults;
    private OpenCsv csv;

    public static void main(String args[]){
        SearchEngineGUI gui = new SearchEngineGUI();
        gui.createGUI();
    }

    public void createGUI(){
        JFrame frame = new JFrame("Search Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550,550);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel searchPanel1 = new JPanel();
        JLabel label1 = new JLabel("Add some fields:");
        searchField1 = new JTextField(20);
        searchPanel1.add(label1);
        searchPanel1.add(searchField1);

        JPanel searchPanel2 = new JPanel();
        JLabel label2 = new JLabel("Add some values:");
        searchField2 = new JTextField(20);
        searchPanel2.add(label2);
        searchPanel2.add(searchField2);

        JPanel buttonPanel = new JPanel();
        resultButton = new JButton("Search");
        resultButton.addActionListener(this);
        buttonPanel.add(resultButton);

        historyButton = new JButton("History");
        historyButton.addActionListener(this);
        buttonPanel.add(historyButton);
        
        yearButton = new JButton("Sort by year");
        yearButton.addActionListener(this);
        buttonPanel.add(yearButton);
        
        numButton = new JButton("Sort by number");
        numButton.addActionListener(this);
        buttonPanel.add(numButton);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        JLabel resultLabel = new JLabel("Results:");
        resultTable = new JTable(new DefaultTableModel(new Object[]{"Num", "Artist", "Title", "Album", "Year", "Date", "Lyric"}, 0));
        JScrollPane scrollPane = new JScrollPane(resultTable);
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel pagePanel = new JPanel();
        prevPageButton = new JButton("Prev");
        prevPageButton.addActionListener(this);
        pagePanel.add(prevPageButton);
        nextPageButton = new JButton("Next");
        nextPageButton.addActionListener(this);
        pagePanel.add(nextPageButton);

        panel.add(searchPanel1);
        panel.add(searchPanel2);
        panel.add(buttonPanel);
        panel.add(resultPanel);
        panel.add(pagePanel);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);

        searchHistory = new ArrayList<String>();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resultButton) {
            String searchTerm1 = searchField1.getText();
            String searchTerm2 = searchField2.getText();
            try {
                allResults = performSearch(searchTerm1, searchTerm2);
            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
            }

            totalResults = allResults.size();
            totalPages = totalResults / 10 + ((totalResults % 10 == 0) ? 0 : 1);

            currentPage = 1;
            printResults();

            searchHistory.add("Fields: " + searchTerm1 + " - Values: " + searchTerm2);
        }
        else if (e.getSource() == nextPageButton) {
        	currentPage ++;
        	if (currentPage>totalPages) {
        		currentPage --;
        	}
        	printResults();
        }
        else if (e.getSource() == prevPageButton) {
        	currentPage --;
        	if (currentPage == 0) {
        		currentPage ++;
        	}
        	printResults();
        }
        else if (e.getSource() == historyButton) {
            String[] historyArray = new String[searchHistory.size()];
            historyArray = searchHistory.toArray(historyArray);
            JOptionPane.showMessageDialog(null, historyArray, "Search History", JOptionPane.PLAIN_MESSAGE);
        }
        else if (e.getSource() == yearButton) {
        	sortListByField(allResults, 4);
        	allResults = boldMatchingWords(allResults, csv.getFields(), csv.getTerms());
        	currentPage = 1;
        	printResults();
        }
        else if (e.getSource() == numButton) {
        	sortListByField(allResults, 0);
        	allResults = boldMatchingWords(allResults, csv.getFields(), csv.getTerms());
        	currentPage = 1;
        	printResults();
        }
    }
    
    public void printResults() {
    	int startIndex = (currentPage - 1) * 10;
        int endIndex = Math.min(currentPage * 10, totalResults);
        ArrayList<ArrayList<String>> results = new ArrayList<>(allResults.subList(startIndex, endIndex));
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0);
        for (ArrayList<String> list: results) {
            model.addRow(new Object[]{list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5),list.get(6)});
        }
    }
    
    public ArrayList<ArrayList<String>> boldMatchingWords(ArrayList<ArrayList<String>> allResults, String[] fields, String []terms) {
    	for (int i = 0; i < allResults.size(); i++) {
    		for (String field: fields) {
    	    	for (String term: terms) {
    	    		if (field.equals("num")) {
    	    			System.out.println(field);
    	    			if (allResults.get(i).get(0).equals(term)) {
    	    				allResults.get(i).set(0,"<html><b>" + term + "</b></html>");
    	    				
    	    			} else {
    	    				continue;
    	    			}
    	    		} else if (field.equals("year")) {
    	    			if (allResults.get(i).get(4).equals(term)) {
    	    				allResults.get(i).set(4,"<html><b>" + term + "</b></html>");
    	    				
    	    			} else {
    	    				continue;
    	    			}
    	    		}
    	    	}
    		}
    	}
	    return allResults;
    }
    
    public void sortListByField(ArrayList<ArrayList<String>> allResults, int fieldIndex) {
    	Pattern pattern1 = Pattern.compile("<html><b>(\\d+)</b></html>");
        Pattern pattern2 = Pattern.compile("<html>(\\d+)</html>");
        Matcher matcher1 = null;
        Matcher matcher2 = null;
    	for (ArrayList<String> list: allResults) {
    		String input = list.get(fieldIndex);
    		matcher1 = pattern1.matcher(input);
            matcher2 = pattern2.matcher(input);
            if (matcher1.matches()) {
                String number = matcher1.group(1);
                list.set(fieldIndex, number);
            } else if (matcher2.matches()) {
            	String number = matcher2.group(1);
                list.set(fieldIndex, number);
            } else {
                System.out.println("No match found");
            }
    	}
        Collections.sort(allResults, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> list1, ArrayList<String> list2) {
                int val1 = Integer.parseInt(list1.get(fieldIndex));
                int val2 = Integer.parseInt(list2.get(fieldIndex));
                return Integer.compare(val1, val2);
            }
        });
    }



    public ArrayList<ArrayList<String>> performSearch(String term1, String term2) throws IOException, ParseException {
        // Your search logic here
    	csv = new OpenCsv();
    	csv.openCsv();
        return csv.read(term1,term2);
    }
}
