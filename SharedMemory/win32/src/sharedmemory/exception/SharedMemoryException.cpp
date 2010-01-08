/*
 * SharedMemoryException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/SharedMemoryException.h>

using namespace std;
using namespace cpp::lang;

namespace sharedmemory {
namespace exception {

	SharedMemoryException::SharedMemoryException() : Exception() {

	}

	SharedMemoryException::SharedMemoryException(const string &message) : Exception(message) {

	}

	SharedMemoryException::SharedMemoryException(const string &message, Exception *cause) : Exception(message, cause) {

	}

	SharedMemoryException::~SharedMemoryException() {

	}

	string SharedMemoryException::getClass() const {
		return "sharedmemory::exception::SharedMemoryException";
	}

}
}
