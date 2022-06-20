package logic;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Ivan Local
 */
public class VezbanjeDeljenja extends JFrame {

    private JButton wrongAnswerButton;
    private JTextField currQuestionTextField;
    private JTextArea answersTextArea;
    private JButton correctAnswerButton;
    private Dimension horizontalDimension;
    //logic
    private int correctAnswersNo;
    private int wrongAnswersNo;
    private JButton startButton;
    private Timer timer;
    private Timer timerCurr;
    private ArrayList<String> allQuestionsArrayList;
    private ArrayList<String> askedQuestionsArrayList;
    private ArrayList<Boolean> correctAnswerProvidedArrayList;
    private ArrayList<String> answersTimeList;
    private Random random;
    private int currCount;
    private JTextField resultsTextField;
    private JScrollPane answersListPane;
    private JList<Object> valueList;
    private DefaultListModel<Object> valueListModel;
    private JPanel timePanel;
    private JTextField totalTimeTextField;
    private JTextField currTimeTextField;
    private JLabel totTimeLabel;
    private JLabel currTimeLabel;
    private long startTimeTotal;
    private long startTimeCurr;
    private long currTimeTotal;
    private long currTimeCurr;
    private long endTimeTotal;
    private int totTotalTime;
    private int currTotalTime;
    private long startTimeCurrMiliSec;
    private long stopTimeCurrMiliSec;
    private DecimalFormat timeFormat;
    private JButton clearButton;
    private JFileChooser myChooser;
    private JButton saveButton;

