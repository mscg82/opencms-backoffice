/*
 * MapViewOfFileException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/MapViewOfFileException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	MapViewOfFileException::MapViewOfFileException() : MapException() {

	}

	MapViewOfFileException::MapViewOfFileException(const string &message) : MapException(message) {

	}

	MapViewOfFileException::MapViewOfFileException(const string &message, Exception *cause) : MapException(message, cause) {

	}

	MapViewOfFileException::~MapViewOfFileException() {

	}

	string MapViewOfFileException::getClass() const {
		return "sharedmemory::exception::MapViewOfFileException";
	}

}
}
