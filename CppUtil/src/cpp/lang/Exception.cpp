/*
 * Exception.cpp
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#include "cpp/lang/Exception.h"

namespace cpp {
namespace lang {

Exception::Exception(){
	string *noMessage = new string("");
	init(*noMessage, NULL);
}

Exception::Exception(const string &message){
	init(message, NULL);
}

Exception::Exception(const string &message, Exception *cause){
	init(message, cause);
}

Exception::~Exception(){
	delete message;
	delete cause;
}

string Exception::getMessage() const{
	return *message;
}

Exception* Exception::getCause() const{
	return cause;
}

void Exception::setMessage(const string &message){
	this->message = new string(message);
}

void Exception::setCause(Exception *cause){
	this->cause = cause;
}

void Exception::init(const string &message, Exception *cause){
	setMessage(message);
	setCause(cause);
}

}
}
