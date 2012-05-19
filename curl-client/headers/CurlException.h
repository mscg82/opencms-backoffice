/*
 * CurlException.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CURLEXCEPTION_H_
#define CURLEXCEPTION_H_

#include "Exception.h"

using namespace std;

class CurlException : public Exception {

public:
	CurlException();
	CurlException(const string& message);
	CurlException(const string& message, Exception *cause);
	virtual ~CurlException();
};

#endif /* CURLEXCEPTION_H_ */
