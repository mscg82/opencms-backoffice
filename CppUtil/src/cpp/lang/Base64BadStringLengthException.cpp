/*
 * Base64BadBufferLength.cpp
 *
 *  Created on: 22/ago/2009
 *      Author: Giuseppe Miscione
 */

#include <cpp/lang/Base64BadStringLengthException.h>

namespace cpp {
namespace lang {

	const string Base64BadStringLengthException::exceptionMessage =
			"The length of the supplied string doesn't match to one of a Base64 string.";

	Base64BadStringLengthException::Base64BadStringLengthException() :
		Base64Exception(Base64BadStringLengthException::exceptionMessage) {

	}

	Base64BadStringLengthException::Base64BadStringLengthException(Exception *cause) :
		Base64Exception(Base64BadStringLengthException::exceptionMessage, cause) {

	}

	Base64BadStringLengthException::~Base64BadStringLengthException() {

	}

}
}
