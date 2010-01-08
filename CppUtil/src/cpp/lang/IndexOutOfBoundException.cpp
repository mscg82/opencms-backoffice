/*
 * IndexOutOfBoundException.cpp
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#include "cpp/lang/IndexOutOfBoundException.h"

namespace cpp {
namespace lang {

IndexOutOfBoundException::IndexOutOfBoundException() :
	Exception() {

}

IndexOutOfBoundException::IndexOutOfBoundException(const string &message) :
	Exception(message) {

}

IndexOutOfBoundException::IndexOutOfBoundException(const string &message, Exception *cause) :
	Exception(message, cause) {

}

IndexOutOfBoundException::~IndexOutOfBoundException() {

}

}
}