    public VezbanjeDeljenja() {
        super("Vezbanje Deljenja");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        makeClearButton(gbc);
        makeQuestions();
        makeTime(gbc);
        makeCurrenQuestion(gbc);
        makeInterface(gbc);
        makeResults(gbc);
        makeStartButton(gbc);
        super.pack();
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) (0.5 * (screenSize.width
                - super.getWidth())), (int) (0.5 * (screenSize.height
                - super.getHeight())), super.getWidth(), super.getHeight());
        horizontalDimension = new Dimension(400, 30);
        timeFormat = new DecimalFormat("0.00");
    }

    private void makeQuestions() {
        allQuestionsArrayList = new ArrayList<>();
        askedQuestionsArrayList = new ArrayList<>();
        correctAnswerProvidedArrayList = new ArrayList<>();
        answersTimeList = new ArrayList<>();
        currCount = 0;
        random = new Random();
        for (int i = 0; i < 500; i++) {
            int number1 = random.nextInt(9) + 2;
            int number2 = random.nextInt(9) + 2;
            int number3 = number1 * number2; 
            allQuestionsArrayList.add(number3 + " / " + number2);
        }
        System.out.println("Questions are: ");
        for (String currQuestion : allQuestionsArrayList) {
            System.out.println(currQuestion);
        }
    }

    private void makeClearButton(GridBagConstraints gbc) {
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearActionPerformed(e);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 10, 10);
        getContentPane().add(clearButton, gbc);
    }

    private void makeTime(GridBagConstraints gbc) {
        Font timeFont = new Font("Arial", Font.BOLD, 24);
        timePanel = new JPanel(new GridBagLayout());

        totTimeLabel = new JLabel("TOTAL TIME");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 10, 10);
        timePanel.add(totTimeLabel, gbc);

        currTimeLabel = new JLabel("Current Question time");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 0);
        timePanel.add(currTimeLabel, gbc);

        totalTimeTextField = new JTextField();
        totalTimeTextField.setColumns(14);
        totalTimeTextField.setFont(timeFont);
        totalTimeTextField.setBackground(Color.white);
        totalTimeTextField.setOpaque(true);
        totalTimeTextField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 10, 10);
        timePanel.add(totalTimeTextField, gbc);

        currTimeTextField = new JTextField();
        currTimeTextField.setColumns(14);
        currTimeTextField.setFont(timeFont);
        currTimeTextField.setBackground(Color.white);
        currTimeTextField.setOpaque(true);
        currTimeTextField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 0);
        timePanel.add(currTimeTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(timePanel, gbc);
    }

    private void makeCurrenQuestion(GridBagConstraints gbc) {
        currQuestionTextField = new JTextField("");
        currQuestionTextField.setColumns(30);
        currQuestionTextField.setFont(new Font("Arial", Font.BOLD, 24));
        currQuestionTextField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(currQuestionTextField, gbc);
    }

    private void makeInterface(GridBagConstraints gbc) {
        Dimension buttonDimension = new Dimension(200, 480);
        JPanel interfacePanel = new JPanel(new GridBagLayout());
        wrongAnswerButton = new JButton("Wrong Answer");
        wrongAnswerButton.setEnabled(false);
        wrongAnswerButton.setPreferredSize(buttonDimension);
        wrongAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wrongAnswerActionPerformed(e);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        interfacePanel.add(wrongAnswerButton, gbc);

        valueList = new JList<>();
        valueListModel = new DefaultListModel<>();
        valueList.setModel(valueListModel);
        answersListPane = new JScrollPane(valueList);
        answersListPane.setPreferredSize(new Dimension(200, 480));

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        interfacePanel.add(answersListPane, gbc);

        correctAnswerButton = new JButton("Correct Answer");
        correctAnswerButton.setEnabled(false);
        correctAnswerButton.setPreferredSize(buttonDimension);
        correctAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                correctAnswerActionPerformed(e);
            }
        });
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        interfacePanel.add(correctAnswerButton, gbc);

        setAccelerators();

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(interfacePanel, gbc);
    }

    private void makeResults(GridBagConstraints gbc) {
        resultsTextField = new JTextField();
        resultsTextField.setColumns(30);
        resultsTextField.setBackground(Color.red);
        resultsTextField.setOpaque(true);
        resultsTextField.setFont(new Font("Arial", Font.BOLD, 24));
        resultsTextField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(resultsTextField, gbc);
    }

    private void makeStartButton(GridBagConstraints gbc) {
        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(630, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startActionPerformed(e);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(startButton, gbc);

        saveButton = new JButton("Save");
        //saveButton.setPreferredSize(new Dimension(50, 50));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFileActionPerformed(e);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 25);
        getContentPane().add(saveButton, gbc);
    }

    private void wrongAnswerActionPerformed(ActionEvent e) {
        wrongAnswersNo++;
        correctAnswerProvidedArrayList.add(false);
        System.out.println("CurrCount " + currCount);
        System.out.println("Wrong answers " + wrongAnswersNo);
        wrongAndCorrectAnswerCommonCode();
    }

    private void correctAnswerActionPerformed(ActionEvent e) {
        correctAnswersNo++;
        correctAnswerProvidedArrayList.add(true);
        System.out.println("CurrCount " + currCount);
        System.out.println("Correct answers " + correctAnswersNo);
        wrongAndCorrectAnswerCommonCode();
    }

    private void wrongAndCorrectAnswerCommonCode() {
        restartTimerCurr();
        valueListModel.addElement(allQuestionsArrayList.get(currCount - 1)
                + " - " + correctAnswerProvidedArrayList.get(currCount - 1)
                + " - " + answersTimeList.get(currCount - 1));
        updateResultsTextField();
        String currAskedQuestion = allQuestionsArrayList.get(currCount);
        currQuestionTextField.setText(currAskedQuestion);
        currCount++;
    }

    private void restartTimerCurr() {
        stopTimeCurrMiliSec = System.currentTimeMillis();
        timerCurr.stop();
        currTimeTextField.setText("");
        double answerTime = (stopTimeCurrMiliSec * (1.0) - startTimeCurrMiliSec) / 1000;
        answersTimeList.add(timeFormat.format(answerTime));
        startTimeCurrMiliSec = System.currentTimeMillis();
        startTimeCurr = startTimeCurrMiliSec / 1000;
        timerCurr.start();
    }

    private void updateResultsTextField() {
        DecimalFormat dm = new DecimalFormat("0.00");
        int correctAnserws = 0;
        for (boolean bool : correctAnswerProvidedArrayList) {
            if (bool) {
                correctAnserws++;
            }
        }
        double percentCorrect = (1.0) * correctAnserws / correctAnswerProvidedArrayList.size() * 100.0;
        System.out.println("Procenat je " + percentCorrect);
        String results = correctAnserws + " / " + correctAnswerProvidedArrayList.size()
                + "\t" + " u procentima " + dm.format(percentCorrect);
        resultsTextField.setText(results);
        resultsTextFieldSetColor(percentCorrect);
    }

    private void resultsTextFieldSetColor(double percentCorrect) {
        if (percentCorrect > 95.0) {
            resultsTextField.setBackground(Color.magenta);
            resultsTextField.setForeground(Color.white);
        } else if (percentCorrect > 90.0) {
            resultsTextField.setBackground(Color.blue);
            resultsTextField.setForeground(Color.white);
        } else if (percentCorrect > 80) {
            resultsTextField.setBackground(Color.green);
            resultsTextField.setForeground(Color.black);
        } else if (percentCorrect > 70) {
            resultsTextField.setBackground(Color.yellow);
            resultsTextField.setForeground(Color.black);
        } else if (percentCorrect > 60) {
            resultsTextField.setBackground(Color.orange);
            resultsTextField.setForeground(Color.black);
        } else {
            resultsTextField.setBackground(Color.red);
            resultsTextField.setForeground(Color.black);
        }
    }

    private void startActionPerformed(ActionEvent e) {
        String currAskedQuestion = allQuestionsArrayList.get(currCount);
        askedQuestionsArrayList.add(currAskedQuestion);
        currQuestionTextField.setText(currAskedQuestion);
        currCount++;
        System.out.println("CurrCount FIRST = " + currCount);
        startButton.setVisible(false);
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                totTimerActionPerformed(e);
            }
        });
        timerCurr = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currTimerActionPerfomed(e);
            }
        });
        wrongAnswerButton.setEnabled(true);
        correctAnswerButton.setEnabled(true);
        totTotalTime = 300;
        currTotalTime = 10;
        startTimeCurrMiliSec = System.currentTimeMillis();
        startTimeTotal = System.currentTimeMillis() / 1000;
        startTimeCurr = startTimeCurrMiliSec / 1000;
        endTimeTotal = startTimeTotal + totTotalTime;
        timer.start();
        timerCurr.start();
    }

    private void totTimerActionPerformed(ActionEvent e) {
        currTimeTotal = System.currentTimeMillis() / 1000;
        totalTimeTextField.setText(String.valueOf(currTimeTotal - startTimeTotal));
        long diffTime = currTimeTotal - startTimeTotal;
        if (diffTime >= totTotalTime) {
            Toolkit.getDefaultToolkit().beep();
            Toolkit.getDefaultToolkit().beep();
            Toolkit.getDefaultToolkit().beep();
            Toolkit.getDefaultToolkit().beep();
            Toolkit.getDefaultToolkit().beep();
            timer.stop();
            String statistika = resultsTextField.getText();

            JOptionPane.showConfirmDialog(null, statistika, "Uspeh",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            saveFileActionPerformed(e);
        }
    }

    private void currTimerActionPerfomed(ActionEvent e) {
        currTimeCurr = System.currentTimeMillis() / 1000;
        currTimeTextField.setText(String.valueOf(currTimeCurr - startTimeCurr));
        long currDiffTime = currTimeCurr - startTimeCurr;
        if (currDiffTime >= currTotalTime) {
            Toolkit.getDefaultToolkit().beep();
            timerCurr.stop();
        }
    }

    private void clearActionPerformed(ActionEvent e) {
        allQuestionsArrayList.clear();
        askedQuestionsArrayList.clear();
        answersTimeList.clear();
        correctAnswerProvidedArrayList.clear();
        totalTimeTextField.setText("");
        currTimeTextField.setText("");
        currQuestionTextField.setText("");
        resultsTextField.setText("");
        valueListModel.clear();
        timer.stop();
        timerCurr.stop();
        currCount = 0;
        makeQuestions();
        startButton.setVisible(true);
        wrongAnswerButton.setEnabled(false);
        correctAnswerButton.setEnabled(false);
    }

    private void setAccelerators() {
        wrongAnswerButton.setMnemonic('A');
        correctAnswerButton.setMnemonic('D');
    }

    //snimanje fajla
    private void saveFileActionPerformed(ActionEvent e) {
        timer.stop();
        timerCurr.stop();
        myChooser = new JFileChooser();
        myChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        myChooser.setDialogTitle("Save Text File");
        myChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        String fileName;
        if (myChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            //see if the file already exist
            if (myChooser.getSelectedFile().exists()) {
                int response;
                response = JOptionPane.showConfirmDialog(null, myChooser.getSelectedFile().toString() + " exists. Overwrite?",
                        "Confirm save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.NO_OPTION) {
                    return;
                }
                // make sure file has txt extension
                // strip off any extension that might be there
                // then tack on txt
                fileName = myChooser.getSelectedFile().toString();
                System.out.println("File name fajla koje je selektovan");
            } else {

                fileName = myChooser.getSelectedFile().getName();
                System.out.println("Za fajl koji nije bio selektovan. New file name is " + fileName);
            }
            int dotLocation = fileName.indexOf(".");
            if (dotLocation == -1) {
                //no extension
                fileName += ".txt";
            } else {
                //make sure that file is with txt extension
                fileName = fileName.substring(0, dotLocation);
                fileName += ".txt";
                System.out.println("File name posle substringa, ako je imao extenziju" + fileName);
            }
            try {
                // Open output file and write
                System.out.println("OutputFile is " + fileName);
                PrintWriter outputFile = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
                outputFile.println(resultsTextField.getText());
                for (int i = 0; i < valueListModel.size(); i++) {
                    {
                        outputFile.println(valueListModel.get(i));
                        System.out.println("Line " + i + " " + valueListModel.get(i));
                    }
                }
                outputFile.flush();
                outputFile.close();
            } catch (IOException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error Writing File",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

//    public static void main(String[] args) {
//        VezbanjeDeljenja vezbanje = new VezbanjeDeljenja();
//        vezbanje.setVisible(true);
//    }
}