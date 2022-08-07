package editor;

import javax.swing.*;

public class TextEditor extends JFrame {
    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setTitle("The first stage");
        setLayout(null);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setName("TextArea");
        area.setBounds(20, 20, 245, 225);
        add(area);

        setVisible(true);
    }
}
