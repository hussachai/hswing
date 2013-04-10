package com.siberhus.hswing.framework;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.jscroll.JScrollDesktopPane;


public abstract class MdiApplication extends Application{
	
	private JScrollDesktopPane desktop;
	
	public void addPage(Page page,String caption){
		pageMap.put(page, caption);
		page.setApplication(this);
	}
	
	@Override
	public JComponent buildPanel() {
		desktop = new JScrollDesktopPane();
		return desktop;
	}
	
	@Override
	public JScrollDesktopPane getMainPanel(){
		return desktop;
	}
	
	public JScrollDesktopPane getDesktop(){
		return desktop;
	}
	
	
	public JMenuItem createPageMenuItem(String text, final MdiPage page, char mnemonic){
		JMenuItem menuItem = new JMenuItem(text);
		menuItem.setMnemonic(mnemonic);
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!page.isVisible()){
					JInternalFrame iframe = desktop
						.add(pageMap.get(page), page.getPanel());
					page.setFrame(iframe);
					iframe.addInternalFrameListener(new InternalFrameAdapter(){
						@Override
						public void internalFrameClosed(InternalFrameEvent evt) {
							super.internalFrameClosed(evt);
							page.setVisible(false);
						}
					});
					if(page.getSize()==null){
						iframe.pack();
					}else{
						iframe.setSize(page.getSize());
					}
					page.setVisible(true);
				}
			}
		});
		return menuItem;
	}
	
}
