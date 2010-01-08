/*
 * MapException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef MAPEXCEPTION_H_
#define MAPEXCEPTION_H_

#include <sharedmemory/exception/SharedMemoryException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class MapException: public SharedMemoryException {
	public:
		MapException();
		MapException(const string &message);
		MapException(const string &message, Exception *cause);
		virtual ~MapException();

		virtual string getClass() const;
	};

}
}

#endif /* MAPEXCEPTION_H_ */
