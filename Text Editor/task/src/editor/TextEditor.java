package editor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {

    public JTextField searchField;
    public JTextArea text;
    public JFileChooser fileChooser;
    JCheckBox regex;
    List<Integer[]> indexes = new ArrayList<>();

    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setTitle("Text editor");
        //setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        fileChooser = new JFileChooser();
        fileChooser.setName("FileChooser");
        //fileChooser.setSelectedFile(new File("C:\\java\\hyperskill.org\\Text Editor\\doc.txt"));
        add(fileChooser);

        // text area
        text = new JTextArea();
        text.setName("TextArea");

        JScrollPane scrollPane = new JScrollPane(text, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setName("ScrollPane");
        scrollPane.setPreferredSize(new Dimension(300, 300));

        // file & search area

        ImageIcon iconOpen = new ImageIcon("open.png");
        ImageIcon iconSave = new ImageIcon("save.png");
        ImageIcon iconSearch = new ImageIcon("search.png");
        ImageIcon iconLeft = new ImageIcon("left.png");
        ImageIcon iconRight = new ImageIcon("right.png");

        JButton buttonOpen = new JButton(iconOpen);
        buttonOpen.setName("OpenButton");
        buttonOpen.setPreferredSize(new Dimension(35, 35));
        buttonOpen.addActionListener(new ActionListenerOpen());

        JButton buttonSave = new JButton(iconSave);
        buttonSave.setName("SaveButton");
        buttonSave.setPreferredSize(new Dimension(35, 35));
        buttonSave.addActionListener(new ActionListenerSave());

        searchField = new JTextField();
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(100, 25));

        JButton buttonSearch = new JButton(iconSearch);
        buttonSearch.setName("StartSearchButton");
        buttonSearch.setPreferredSize(new Dimension(35, 35));
        buttonSearch.addActionListener(new ActionListenerSearch());

        JButton buttonPrevious = new JButton(iconLeft);
        buttonPrevious.setName("PreviousMatchButton");
        buttonPrevious.setPreferredSize(new Dimension(35, 35));
        buttonPrevious.addActionListener(new ActionListenerPrevious());

        JButton buttonNext = new JButton(iconRight);
        buttonNext.setName("NextMatchButton");
        buttonNext.setPreferredSize(new Dimension(35, 35));
        buttonNext.addActionListener(new ActionListenerNext());

        regex = new JCheckBox("Use regex");
        regex.setName("UseRegExCheckbox");

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(buttonOpen);
        panel.add(buttonSave);
        panel.add(searchField);
        panel.add(buttonSearch);
        panel.add(buttonPrevious);
        panel.add(buttonNext);
        panel.add(regex);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // menu
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        menuFile.setName("MenuFile");
        menuFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem menuItemLoad = new JMenuItem("Open", KeyEvent.VK_O);
        menuItemLoad.setName("MenuOpen");
        menuItemLoad.addActionListener(new ActionListenerOpen());
        menuFile.add(menuItemLoad);

        JMenuItem menuItemSave = new JMenuItem("Save", KeyEvent.VK_S);
        menuItemSave.setName("MenuSave");
        menuItemSave.addActionListener(new ActionListenerSave());
        menuFile.add(menuItemSave);

        menuFile.addSeparator();

        JMenuItem menuItemExit = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItemExit.setName("MenuExit");
        menuItemExit.addActionListener(actionEvent -> dispose());
        menuFile.add(menuItemExit);

        JMenu menuSearch = new JMenu("Search");
        menuSearch.setName("MenuSearch");
        menuSearch.setMnemonic(KeyEvent.VK_E);

        JMenuItem menuItemStartSearch = new JMenuItem("Start search", KeyEvent.VK_S);
        menuItemStartSearch.setName("MenuStartSearch");
        menuItemStartSearch.addActionListener(new ActionListenerSearch());
        menuSearch.add(menuItemStartSearch);

        JMenuItem menuItemPreviousSearch = new JMenuItem("Previous search", KeyEvent.VK_P);
        menuItemPreviousSearch.setName("MenuPreviousMatch");
        menuItemPreviousSearch.addActionListener(new ActionListenerPrevious());
        menuSearch.add(menuItemPreviousSearch);

        JMenuItem menuItemNextMatch = new JMenuItem("Next match", KeyEvent.VK_N);
        menuItemNextMatch.setName("MenuNextMatch");
        menuItemNextMatch.addActionListener(new ActionListenerNext());
        menuSearch.add(menuItemNextMatch);

        JMenuItem menuItemUseRegularExpressions = new JMenuItem("Use regular expressions", KeyEvent.VK_R);
        menuItemUseRegularExpressions.setName("MenuUseRegExp");
        menuItemUseRegularExpressions.addActionListener(actionEvent -> regex.setSelected(!regex.isSelected()));
        menuSearch.add(menuItemUseRegularExpressions);

        menuBar.add(menuFile);
        menuBar.add(menuSearch);
        setJMenuBar(menuBar);

        setVisible(true);
    }

    class ActionListenerSearch implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            indexes.clear();

            if (regex.isSelected()) {
                Pattern pattern = Pattern.compile(searchField.getText());
                Matcher matcher = pattern.matcher(text.getText());
                while (matcher.find()) {
                    indexes.add(new Integer[] {matcher.start(), matcher.end()});
                }
            } else {
                int index = 0;
                String textStr = text.getText();
                do {
                    index = textStr.indexOf(searchField.getText(), index);
                    if (index != -1) {
                        indexes.add(new Integer[] {index, index + searchField.getText().length()});
                        index++;
                    }
                } while (index != -1);
            }

            int fromIndex = 0;
            for (int i = 0; i < indexes.size(); i++) {
                if (indexes.get(i)[0] > fromIndex) {
                    text.setCaretPosition(indexes.get(i)[1]);
                    text.select(indexes.get(i)[0], indexes.get(i)[1]);
                    text.grabFocus();
                    break;
                }
            }
        }
    }

    class ActionListenerPrevious implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int fromIndex = text.getCaretPosition();
            for (int i = indexes.size() - 1; i >=0 ; i--) {
                if (indexes.get(i)[1] < fromIndex) {
                    text.setCaretPosition(indexes.get(i)[1]);
                    text.select(indexes.get(i)[0], indexes.get(i)[1]);
                    text.grabFocus();
                    return;
                }
            }

            fromIndex = text.getText().length() - 1;
            for (int i = indexes.size() - 1; i >=0 ; i--) {
                if (indexes.get(i)[1] < fromIndex) {
                    text.setCaretPosition(indexes.get(i)[1]);
                    text.select(indexes.get(i)[0], indexes.get(i)[1]);
                    text.grabFocus();
                    return;
                }
            }
        }
    }

    class ActionListenerNext implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            int fromIndex = text.getCaretPosition();
            for (int i = 0; i < indexes.size(); i++) {
                if (indexes.get(i)[0] > fromIndex) {
                    text.setCaretPosition(indexes.get(i)[1]);
                    text.select(indexes.get(i)[0], indexes.get(i)[1]);
                    text.grabFocus();
                    return;
                }
            }

            fromIndex = 0;
            for (int i = 0; i < indexes.size(); i++) {
                if (indexes.get(i)[0] > fromIndex) {
                    text.setCaretPosition(indexes.get(i)[1]);
                    text.select(indexes.get(i)[0], indexes.get(i)[1]);
                    text.grabFocus();
                    return;
                }
            }
        }
    }

    class ActionListenerSave implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fileWriter = new FileWriter(fileChooser.getSelectedFile());
                     BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

                    bufferedWriter.write(text.getText());

                } catch (FileNotFoundException e) {
                    System.out.println("File not found!");
                } catch (IOException e) {
                    System.out.println("IOException!");
                }
            }
        }
    }

    class ActionListenerOpen implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            text.setText("");
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try (FileReader fileReader = new FileReader(fileChooser.getSelectedFile());
                     BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                    text.read(bufferedReader, null);

                } catch (FileNotFoundException e) {
                    System.out.println("File not found!");
                } catch (IOException e) {
                    System.out.println("IOException!");
                }
            }
        }
    }

}
