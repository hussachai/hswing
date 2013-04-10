package com.siberhus.hswing.framework;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;


public abstract class MdiPage extends AbstractPage{

	private JInternalFrame frame;
	
	public JInternalFrame getFrame() {
		
		return frame;
	}
	
	public void setFrame(JInternalFrame frame) {
		this.frame = frame;
		/* Fix Vista issue when mode is JDesktopPane.OUTLINE_DRAG_MODE the frame will render very slow*/
		frame.getDesktopPane().setDragMode(JDesktopPane.LIVE_DRAG_MODE);
		/* End */
	}
	
	@Override
	public MdiApplication getApplication() {
		return (MdiApplication)super.getApplication();
	}
	
	
}
