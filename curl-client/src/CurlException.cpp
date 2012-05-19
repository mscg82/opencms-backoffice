/*
 * CurlException.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "CurlException.h"

CurlException::CurlException() : Exception()
{

}

CurlException::CurlException(const string& message) : Exception(message)
{

}

CurlException::CurlException(const string& message, Exception *cause) : Exception(message, cause)
{

}

CurlException::~CurlException() {

}
