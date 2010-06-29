/**
 * 
 */
package it.virgilio.guidatv.item;

import it.virgilio.guidatv.programs.TVProgram;
import it.virgilio.guidatv.theme.VirgilioTheme;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.j4me.ui.Theme;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.Label;

/**
 * This class paints TV program informations.
 * 
 * @author Giuseppe Miscione
 *
 */
public class ProgramItem extends Component {
	
	private static final int HEADER_PADDING = 2;
	private static final int PROGRAM_PADDING = 3;
	
	private TVProgram program;
	private Label programName;
	
	public ProgramItem(TVProgram program) {
		this.program = program;
		programName = new Label(program.getName());
		programName.setHorizontalAlignment(Graphics.HCENTER);
	}

	protected int[] getPreferredComponentSize(Theme theme, int viewportWidth, int viewportHeight) {
		VirgilioTheme vt = (VirgilioTheme) theme;
		
		int fontHeight = vt.getFont().getHeight();
		int height = vt.getProgramInfoMarginTop() + vt.getProgramInfoMarginBottom();
		
		// add header height
		height += fontHeight + 2 * HEADER_PADDING;
		
		// add program info height
		height +=
			programName.getPreferredSize(theme, viewportWidth - 2 * PROGRAM_PADDING, viewportHeight)[1] +
			2 * PROGRAM_PADDING;
		
		return new int[] { viewportWidth, height };
	}

	protected void paintComponent(Graphics g, Theme theme, int width, int height, boolean selected) {
		VirgilioTheme vt = (VirgilioTheme) theme;
		
		Font font = theme.getFont();
		g.setFont(font);
		int fontHeight = font.getHeight();

		int marginSum = vt.getProgramInfoMarginTop() + vt.getProgramInfoMarginBottom();
		int offsetTop = vt.getProgramInfoMarginTop();
		int timeBoxHeight = fontHeight + 2 * HEADER_PADDING;
		int programInfoOffset = offsetTop + timeBoxHeight;
		// draw a border around the whole element
		int rounding = Math.min( height / 4, 8 );
		g.setColor(vt.getProgramInfoBackgroundColor());
		g.fillRoundRect(0, offsetTop,
				width - 1, height - marginSum - 1, rounding, rounding);
		g.setColor(vt.getProgramInfoBorderColor());
		g.drawRoundRect(0, offsetTop,
			width - 1, height - marginSum - 1,
			rounding, rounding);
		
		// draw the border on the left top half of the element
		g.setColor(vt.getProgramStartTimeBackgroundColor());
		g.fillRoundRect(0, offsetTop,
				width / 2 - 1, timeBoxHeight - 1, rounding, rounding);
		g.setColor(vt.getProgramTimeBorderColor());
		g.drawRoundRect(0, offsetTop,
			width / 2 - 1, timeBoxHeight - 1,
			rounding, rounding);	
			
		// draw the border on the rigth top half of the element
		g.setColor(vt.getProgramEndTimeBackgroundColor());
		g.fillRoundRect(width / 2, offsetTop,
				width / 2 - 1, timeBoxHeight - 1, rounding, rounding);
		g.setColor(vt.getProgramTimeBorderColor());
		g.drawRoundRect(width / 2, offsetTop,
			width / 2 - 1, timeBoxHeight - 1,
			rounding, rounding);
				
		
		// draw start time in the left top half of the element
		g.setColor(vt.getProgramStartTimeFontColor());
		g.drawString(program.getStartTime(), width / 4, offsetTop + HEADER_PADDING, Graphics.TOP|Graphics.HCENTER);
		// draw end time in the rigth top half of the element
		g.setColor(vt.getProgramEndTimeFontColor());
		g.drawString(program.getEndTime(), width * 3 / 4, offsetTop + HEADER_PADDING, Graphics.TOP|Graphics.HCENTER);
		
		int labelHeight = height - programInfoOffset - 2 * PROGRAM_PADDING - vt.getProgramInfoMarginBottom();
		g.setColor(vt.getFontColor());
		programName.visible(true);
		programName.paint(g, theme, getScreen(), PROGRAM_PADDING, programInfoOffset + PROGRAM_PADDING,
			width - PROGRAM_PADDING, labelHeight,
			selected);
	}

}
