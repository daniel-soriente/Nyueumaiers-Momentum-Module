import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTick;
import org.jfree.chart.axis.Tick;
import org.jfree.ui.RectangleEdge;


public class TmxDateAxis extends DateAxis {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	   @Override
	   protected List refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
	      List ticks = super.refreshTicksHorizontal(g2, dataArea, edge);
	      List ret = new ArrayList();
	      for (Tick tick : (List<Tick>)ticks) {
	         if (tick instanceof DateTick) {
	            DateTick dateTick = (DateTick)tick;
	            ret.add(new DateTick(dateTick.getDate(), dateTick.getText(), dateTick.getTextAnchor(), dateTick.getRotationAnchor(), getLabelAngle()));
	         } else {
	            ret.add(tick);
	         }
	      }
	      return ret;
	   }
}
