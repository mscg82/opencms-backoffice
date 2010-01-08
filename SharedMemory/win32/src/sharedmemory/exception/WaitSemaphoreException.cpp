/*
 * WaitSemaphoreException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/WaitSemaphoreException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	WaitSemaphoreException::WaitSemaphoreException() : SemaphoreException() {

	}

	WaitSemaphoreException::WaitSemaphoreException(const string &message) : SemaphoreException(message) {

	}

	WaitSemaphoreException::WaitSemaphoreException(const string &message, Exception *cause) : SemaphoreException(message, cause) {

	}

	WaitSemaphoreException::~WaitSemaphoreException() {

	}

	string WaitSemaphoreException::getClass() const {
		return "sharedmemory::exception::WaitSemaphoreException";
	}

}
}
