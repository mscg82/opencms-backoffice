/*
 * WindowsConsoleWriterStatisticsHandler.cpp
 *
 *  Created on: 20/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "WindowsConsoleWriterStatisticsHandler.h"

WindowsConsoleWriterStatisticsHandler::WindowsConsoleWriterStatisticsHandler(const string &prefix) {
	cleanConsole = AllocConsole();
	consoleOut = GetStdHandle(STD_OUTPUT_HANDLE);
	first = true;
	DWORD bytesWritten;
	WriteFile(consoleOut, prefix.c_str(), prefix.size(), &bytesWritten, NULL);
}

WindowsConsoleWriterStatisticsHandler::~WindowsConsoleWriterStatisticsHandler() {
	CloseHandle(consoleOut);
	if(cleanConsole)
		FreeConsole();
}

int WindowsConsoleWriterStatisticsHandler::updatedStatistics(CurlClient *client) {
	if(!first) {
		// print backspace chars to delete the percentage
		DWORD bytesWritten;
		WriteFile(consoleOut, "\b\b\b\b\b\b\b\b", 8, &bytesWritten, NULL);
	}
	first = false;
	const CurlTransferStatistics stats = client->getStatistics();
    char outMsg[512];
    double perc = (stats.getDownloadNow() / stats.getDownloadTotal()) * 100.0;
    if(isnan(perc) || isinf(perc))
    	perc = 0.0;
    sprintf(outMsg, "%6.2f %%", perc);
    string outMsgStr(outMsg);
    DWORD bytesWritten;
    WriteFile(consoleOut, outMsgStr.c_str(), outMsgStr.size(), &bytesWritten, NULL);

    return 0;
}
