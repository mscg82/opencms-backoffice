/*
 * CreateFileMappingException.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef CREATEFILEMAPPINGEXCEPTION_H_
#define CREATEFILEMAPPINGEXCEPTION_H_

#include <sharedmemory/exception/MapException.h>

using namespace sharedmemory::exception;

namespace sharedmemory {
namespace exception {

	class CreateFileMappingException: public MapException {
	public:
		CreateFileMappingException();
		CreateFileMappingException(const string &message);
		CreateFileMappingException(const string &message, Exception *cause);
		virtual ~CreateFileMappingException();

		virtual string getClass() const;
	};

}
}

#endif /* CREATEFILEMAPPINGEXCEPTION_H_ */
