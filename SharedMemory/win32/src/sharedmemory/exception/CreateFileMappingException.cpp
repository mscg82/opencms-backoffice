/*
 * CreateFileMappingException.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/exception/CreateFileMappingException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	CreateFileMappingException::CreateFileMappingException() : MapException() {

	}

	CreateFileMappingException::CreateFileMappingException(const string &message) : MapException(message) {

	}

	CreateFileMappingException::CreateFileMappingException(const string &message, Exception *cause) : MapException(message, cause) {

	}

	CreateFileMappingException::~CreateFileMappingException() {

	}

	string CreateFileMappingException::getClass() const {
		return "sharedmemory::exception::CreateFileMappingException";
	}

}
}
