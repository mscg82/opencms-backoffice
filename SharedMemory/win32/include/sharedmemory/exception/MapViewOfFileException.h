/*
 * MapViewOfFileException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef MAPVIEWOFFILEEXCEPTION_H_
#define MAPVIEWOFFILEEXCEPTION_H_

#include <sharedmemory/exception/MapException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class MapViewOfFileException: public MapException {
	public:
		MapViewOfFileException();
		MapViewOfFileException(const string &message);
		MapViewOfFileException(const string &message, Exception *cause);
		virtual ~MapViewOfFileException();

		virtual string getClass() const;
	};

}
}

#endif /* MAPVIEWOFFILEEXCEPTION_H_ */
