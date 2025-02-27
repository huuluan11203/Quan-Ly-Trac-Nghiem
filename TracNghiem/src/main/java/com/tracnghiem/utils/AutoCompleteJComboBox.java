/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tracnghiem.utils;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author X
 */
public class AutoCompleteJComboBox extends JComboBox<String>{
     private final List<String> items;
    private final JTextField textField;

    public AutoCompleteJComboBox(List<String> data) {
        super(new DefaultComboBoxModel<>(data.toArray(new String[0])));
        this.items = new ArrayList<>(data);
        this.setEditable(true);

        textField = (JTextField) this.getEditor().getEditorComponent();
        textField.setDocument(new AutoCompleteDocument());
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String input = textField.getText().toLowerCase();
                SwingUtilities.invokeLater(() -> updateList(input));
            }
        });
    }

    private void updateList(String input) {
        if (input.isEmpty()) {
            this.hidePopup();
            return;
        }

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String item : items) {
            if (item.toLowerCase().contains(input)) {
                model.addElement(item);
            }
        }
        
        this.setModel(model);
        textField.setText(input);
        this.setPopupVisible(model.getSize() > 0);
    }

    private class AutoCompleteDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offs, str, a);
            updateList(getText(0, getLength()).toLowerCase());
        }
    }
}
