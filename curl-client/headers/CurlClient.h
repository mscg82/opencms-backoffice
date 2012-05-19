/*
 * CurlClient.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CURLCLIENT_H_
#define CURLCLIENT_H_

#include <curl/curl.h>
#include <string>
#include "CurlException.h"
#include "CurlResult.h"
#include "AbstractCurlWriteHandler.h"
#include "AbstractTransferStatisticHandler.h"
#include "CurlTransferStatistics.h"

using namespace std;

class AbstractCurlWriteHandler;
class AbstractTransferStatisticHandler;

class CurlClient {
private:
	CURL* curl;
	string *url;
	AbstractCurlWriteHandler *writeHandler;
	AbstractTransferStatisticHandler *statisticsHandler;
	CurlTransferStatistics statistics;

	void cleanUrl();

	static size_t writeData(void *buffer, size_t size, size_t nmemb, void *info);
	static int transferProgress(void *info, double dltotal, double dlnow, double ultotal, double ulnow);

public:
	CurlClient(bool disableSSLChecks = true);
	virtual ~CurlClient();

	CURL* getCurlHandle() const;

	const string& getUrl() const;
	void setUrl(const string& url);

	CurlResult executeCall();

	AbstractCurlWriteHandler *getWriteHandler() const;
    void setWriteHandler(AbstractCurlWriteHandler *writeHandler);
    void cleanWriteHandler();

    AbstractTransferStatisticHandler *getStatisticsHandler() const;
    void setStatisticsHandler(AbstractTransferStatisticHandler *statisticsHandler);
    void cleanStatisticsHandler();

    const CurlTransferStatistics& getStatistics() const;
};

#endif /* CURLCLIENT_H_ */
