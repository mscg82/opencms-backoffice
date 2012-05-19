/*
 * ConsoleWriteStatisticsHandler.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "ConsoleWriteStatisticsHandler.h"

ConsoleWriteStatisticsHandler::ConsoleWriteStatisticsHandler() {

}

ConsoleWriteStatisticsHandler::~ConsoleWriteStatisticsHandler() {

}

int ConsoleWriteStatisticsHandler::updatedStatistics(CurlClient *client) {
	CurlTransferStatistics stats = client->getStatistics();
	cout << "dl tot: " << stats.getDownloadTotal() << "; dl now: " << stats.getDownloadNow() << "; up tot: " <<
		stats.getUploadTotal() << "; up now: " << stats.getUploadNow() << endl;
	return 0;
}
