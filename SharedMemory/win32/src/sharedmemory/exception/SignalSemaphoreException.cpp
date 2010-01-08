/*
 * SignalSemaphoreException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/SignalSemaphoreException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	SignalSemaphoreException::SignalSemaphoreException() : SemaphoreException() {

	}

	SignalSemaphoreException::SignalSemaphoreException(const string &message) : SemaphoreException(message) {

	}

	SignalSemaphoreException::SignalSemaphoreException(const string &message, Exception *cause) : SemaphoreException(message, cause) {

	}

	SignalSemaphoreException::~SignalSemaphoreException() {

	}

	string SignalSemaphoreException::getClass() const {
		return "sharedmemory::exception::SignalSemaphoreException";
	}

}
}
