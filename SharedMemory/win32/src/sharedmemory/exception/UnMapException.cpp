/*
 * UnMapException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/UnMapException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	UnMapException::UnMapException() : SharedMemoryException() {

	}

	UnMapException::UnMapException(const string &message) : SharedMemoryException(message) {

	}

	UnMapException::UnMapException(const string &message, Exception *cause) : SharedMemoryException(message, cause) {

	}

	UnMapException::~UnMapException() {

	}

	string UnMapException::getClass() const {
		return "sharedmemory::exception::UnMapException";
	}

}
}
