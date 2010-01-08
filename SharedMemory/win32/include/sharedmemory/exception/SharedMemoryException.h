/*
 * SharedMemoryException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef SHAREDMEMORYEXCEPTION_H_
#define SHAREDMEMORYEXCEPTION_H_

#include <cpp/lang/Exception.h>

using namespace std;
using namespace cpp::lang;

namespace sharedmemory {
namespace exception {

	class SharedMemoryException: public Exception {
	public:
		SharedMemoryException();
		SharedMemoryException(const string &message);
		SharedMemoryException(const string &message, Exception *cause);
		virtual ~SharedMemoryException();

		virtual string getClass() const;
	};

}
}

#endif /* SHAREDMEMORYEXCEPTION_H_ */
