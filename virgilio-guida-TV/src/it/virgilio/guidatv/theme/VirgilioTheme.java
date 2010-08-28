/**
 * 
 */
package it.virgilio.guidatv.theme;

import org.j4me.ext.PopupMenuTheme;
import org.j4me.ui.Theme;

/**
 * @author Giuseppe Miscione
 *
 */
public class VirgilioTheme extends Theme implements PopupMenuTheme {
	
	private static final int[] margins = {2, 3, 2, 3};

	public VirgilioTheme() {
		super();
	}
	
	public String getCacheStatusTitle() {
		return "Stato cache";
	}
	
	public String getChannelSelectionTitle() {
		return "${day} / ${month} - Canali";
	}
	
	public String getDaySelectionTitle() {
		return "Seleziona un giorno";
	}
	
	public String getDeleteItemsInDiskCache() {
		return "Rimuovi da scheda";
	}

	public String getDeleteItemsInMemoryCache() {
		return "Rimuovi da memoria";
	}

	public String getElaboratingDataText() {
		return "Elaborazione informazioni in corso...\n${perc} %\n ";
	}

	public String getFloatSeparator() {
		return ",";
	}
	
	public int[] getItemMargins() {
		return margins;
	}
	
	public String getItemsInDiskCache() {
		return "Su scheda: ${count}";
	}
	
	public String getItemsInMemoryCache() {
		return "In memoria: ${count}";
	}
	
	public String getLeftSoftButtonText() {
		return "Menu";
	}

	public String getMainMenuTitle() {
		return "Virgilio Guida TV";
	}
	
	public String getMemoryStatusFreeMemory() {
		return "Memoria libera: ${mem} MB";
	}
	
	public String getMemoryStatusFreeTextAction() {
		return "Pulisci";
	}
	
	public String getMemoryStatusTitle() {
		return "Stato della memoria";
	}

	public String getMemoryStatusTotalMemory() {
		return "Memoria totale: ${mem} MB";
	}
	
	public int getMenuBackgroundAlpha() {
		return PopupMenuTheme.DEFAULT_BACKGROUND_ALPHA;
	}

	public int getMenuBackgroundColor() {
		return PopupMenuTheme.DEFAULT_BACKGROUND_COLOR;
	}
	
	public int[] getMenuBorderColors() {
		return PopupMenuTheme.DEFAULT_BORDER_COLORS;
	}

	public int[] getMenuBorders() {
		return PopupMenuTheme.DEFAULT_MENUBORDERS;
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

	public String getMinWidth() {
		return "50%";
	}
	
	public int getPopupMenuFontColor() {
		return Theme.LIGHT_BLUE;
	}
	
	public int getProgramEndTimeBackgroundColor() {
		return WHITE;
	}
	
	public int getProgramEndTimeFontColor() {
		return BLACK;
	}
	
	public int getProgramInfoBackgroundColor() {
		return LIGHT_GRAY;
	}
	
	public int getProgramInfoBorderColor() {
		return 0x00909090;
	}

	public int getProgramInfoMarginBottom() {
		return 2;
	}

	public int getProgramInfoMarginTop() {
		return 0;
	}

	public String getProgramsLoadingText() {
		return "Caricamento del palinsesto in corso...";
	}

	public int getProgramStartTimeBackgroundColor() {
		return LIGHT_BLUE;
	}

	public int getProgramStartTimeFontColor() {
		return WHITE;
	}

	public int getProgramTimeBorderColor() {
		return NAVY;
	}

	public int getSelectionBackgroundColor() {
		return PopupMenuTheme.DEFAULT_SELECTED_BACKGROUND_COLOR;
	}

	public int getSelectionForegroundColor() {
		return PopupMenuTheme.DEFAULT_SELECTED_FOREGROUND_COLOR;
	}
	
	public String getTextForEmptyPrograms() {
		return "Nessun programma presente per il canale selezionato";
	}

	public String getUpdateErrorText() {
		return "Si è verificato un errore durante l'aggiornamento del palinsesto.";
	}
}
