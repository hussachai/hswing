package com.siberhus.hswing.util;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

/**
 *
 * @author Hussachai
 */
public class ComponentUtils {
	
	static final double screenWidth;
	static final double screenHeight;
	
	static{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = dimension.getWidth();
		screenHeight = dimension.getHeight();
	}
	
	public static void showOnScreenCenter(Component window ){
		double compWidth = window.getWidth();
		double compHeight = window.getHeight();
		int xPos = (int)((screenWidth/2)-(compWidth/2));
		int yPos = (int)((screenHeight/2)-(compHeight/2));
		window.setLocation(xPos, yPos);
		window.setVisible(true);
	}
	
	public static void setButtonGroupEnabled(ButtonGroup buttonGroup , boolean enabled){
		Enumeration<AbstractButton> buttonEnum = buttonGroup.getElements();
		while(buttonEnum.hasMoreElements()){
			buttonEnum.nextElement().setEnabled(enabled);
		}
	}
	
	public static Container findContainer(Component thisComp, Class<? extends Container> containerClass){
		if(thisComp==null){
			return null;
		}
		Container parent = thisComp.getParent();
		do{
			parent = parent.getParent();
			if(parent!=null && containerClass.isAssignableFrom(parent.getClass())){
				return parent;
			}
		}while(parent!=null);
		return null;
	}
	
	public static Window findWindow(Component comp){
		return (Window)findContainer(comp, Window.class);
	}
	
	public static Frame findFrame(Component comp){
		return (Frame)findContainer(comp, Frame.class);
	}
	
	public static Dialog findDialog(Component comp){
		return (Dialog)findContainer(comp, Dialog.class);
	}
	
	public static JWindow findJWindow(Component comp){
		return (JWindow)findContainer(comp, JWindow.class);
	}
	
	public static JFrame findJFrame(Component comp){
		return (JFrame)findContainer(comp, JFrame.class);
	}
	
	public static JDialog findJDialog(Component comp){
		return (JDialog)findContainer(comp, JDialog.class);
	}
	
	public static void main(String args[]) throws Exception {
		
		JFrame f = new JFrame();
		JLabel l1 = new JLabel();
		f.getContentPane().add(l1);
		System.out.println(findDialog(l1));
	}
}
