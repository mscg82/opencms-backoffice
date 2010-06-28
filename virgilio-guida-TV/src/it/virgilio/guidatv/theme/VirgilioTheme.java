/**
 * 
 */
package it.virgilio.guidatv.theme;

import org.j4me.ui.Theme;

/**
 * @author Giuseppe Miscione
 *
 */
public class VirgilioTheme extends Theme {

	public VirgilioTheme() {
		super();
	}
	
	public String getFloatSeparator() {
		return ",";
	}
	
	public String getMainMenuTitle() {
		return "Virgilio Guida TV";
	}
	
	public String getDaySelectionTitle() {
		return "Seleziona un giorno";
	}
	
	public String getChannelSelectionTitle() {
		return "${day} / ${month} - Canali";
	}

	public String getMenuTextForCancel() {
		return "Indietro";
	}

	public String getMenuTextForExit() {
		return "Esci";
	}

	public String getMenuTextForOK() {
		return "OK";
	}
	
	public String getTextForEmptyPrograms() {
		return "Nessun programma presente per il canale selezionato";
	}
	
	public String getProgramsLoadingText() {
		return "Caricamento del palinsesto in corso...";
	}
	
	public String getElaboratingDataText() {
		return "Elaborazione informazioni in corso...\n${perc} %\n ";
	}
	
	public String getUpdateErrorText() {
		return "Si è verificato un errore durante l'aggiornamento del palinsesto.";
	}

	public int getProgramInfoBorderColor() {
		return 0x00909090;
	}
	
	public int getProgramInfoBackgroundColor() {
		return LIGHT_GRAY;
	}
	
	public int getProgramInfoMarginTop() {
		return 2;
	}
	
	public int getProgramInfoMarginBottom() {
		return 2;
	}

	public int getProgramTimeBorderColor() {
		return NAVY;
	}
	
	public int getProgramStartTimeBackgroundColor() {
		return LIGHT_BLUE;
	}

	public int getProgramStartTimeFontColor() {
		return WHITE;
	}
	
	public int getProgramEndTimeBackgroundColor() {
		return WHITE;
	}

	public int getProgramEndTimeFontColor() {
		return BLACK;
	}
}
