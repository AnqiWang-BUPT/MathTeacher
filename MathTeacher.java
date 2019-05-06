import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class writes a basic math teacher program with GUI.
 * @author AnqiWang
 * @version 1.0
 */
public class MathTeacher extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final int MAXIMUM_TEXTFIELD = 3;

	enum Operand {                 // Not allow other operators other than {+,-,*,/}
			PLUS, SUBTRACT, MULTIPLY, DIVIDE
		}

	private int correctAnswer;
	private int rightCount;
	private int questionPlayed;

	private JPanel contentPane = new JPanel();
	private JPanel panelCenter = new JPanel();
	private JTextField textAnswer = new JTextField(); // The place where user input the answer. 
	private JLabel lblNewLabel = new JLabel("Question");
	private JLabel labelQuestion = new JLabel("");    // The place where the question is shown.   
	private JLabel labelScore = new JLabel("");       // The place where counts the number of correct answers of the total answers.
	private JLabel labelResult = new JLabel("");      // The place where GUI tells the user the answer is right or wrong. 
	private JPanel panelSouth = new JPanel();
	private JButton btnAnswer = new JButton("Press for answer");
	private JButton btnClear = new JButton("C");
	private JButton btnMinus = new JButton("-");
	private JButton buttons[] = new JButton[10];
	
	/**
	 * This is the constructor which create the frame.
	 */
	public MathTeacher() {
		
		setTitle("Math Teacher");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 160);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);

		// Initiate the JTextField textAnswer
		textAnswer.setBounds(10, 11, 139, 20);
		panelCenter.add(textAnswer);
		textAnswer.setColumns(10);
		// Set maximum length(3) of textAnswer
		textAnswer.setDocument(new PlainDocument() {
			private static final long serialVersionUID = 1L;
            public void insertString(int offset, String s, AttributeSet attributeSet) throws BadLocationException {
				if (s == null || offset < 0) {
					return;
				}
				for (int i = 0; i < s.length(); i++) {
					if (getLength() > MAXIMUM_TEXTFIELD - 1) {
						break;
					}
					super.insertString(offset + i, s.substring(i, i + 1), attributeSet);
				}
				return;
			}
		});
		textAnswer.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int a = e.getKeyChar();
				if (a >= KeyEvent.VK_0 && a <= KeyEvent.VK_9) {
					// If enter is number
				} else if (textAnswer.getText().length() == 0 && a == KeyEvent.VK_MINUS) {
					// Or it is the first one, then can accept '-'
				} else {
					// Consumes this event so that it will not be processed in the default manner by the source which originated it.
					e.consume();
				}
			}
		});

		// Initiate the "Press for answer" button
		btnAnswer.setBounds(7, 50, 142, 23);
		panelCenter.add(btnAnswer);
		btnAnswer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a = textAnswer.getText();
				// Nothing should happen if the user clicks the ‘Press for answer’ button without entering anything in the text field.
				if ("".equals(a)) {
					return;
				} else {
					int userAnswer = Integer.parseInt(a);
					if (userAnswer == correctAnswer) {
						++rightCount;
						labelResult.setText("Correct!");
						labelResult.setForeground(Color.GREEN);
					} else {
						labelResult.setText("Wrong! The answer should be " + correctAnswer);
						labelResult.setForeground(Color.RED);
					}
					++questionPlayed;
					labelScore.setText(rightCount + " correct out of " + questionPlayed);
					generateQuestion();
					textAnswer.setText("");
				}
			}
		});
		
		// Initiate the JLabel lblNewLabel, labelQuestion, labelResult and labelScore
		lblNewLabel.setBounds(179, 14, 150, 14);
		panelCenter.add(lblNewLabel);
		labelQuestion.setBounds(249, 14, 150, 14);
		panelCenter.add(labelQuestion);
		labelResult.setBounds(159, 54, 300, 14);
		panelCenter.add(labelResult);
		labelScore.setBounds(379, 54, 150, 14);
		panelCenter.add(labelScore);

		// Initiate the JLabel panelSouth and put the 12 buttons in Extension 3
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		// Extension 3: Grid Layout Container
		panelSouth.setLayout(new GridLayout(0, 12, 0, 0));
		
		panelSouth.add(btnClear);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAnswer.setText("");
			}
		});
		
		panelSouth.add(btnMinus);
		btnMinus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String a = textAnswer.getText();
				if (a.length() == 0) {
					textAnswer.setText("-");
				}
			}
		});
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new JButton(i + "");
			buttons[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton) e.getSource();
					if (canAppendToAnswerTextfield()) {
						String a = textAnswer.getText();
						textAnswer.setText(a + btn.getText());
					}
				}
			});
		panelSouth.add(buttons[i]);
		}

		reset();
		generateQuestion();
	}
    // Check whether can append to answer TextField
	private boolean canAppendToAnswerTextfield() {
		return textAnswer.getText().length() < MAXIMUM_TEXTFIELD;
	}

	/**
	 * This is the method which reset the rightCount, questionPlayed, labelScore and labelResult.
	 */
	private void reset() {
		rightCount = 0;
		questionPlayed = 0;
		labelScore.setText("");
		labelResult.setText("");
	}

	/**
	 * This is the method which generate the question.
	 */
	private void generateQuestion() {
		int x = (int)(Math.random()*10+1);
		int y = (int)(Math.random()*10+1);
		Operand operand = Operand.values()[(int) (Math.random()*4)];
		
		String op = "";
		switch (operand) {
		case PLUS: {
			correctAnswer = x + y;
			op = "+";
			break;
		}
		case SUBTRACT: {
			correctAnswer = x - y;
			op = "-";
			break;
		}
		case MULTIPLY: {
			correctAnswer = x * y;
			op = "*";
			break;
		}
		case DIVIDE: {
			// Extension 1: think about (x*y)/y (where x and y are integers).
			while (true) {
				x = (int)(Math.random()*5+1);
				y = (int)(Math.random()*5+1);
				x = x * y;
				if (x >= 1 && x <= 10) {
					break;
				}
			}
			correctAnswer = x / y;
			op = "/";
			break;
		}
		}

		String question = x + " " + op + " " + y;
		labelQuestion.setText(question);
	}

	/**
	 * Main method Launch the application.
	 */
	public static void main(String[] args) {
	    MathTeacher frame = new MathTeacher();
	    frame.setVisible(true);
	}
}
