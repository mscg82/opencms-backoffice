/*
 * CurlWriteHandler.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CURLWRITEHANDLER_H_
#define CURLWRITEHANDLER_H_

#include <curl/curl.h>
#include "CurlClient.h"

class CurlClient;

class AbstractCurlWriteHandler {
public:
	AbstractCurlWriteHandler();
	virtual ~AbstractCurlWriteHandler();

	virtual size_t writeData(void *buffer, size_t size, size_t nmemb, CurlClient *client) = 0;
};

#endif /* CURLWRITEHANDLER_H_ */
