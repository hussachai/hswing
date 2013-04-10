package com.siberhus.ext.miglayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.ConstraintParser;
import net.miginfocom.layout.IDEUtil;
import net.miginfocom.layout.LC;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;

import com.siberhus.hswing.util.ComponentUtils;


public class MigHelper {
	
	protected static final boolean OPAQUE = false;

	protected static boolean buttonOpaque = true;
	protected static int benchRuns = 0;
	protected static boolean contentAreaFilled = true;

	// **********************************************************
	// * Helper Methods
	// **********************************************************
	
	protected static final ToolTipListener toolTipListener = new ToolTipListener();
	protected static final ConstraintListener constraintListener = new ConstraintListener();
	
	public static JLabel createLabel(String text) {
		return createLabel(text, SwingConstants.LEADING);
	}

	public static JLabel createLabel(String text, int align) {
		final JLabel b = new JLabel(text, align);
		configureActiveComponet(b);
		return b;
	}

	public static JComboBox createCombo(Object[] items) {
		JComboBox combo = new JComboBox(items);

		if (PlatformDefaults.getCurrentPlatform() == PlatformDefaults.MAC_OSX)
			combo.setOpaque(false);

		return combo;
	}

	public static JTextField createTextField(int cols) {
		return createTextField("", cols);
	}

	public static JTextField createTextField(String text) {
		return createTextField(text, 0);
	}

	public static JTextField createTextField(String text, int cols) {
		final JTextField b = new JTextField(text, cols);

		configureActiveComponet(b);

		return b;
	}

	protected static final Font BUTT_FONT = new Font("monospaced", Font.PLAIN,
			12);

	public static JButton createButton() {
		return createButton("");
	}
	
	public static JButton createButton(String text) {
		return createButton(text, false);
	}
	
	public static JButton createButton(String text, boolean bold) {
		JButton b = new JButton(text) {
			private static final long serialVersionUID = 1L;
			public void addNotify() {
				super.addNotify();
				if (benchRuns == 0) { // Since this does not exist in the SWT
					// version
					if (getText().length() == 0) {
						String lText = (String) ((MigLayout) getParent()
								.getLayout()).getComponentConstraints(this);
						setText(lText != null && lText.length() > 0 ? lText
								: "<Empty>");
					}
				} else {
					setText("Benchmark Version");
				}
			}
		};

		if (bold)
			b.setFont(b.getFont().deriveFont(Font.BOLD));

		configureActiveComponet(b);

		b.setOpaque(buttonOpaque); // Or window's buttons will have strange
		// border
		b.setContentAreaFilled(contentAreaFilled);

		return b;
	}

	public static JToggleButton createToggleButton(String text) {
		JToggleButton b = new JToggleButton(text);
		// configureActiveComponet(b);
		b.setOpaque(buttonOpaque); // Or window's buttons will have strange
		// border
		return b;
	}

	public static JCheckBox createCheck(String text) {
		JCheckBox b = new JCheckBox(text);

		configureActiveComponet(b);

		b.setOpaque(OPAQUE); // Or window's checkboxes will have strange border
		return b;
	}

	public static JPanel createTabPanel(LayoutManager lm) {
		JPanel panel = new JPanel(lm);
		configureActiveComponet(panel);
		panel.setOpaque(OPAQUE);
		return panel;
	}
	
	public static JComponent createPanel() {
		return createPanel("");
	}

	public static JComponent createPanel(String s) {
		JLabel panel = new JLabel(s, SwingConstants.CENTER) {
			private static final long serialVersionUID = 1L;

			public void addNotify() {
				super.addNotify();
				if (benchRuns == 0) { // Since this does not exist in the SWT
					// version
					if (getText().length() == 0) {
						String lText = (String) ((MigLayout) getParent()
								.getLayout()).getComponentConstraints(this);
						setText(lText != null && lText.length() > 0 ? lText
								: "<Empty>");
					}
				}
			}
		};
		panel.setBorder(new EtchedBorder());
		panel.setOpaque(true);
		configureActiveComponet(panel);

		return panel;
	}

