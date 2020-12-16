package bsu.rfe.java.group8.lab3.Sirota.Var1;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    //ищем ячейки, строковое представление совпадает с needle(иголкой)
    private String needle = null;
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
        //Показать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        //установить в качестве разделителя дробной части точку
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        //разместить надпись внутри панели
        panel.add(label);
        //установить выравнивание надписи по левому краю
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
        //преобразовать в Double с помощью форматировщика
        String formattedDouble = formatter.format(value);
        //установить текст надписи равным строковуму представлению числа
        label.setText(formattedDouble);
        if (col == 1 && needle != null && needle.equals(formattedDouble)){
            //номер столбца 1 значит 2 столбец + иголка не null значит что-то ищем + значение иголки
            //совподает со значением ячейки значит окрашиваем в крассный
            panel.setBackground(Color.RED);
        } else {
            //иначе в белый
            panel.setBackground(Color.WHITE);
        }
        return panel;
    }
    public void setNeedle(String needle){
        this.needle = needle;
    }
}
