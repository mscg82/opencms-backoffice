/*
 * SignalSemaphoreException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef SIGNALSEMAPHOREEXCEPTION_H_
#define SIGNALSEMAPHOREEXCEPTION_H_

#include <sharedmemory/exception/SemaphoreException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class SignalSemaphoreException: public SemaphoreException {
	public:
		SignalSemaphoreException();
		SignalSemaphoreException(const string &message);
		SignalSemaphoreException(const string &message, Exception *cause);
		virtual ~SignalSemaphoreException();

		virtual string getClass() const;
	};

}
}

#endif /* SIGNALSEMAPHOREEXCEPTION_H_ */
