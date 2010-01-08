/*
 * SemaphoreException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/SemaphoreException.h>

using namespace std;
using namespace cpp::lang;

namespace sharedmemory {
namespace exception {

	SemaphoreException::SemaphoreException() : Exception() {

	}

	SemaphoreException::SemaphoreException(const string &message) : Exception(message) {

	}

	SemaphoreException::SemaphoreException(const string &message, Exception *cause) : Exception(message, cause) {

	}

	SemaphoreException::~SemaphoreException() {

	}

	string SemaphoreException::getClass() const {
		return "sharedmemory::exception::SemaphoreException";
	}

}
}