	public static JTextArea createTextArea(String text, int rows, int cols) {
		JTextArea ta = new JTextArea(text, rows, cols);
		ta.setBorder(UIManager.getBorder("TextField.border"));
		ta.setFont(UIManager.getFont("TextField.font"));
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);

		configureActiveComponet(ta);

		return ta;
	}

	public static JScrollPane createTextAreaScroll(String text, int rows, int cols,
			boolean hasVerScroll) {
		JTextArea ta = new JTextArea(text, rows, cols);
		ta.setFont(UIManager.getFont("TextField.font"));
		ta.setWrapStyleWord(true);
		ta.setLineWrap(true);

		JScrollPane scroll = new JScrollPane(ta,
				hasVerScroll ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
						: ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		return scroll;
	}

	public static JComponent configureActiveComponet(JComponent c) {
		if (benchRuns == 0) {
			c.addMouseMotionListener(toolTipListener);
			c.addMouseListener(constraintListener);
		}
		return c;
	}

	static final Color LABEL_COLOR = new Color(0, 70, 213);

	public static void addSeparator(JPanel panel, String text) {
		JLabel l = createLabel(text);
		l.setForeground(LABEL_COLOR);
		
		panel.add(l, "gapbottom 1, span, split 2, aligny center");
		panel.add(configureActiveComponet(new JSeparator()),
				"gapleft rel, growx");
	}
	
	public static class ConstraintListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger())
				react(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
				react(e);
		}

		public void react(MouseEvent e) {
			JComponent c = (JComponent) e.getSource();
			LayoutManager lm = c.getParent().getLayout();
			if (lm instanceof MigLayout == false)
				lm = c.getLayout();

			if (lm instanceof MigLayout) {
				MigLayout layout = (MigLayout) lm;
				boolean isComp = layout.isManagingComponent(c);

				Object compConstr = isComp ? layout.getComponentConstraints(c)
						: null;
				if (isComp && compConstr == null)
					compConstr = "";

				Object rowsConstr = isComp ? null : layout.getRowConstraints();
				Object colsConstr = isComp ? null : layout
						.getColumnConstraints();
				Object layoutConstr = isComp ? null : layout
						.getLayoutConstraints();
				
				ConstraintsDialog cDlg = new ConstraintsDialog(
						ComponentUtils.findJFrame(e.getComponent()),
						layoutConstr instanceof LC ? IDEUtil
								.getConstraintString((LC) layoutConstr, false)
								: (String) layoutConstr,
						rowsConstr instanceof AC ? IDEUtil.getConstraintString(
								(AC) rowsConstr, false, false)
								: (String) rowsConstr,
						colsConstr instanceof AC ? IDEUtil.getConstraintString(
								(AC) colsConstr, false, false)
								: (String) colsConstr,
						compConstr instanceof CC ? IDEUtil.getConstraintString(
								(CC) compConstr, false) : (String) compConstr);

				cDlg.pack();
				cDlg.setLocationRelativeTo(c);

				if (cDlg.showDialog()) {
					try {
						if (isComp) {
							String constrStr = cDlg.componentConstrTF.getText()
									.trim();
							layout.setComponentConstraints(c, constrStr);
							if (c instanceof JButton) {
								c.setFont(BUTT_FONT);
								((JButton) c)
										.setText(constrStr.length() == 0 ? "<Empty>"
												: constrStr);
							}
						} else {
							layout.setLayoutConstraints(cDlg.layoutConstrTF
									.getText());
							layout.setRowConstraints(cDlg.rowsConstrTF
									.getText());
							layout.setColumnConstraints(cDlg.colsConstrTF
									.getText());
						}
					} catch (Exception ex) {
						StringWriter sw = new StringWriter();
						ex.printStackTrace(new PrintWriter(sw));
						JOptionPane.showMessageDialog(SwingUtilities
								.getWindowAncestor(c), sw.toString(),
								"Error parsing Constraint!",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					c.invalidate();
					c.getParent().validate();
				}
			}
		}
	}

	protected static class ToolTipListener extends MouseMotionAdapter {
		public void mouseMoved(MouseEvent e) {
			JComponent c = (JComponent) e.getSource();
			LayoutManager lm = c.getParent().getLayout();
			if (lm instanceof MigLayout) {
				Object constr = ((MigLayout) lm).getComponentConstraints(c);
				if (constr instanceof String)
					c.setToolTipText((constr != null ? ("\"" + constr + "\"")
							: "null"));
			}
		}
	}

	protected static class ConstraintsDialog extends JDialog implements
			ActionListener, KeyEventDispatcher {
		private static final long serialVersionUID = 1L;
		private static final Color ERROR_COLOR = new Color(255, 180, 180);
		private final JPanel mainPanel = new JPanel(new MigLayout(
				"fillx,flowy,ins dialog", "[fill]", "2[]2"));
		final JTextField layoutConstrTF;
		final JTextField rowsConstrTF;
		final JTextField colsConstrTF;
		final JTextField componentConstrTF;

		private final JButton okButt = new JButton("OK");
		private final JButton cancelButt = new JButton("Cancel");
		
		private boolean okPressed = false;
		
		public ConstraintsDialog(Frame owner, String layoutConstr,
				String rowsConstr, String colsConstr, String compConstr) {
			super(owner, (compConstr != null ? "Edit Component Constraints"
					: "Edit Container Constraints"), true);

			layoutConstrTF = createConstraintField(layoutConstr);
			rowsConstrTF = createConstraintField(rowsConstr);
			colsConstrTF = createConstraintField(colsConstr);
			componentConstrTF = createConstraintField(compConstr);

			if (componentConstrTF != null) {
				mainPanel.add(new JLabel("Component Constraints"));
				mainPanel.add(componentConstrTF);
			}

			if (layoutConstrTF != null) {
				mainPanel.add(new JLabel("Layout Constraints"));
				mainPanel.add(layoutConstrTF);
			}

			if (colsConstrTF != null) {
				mainPanel.add(new JLabel("Column Constraints"), "gaptop unrel");
				mainPanel.add(colsConstrTF);
			}

			if (rowsConstrTF != null) {
				mainPanel.add(new JLabel("Row Constraints"), "gaptop unrel");
				mainPanel.add(rowsConstrTF);
			}

			mainPanel.add(okButt, "tag ok,split,flowx,gaptop 15");
			mainPanel.add(cancelButt, "tag cancel,gaptop 15");

			setContentPane(mainPanel);

			okButt.addActionListener(this);
			cancelButt.addActionListener(this);
		}

		public void addNotify() {
			super.addNotify();
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.addKeyEventDispatcher(this);
		}
		
		public void removeNotify() {
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.removeKeyEventDispatcher(this);
			super.removeNotify();
		}

		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				dispose();
			return false;
		}

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButt)
				okPressed = true;
			dispose();
		}
		
		private JTextField createConstraintField(String text) {
			if (text == null)
				return null;

			final JTextField tf = new JTextField(text, 50);
			tf.setFont(new Font("monospaced", Font.PLAIN, 12));

			tf.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						okButt.doClick();
						return;
					}

					javax.swing.Timer timer = new Timer(50,
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									String constr = tf.getText();
									try {
										if (tf == layoutConstrTF) {
											ConstraintParser
													.parseLayoutConstraint(constr);
										} else if (tf == rowsConstrTF) {
											ConstraintParser
													.parseRowConstraints(constr);
										} else if (tf == colsConstrTF) {
											ConstraintParser
													.parseColumnConstraints(constr);
										} else if (tf == componentConstrTF) {
											ConstraintParser
													.parseComponentConstraint(constr);
										}

										tf.setBackground(Color.WHITE);
										okButt.setEnabled(true);
									} catch (Exception ex) {
										tf.setBackground(ERROR_COLOR);
										okButt.setEnabled(false);
									}
								}
							});
					timer.setRepeats(false);
					timer.start();
				}
			});

			return tf;
		}

		private boolean showDialog() {
			setVisible(true);
			return okPressed;
		}
	}
	
}
