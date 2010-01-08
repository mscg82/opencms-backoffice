/*
 * NullPointerException.cpp
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#include "cpp/lang/NullPointerException.h"

namespace cpp {
namespace lang {

NullPointerException::NullPointerException() :
	Exception("null") {

}

NullPointerException::NullPointerException(Exception *cause) :
	Exception("null", cause) {

}

NullPointerException::~NullPointerException() {

}

string NullPointerException::getClass() const {
	return "cpp::lang::NullPointerException";
}

}
}
