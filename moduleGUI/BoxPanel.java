import java.awt.Color;
import java.awt.Dimension;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.border.DropShadowBorder;

public class BoxPanel extends JXPanel{

	private static final long serialVersionUID = 1L;

	public BoxPanel(Dimension d){
		
		
		this.setSize(d);
		//this.setLocation(location);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		
		DropShadowBorder shadow = new DropShadowBorder();
        shadow.setShadowColor(new Color(204,255,204));
        shadow.setShadowOpacity((float) 0.35);
        shadow.setShowLeftShadow(true);
        shadow.setShowRightShadow(true);
        shadow.setShowBottomShadow(true);
        shadow.setShowTopShadow(true);
        this.setBorder(shadow);
	}

}

