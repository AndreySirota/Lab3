package bsu.rfe.java.group8.lab3.Sirota.Var1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MainFrame extends JFrame {
    //разиеры окна
    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;
    //массив коэффициентов многочлена
    private Double[] coefficients;
    //Объект диалогового окна для выбора файлов
    private JFileChooser fileChooser = null;
    //Элементы меню
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem showInfMenuItem;
    //поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    //визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    //модель данный с результатами вычислений
    private GornerTableModel data;

    public MainFrame(Double[] coefficients) {
        //вызов конструктора предка
        super("Тубулирование многочлена на отрезке по схеме Горнера");
        //передаем во внутреннее поле кэфы
        this.coefficients = coefficients;
        //размеры окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        //создание меню
        JMenuBar menuBar = new JMenuBar();
        //Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
        //Добавить в меню пункт "файл"
        JMenu fileMenu = new JMenu("Файл");
        //добавить его в главное меню
        menuBar.add(fileMenu);
        //создаем пункт "таблица"
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        //создаем пункт справка
        JMenu referenceMenu = new JMenu("Справка");
        menuBar.add(referenceMenu);
        //Создать новое "действие" по осохранению в текстовый файл
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    //если экземпляр не создан - то создаем его
                    fileChooser = new JFileChooser();
                    //и инициализируем текущей директивой
                    fileChooser.setCurrentDirectory(new File("."));
                }
                //показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    //если результат его показа успешны сохраняем данные в файл
                    saveToTextFile(fileChooser.getSelectedFile());
                }
            }
        };
        //добавить соответствующий пункт подменю в меню "Файл"
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);
        //Создать новое "действие" по осохранению в текстовый файл
        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    //если экземпляр диалогового окна еще не создан - то создаем его
                    fileChooser = new JFileChooser();
                    //и инициализируем текущей директивой
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    //если результат его показа успешны сохраняем данные в двоичный файл
                    saveToGraphicsFile(fileChooser.getSelectedFile());
                }
            }
        };
        //добавить соответсвующий пункт подменю в меню "Файл"
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);
        //Создать новое "действие" по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            @Override
            public void actionPerformed(ActionEvent event) {
                //запросить пользователя ввести искомую строчку
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                //
                renderer.setNeedle(value);
                //обновить таблицуустановить значение в качестве иголки
                getContentPane().repaint();
            }
        };
        //добавить действие в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
        searchValueMenuItem.setEnabled(false);
        Action refereceOboutMe = new AbstractAction("Справка") {
            @Override
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(MainFrame.this, "Автор - Сирота Андрей, 8 Группа!", "Справка", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        //добавить действие в подменю справка
        showInfMenuItem = referenceMenu.add(refereceOboutMe);
        showInfMenuItem.setEnabled(true);
        //создаем области с полями для границ ввода на отрезке с шагом
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        //поле до границы
        JLabel labelForTo = new JLabel("до");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        //поле с шагом
        JLabel labelForStep = new JLabel("с шагом");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        //создаем контейнер для всего этого
        Box hBoxRange = Box.createHorizontalBox();
        //делаем объемную рамку
        hBoxRange.setBorder(BorderFactory.createBevelBorder(1));
        hBoxRange.add(Box.createHorizontalGlue());
        hBoxRange.add(labelForFrom);
        hBoxRange.add(Box.createVerticalStrut(10));
        hBoxRange.add(textFieldFrom);
        hBoxRange.add(Box.createVerticalStrut(20));
        hBoxRange.add(labelForTo);
        hBoxRange.add(Box.createVerticalStrut(10));
        hBoxRange.add(textFieldTo);
        hBoxRange.add(Box.createVerticalStrut(20));
        hBoxRange.add(labelForStep);
        hBoxRange.add(Box.createVerticalStrut(10));
        hBoxRange.add(textFieldStep);
        hBoxRange.add(Box.createHorizontalGlue());
        //установим размер области вавным удвоенному мин.,что бы при компановке область не сдавили
        hBoxRange.setPreferredSize(new Dimension(new Double(hBoxRange.getMaximumSize().getWidth()).intValue(), new Double(hBoxRange.getMaximumSize().getHeight()).intValue() * 2));
        // ставим область в верхнюю часть
        getContentPane().add(hBoxRange, BorderLayout.NORTH);