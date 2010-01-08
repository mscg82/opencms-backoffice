/*
 * WaitSemaphoreException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef WAITSEMAPHOREEXCEPTION_H_
#define WAITSEMAPHOREEXCEPTION_H_

#include <sharedmemory/exception/SemaphoreException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class WaitSemaphoreException: public SemaphoreException {
	public:
		WaitSemaphoreException();
		WaitSemaphoreException(const string &message);
		WaitSemaphoreException(const string &message, Exception *cause);
		virtual ~WaitSemaphoreException();

		virtual string getClass() const;
	};

}
}

#endif /* WAITSEMAPHOREEXCEPTION_H_ */
