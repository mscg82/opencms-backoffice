/*
 * CurlResult.h
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#ifndef CURLRESULT_H_
#define CURLRESULT_H_

#include <curl/curl.h>
#include <string>
using namespace std;

class CurlResult {
private:
	string message;
	CURLcode code;

public:
	CurlResult(CURLcode code);
	CurlResult(const CurlResult& orig);
	virtual ~CurlResult();

	CurlResult& operator=(const CurlResult& orig);

	CURLcode getCode() const;
    const string &getMessage() const;
    bool isOkResponse() const;

};

#endif /* CURLRESULT_H_ */
