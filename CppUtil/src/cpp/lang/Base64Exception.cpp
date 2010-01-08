/*
 * Base64Exception.cpp
 *
 *  Created on: 22/ago/2009
 *      Author: Giuseppe Miscione
 */

#include <cpp/lang/Base64Exception.h>

using namespace std;

namespace cpp {
namespace lang {

	Base64Exception::Base64Exception() : Exception() {

	}

	Base64Exception::Base64Exception(const string &message) : Exception (message){

	}

	Base64Exception::Base64Exception(const string &message, Exception *cause) : Exception(message, cause){

	}

	Base64Exception::~Base64Exception() {

	}

}
}
