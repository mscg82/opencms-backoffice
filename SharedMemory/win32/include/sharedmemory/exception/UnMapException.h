/*
 * UnMapException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef UNMAPEXCEPTION_H_
#define UNMAPEXCEPTION_H_

#include <sharedmemory/exception/SharedMemoryException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class UnMapException: public SharedMemoryException {
	public:
		UnMapException();
		UnMapException(const string &message);
		UnMapException(const string &message, Exception *cause);
		virtual ~UnMapException();

		virtual string getClass() const;
	};

}
}

#endif /* UNMAPEXCEPTION_H_ */
