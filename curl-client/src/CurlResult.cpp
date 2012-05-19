/*
 * CurlResult.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "CurlResult.h"

CurlResult::CurlResult(CURLcode code) : message(curl_easy_strerror(code)) {
	this->code = code;
}

CurlResult::CurlResult(const CurlResult& orig) {
	*this = orig;
}

CurlResult::~CurlResult() {

}

CURLcode CurlResult::getCode() const {
	return code;
}

CurlResult& CurlResult::operator=(const CurlResult& orig) {
	this->message = orig.message;
	this->code = orig.code;

	return *this;
}

const string &CurlResult::getMessage() const {
	return message;
}

bool CurlResult::isOkResponse() const {
	return (code == CURLE_OK);
}
