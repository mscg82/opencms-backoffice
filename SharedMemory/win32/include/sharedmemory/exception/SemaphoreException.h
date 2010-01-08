/*
 * SemaphoreException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef SEMAPHOREEXCEPTION_H_
#define SEMAPHOREEXCEPTION_H_

#include <cpp/lang/Exception.h>

using namespace cpp::lang;

namespace sharedmemory {
namespace exception {

	class SemaphoreException: public Exception {
	public:
		SemaphoreException();
		SemaphoreException(const string &message);
		SemaphoreException(const string &message, Exception *cause);
		virtual ~SemaphoreException();

		virtual string getClass() const;
	};

}
}

#endif /* SEMAPHOREEXCEPTION_H_ */
