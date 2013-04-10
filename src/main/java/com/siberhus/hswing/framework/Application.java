package com.siberhus.hswing.framework;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siberhus.hswing.util.ComponentUtils;

public abstract class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	// Life Cycle *************************************************************
	
	protected Map<Page,String> pageMap;
	protected JFrame mainFrame;
	protected JComponent mainPanel;
	protected JMenuBar mainMenuBar;
	protected JComponent mainStatusBar;
	 
	
	public Application(){
		pageMap = new LinkedHashMap<Page, String>();
	}
	
	public void addPage(Page page){
		pageMap.put(page,page.toString());
		page.setApplication(this);
	}
	
	public JFrame getMainFrame(){
		return mainFrame;
	}
	
	public JComponent getMainPanel(){
		return mainPanel;
	}
	
	public JMenuBar getMainMenuBar(){
		return mainMenuBar;
	}
	
	public JComponent getMainStatusBar(){
		return mainStatusBar;
	}
	
	public void showErrorDialog(String message){
		JOptionPane.showMessageDialog(getMainFrame()
				, message, "ERROR",JOptionPane.ERROR_MESSAGE);
	}
	
	public void showInfoDialog(String message){
		JOptionPane.showMessageDialog(getMainFrame()
				, message, "INFO",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showWarningDialog(String message){
		JOptionPane.showMessageDialog(getMainFrame()
				, message, "WARNING",JOptionPane.WARNING_MESSAGE);
	}
	
	public String showInputDialog(String message,String initailValue){
		return showInputDialog(message, initailValue,false);
	}
	
	public String showInputDialog(String message,String initialValue,boolean force){
		String value = null;
		do{
			value = JOptionPane.showInputDialog(getMainFrame()
					, message, initialValue);
			if(!force){
				break;
			}
		}while(StringUtils.isBlank(value));
		
		return StringUtils.trimToNull(value);
	}
	
	public int showYesNoConfirmDialog(String message){
		return JOptionPane.showConfirmDialog(getMainFrame()
				, message,"CONFIRM",JOptionPane.YES_NO_OPTION);
	}
	
	public int showYesNoCancelConfirmDialog(String message){
		return JOptionPane.showConfirmDialog(getMainFrame()
				, message,"CONFIRM",JOptionPane.YES_NO_CANCEL_OPTION);
	}
	
	/**
	 * Instantiates the given SwingApplication class, then invokes
	 * {@code #startup} with the given arguments. Typically this method is
	 * called from an application's #main method.
	 * 
	 * @param appClass
	 *            the class of the application to launch
	 * @param args
	 *            optional launch arguments, often the main method's arguments.
	 */
	public static synchronized void launch(
			final Class<? extends Application> appClass,
			final String[] args) {
		Runnable runnable = new Runnable() {
			public void run() {
				Application application = null;
				try {
					application = (Application) appClass.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
					 logger.error("Can't instantiate " +appClass,e);
					return;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					logger.error("Illegal Access during launch of "+ appClass, e);
					return;
				}
				try {
					application.startup(args);
					application.createFrame();
					application.initComponents();
					application.asseembleWindow();
				} catch (Exception e) {
					String message = "Failed to launch " + appClass;
					logger.error(message, e);
					throw new Error(message, e);
				}
			}
		};
		if (EventQueue.isDispatchThread()) {
			runnable.run();
		} else {
			EventQueue.invokeLater(runnable);
		}
	}

	/**
	 * Starts this application when the application is launched. A typical
	 * application creates and shows the GUI in this method. This method runs on
	 * the event dispatching thread.
	 * <p>
	 * 
	 * Called by the static {@code launch} method.
	 * 
	 * @param args
	 *            optional launch arguments, often the main method's arguments.
	 * 
	 * @see #launch(Class, String[])
	 */
	protected abstract void startup(String[] args);
	
	protected abstract void shutdown();
	
	protected abstract JMenuBar buildMenuBar();
	
	protected abstract JComponent buildPanel();
	
	protected JComponent buildStatusBar(){
		return null;
	}
	
	protected abstract void initComponents();
	
	// Default Frame Configuration ********************************************

	protected void createFrame() {
		mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				shutdown();
			}
		});
		mainFrame.setSize(getFrameSize());
		ComponentUtils.showOnScreenCenter(mainFrame);
	}
	
	protected Dimension getFrameSize(){
		return new Dimension(800,600);
	}
	
	/* Add main panel,main menu and main status bar here */
	protected void asseembleWindow(){
		mainPanel = buildPanel();
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(mainPanel,BorderLayout.CENTER);
		mainMenuBar = buildMenuBar();
		mainFrame.setJMenuBar(mainMenuBar);
		mainStatusBar = buildStatusBar();
		if(mainStatusBar!=null){
			mainFrame.getContentPane().add(mainStatusBar,BorderLayout.LINE_END);
		}
	}
	
}
