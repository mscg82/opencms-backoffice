/*
 * WindowsConsoleWriterStatisticsHandler.h
 *
 *  Created on: 20/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef WINDOWSCONSOLEWRITERSTATISTICSHANDLER_H_
#define WINDOWSCONSOLEWRITERSTATISTICSHANDLER_H_

#include <windows.h>
#include <string>
#include <cmath>
#include <AbstractTransferStatisticHandler.h>

using namespace std;

class WindowsConsoleWriterStatisticsHandler : public AbstractTransferStatisticHandler {
private:
	HANDLE consoleOut;
	bool cleanConsole;
	bool first;
public:
	WindowsConsoleWriterStatisticsHandler(const string &prefix);
	virtual ~WindowsConsoleWriterStatisticsHandler();

	virtual int updatedStatistics(CurlClient *client);
};

#endif /* WINDOWSCONSOLEWRITERSTATISTICSHANDLER_H_ */
