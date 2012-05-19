/*
 * ConsoleWriteStatisticsHandler.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CONSOLEWRITESTATISTICSHANDLER_H_
#define CONSOLEWRITESTATISTICSHANDLER_H_

#include "AbstractTransferStatisticHandler.h"
#include <iostream>

using namespace std;

class ConsoleWriteStatisticsHandler : public AbstractTransferStatisticHandler {
public:
	ConsoleWriteStatisticsHandler();
	virtual ~ConsoleWriteStatisticsHandler();

	virtual int updatedStatistics(CurlClient *client);
};

#endif /* CONSOLEWRITESTATISTICSHANDLER_H_ */
