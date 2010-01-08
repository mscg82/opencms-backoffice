/*
 * ArrayIndexOutOfBoundException.cpp
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#include "cpp/lang/ArrayIndexOutOfBoundException.h"
#include <sstream>

namespace cpp {
namespace lang {

ArrayIndexOutOfBoundException::ArrayIndexOutOfBoundException(int index) :
	IndexOutOfBoundException("", NULL){
	init(index, NULL);
}

ArrayIndexOutOfBoundException::ArrayIndexOutOfBoundException(int index, Exception *cause){
	init(index, cause);
}

ArrayIndexOutOfBoundException::~ArrayIndexOutOfBoundException() {

}

void ArrayIndexOutOfBoundException::init(int index, Exception *cause){
	std::ostringstream strbuff("", ios::out);
	strbuff << "Invalid index: " << index;
	setMessage(strbuff.str());

	setCause(cause);
}

string ArrayIndexOutOfBoundException::getClass() const {
	return "cpp::lang::ArrayIndexOutOfBoundException";
}

}
}
