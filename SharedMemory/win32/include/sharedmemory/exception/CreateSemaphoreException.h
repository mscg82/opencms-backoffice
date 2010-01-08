/*
 * CreateSemaphoreException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef CREATESEMAPHOREEXCEPTION_H_
#define CREATESEMAPHOREEXCEPTION_H_

#include <sharedmemory/exception/SemaphoreException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class CreateSemaphoreException: public SemaphoreException {
	public:
		CreateSemaphoreException();
		CreateSemaphoreException(const string &message);
		CreateSemaphoreException(const string &message, Exception *cause);
		virtual ~CreateSemaphoreException();

		virtual string getClass() const;
	};

}
}

#endif /* CREATESEMAPHOREEXCEPTION_H_ */
