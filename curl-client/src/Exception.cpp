/*
 * Exception.cpp
 *
 *  Created on: 19/mag/2012
 *      Author: Giuseppe Miscione
 */

#include "Exception.h"

Exception::Exception() {

}

Exception::Exception(const string& message) : message(message)
{
	this->cause = NULL;
}
Exception::Exception(const string& message, Exception *exception) : message(message)
{
	this->cause = exception;
}

Exception::~Exception() {
	if(cause != NULL)
		delete cause;
}

const string& Exception::getMessage() const {
	return message;
}

const Exception* Exception::getCause() const {
	return cause;
}
