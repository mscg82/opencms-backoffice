/*
 * CreateSemaphoreException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/CreateSemaphoreException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	CreateSemaphoreException::CreateSemaphoreException() : SemaphoreException() {

	}

	CreateSemaphoreException::CreateSemaphoreException(const string &message) : SemaphoreException(message) {

	}

	CreateSemaphoreException::CreateSemaphoreException(const string &message, Exception *cause) : SemaphoreException(message, cause) {

	}

	CreateSemaphoreException::~CreateSemaphoreException() {

	}

	string CreateSemaphoreException::getClass() const {
		return "sharedmemory::exception::CreateSemaphoreException";
	}

}
}
