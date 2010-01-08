/*
 * MapException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/MapException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	MapException::MapException() : SharedMemoryException() {

	}

	MapException::MapException(const string &message) : SharedMemoryException(message) {

	}

	MapException::MapException(const string &message, Exception *cause) : SharedMemoryException(message, cause) {

	}

	MapException::~MapException() {

	}

	string MapException::getClass() const {
		return "sharedmemory::exception::MapException";
	}

}
}
