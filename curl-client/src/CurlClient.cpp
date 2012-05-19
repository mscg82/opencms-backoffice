/*
 * CurlClient.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "CurlClient.h"

CurlClient::CurlClient(bool disableSSLChecks) : statistics() {
	url = NULL;
	writeHandler = NULL;
	statisticsHandler = NULL;

	curl = curl_easy_init();
	if(curl == NULL)
		throw CurlException("Cannot initialize curl object");

	curl_easy_setopt(curl, CURLOPT_WRITEDATA, this);
	curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, CurlClient::writeData);
	curl_easy_setopt(curl, CURLOPT_PROGRESSDATA, this);
	curl_easy_setopt(curl, CURLOPT_PROGRESSFUNCTION, CurlClient::transferProgress);
	curl_easy_setopt(curl, CURLOPT_NOPROGRESS, 0);
	if(disableSSLChecks) {
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYPEER, 0L);
		curl_easy_setopt(curl, CURLOPT_SSL_VERIFYHOST, 0L);
	}
}

CurlClient::~CurlClient() {
	cleanUrl();

	if(curl != NULL)
		curl_easy_cleanup(curl);
}

void CurlClient::cleanUrl() {
	if(url != NULL) {
		delete url;
		url = NULL;
	}
}

CURL* CurlClient::getCurlHandle() const {
	return this->curl;
}

const string& CurlClient::getUrl() const {
	return *url;
}

void CurlClient::setUrl(const string& url) {
	cleanUrl();
	this->url = new string(url);
	curl_easy_setopt(curl, CURLOPT_URL, this->url->c_str());
}

CurlResult CurlClient::executeCall() {
	statistics.reset();
	CURLcode res = curl_easy_perform(curl);
	return CurlResult(res);
}

AbstractCurlWriteHandler *CurlClient::getWriteHandler() const {
	return writeHandler;
}

void CurlClient::setWriteHandler(AbstractCurlWriteHandler *writeHandler) {
	cleanWriteHandler();
	this->writeHandler = writeHandler;
}

void CurlClient::cleanWriteHandler() {
	if(writeHandler != NULL) {
		delete writeHandler;
		writeHandler = NULL;
	}
}

AbstractTransferStatisticHandler *CurlClient::getStatisticsHandler() const {
	return statisticsHandler;
}

void CurlClient::setStatisticsHandler(AbstractTransferStatisticHandler *statisticsHandler) {
	cleanStatisticsHandler();
	this->statisticsHandler = statisticsHandler;
}

void CurlClient::cleanStatisticsHandler() {
	if(statisticsHandler != NULL) {
		delete statisticsHandler;
		statisticsHandler = NULL;
	}
}

const CurlTransferStatistics& CurlClient::getStatistics() const {
	return statistics;
}

int CurlClient::transferProgress(void *info, double dltotal, double dlnow, double ultotal, double ulnow) {
	CurlClient *client = (CurlClient*) info;
	client->statistics.set(dltotal, dlnow, ultotal, ulnow);

	if(client->getStatisticsHandler() != NULL)
		return client->getStatisticsHandler()->updatedStatistics(client);

	return 0;
}

size_t CurlClient::writeData(void *buffer, size_t size, size_t nmemb, void *info) {
	CurlClient *client = (CurlClient*) info;

	if(client->getWriteHandler() != NULL)
		return client->getWriteHandler()->writeData(buffer, size, nmemb, client);

	return 0;
}
