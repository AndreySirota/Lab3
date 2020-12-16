package bsu.rfe.java.group8.lab3.Sirota.Var1;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    private double result[] = new double[1];

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients){
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom(){
        return from;
    }
    public Double getTo(){
        return to;
    }
    public Double getStep(){
        return step;
    }
    public int getColumnCount(){
        return 3;
    }

    @Override
    public int getRowCount() {
        //вычислить количество точек между началом и концом отрезка исходя из шага тубулирования
        return new Double(Math.ceil((to - from) / step)).intValue() + 1;
    }
    @Override
    public Object getValueAt(int row, int col){
        //вычислить Х как начало_отрезка + шаг * номер_строки
        double x = from + step * row;
        switch (col) {
            case 0:
                //если запрашивается значение 1-го столбца, то это Х
                return x;
            case 1:
                //если запрашивается значение 2-го столбца, то это значение многочлена
                result[0] = 0.0;
                for (int i = 0; i < coefficients.length; i++) {
                    result[0] += Math.pow(x, coefficients.length - 1 - i) * coefficients[i];
                }
                return result[0];
            default:
                result[0] = 0.0;
                for (int i = 0; i < coefficients.length; i++) {
                    result[0] += Math.pow(x, coefficients.length - 1 - i) * coefficients[i];
                }
                boolean flag = false;
                if (result[0] > 0){
                    flag = true;
                }
                if (flag == true){
                    return true;
                } else {
                    return false;
                }
        }
    }
    @Override
    public String getColumnName(int col){
        switch (col){
            case 0:
                return "Значение Х";
            case 1:
                return "Значение многочлена";
            default:
                return "Значение больше нуля?";
        }
    }
    @Override
    public Class<?> getColumnClass(int col){
        //и в 1-ом и во 2-ом столбце находятся значения типа Double в 3 Boolean
        switch (col){
            case 2:
                return Boolean.class;
            default:
                return Double.class;
        }
    }
}

