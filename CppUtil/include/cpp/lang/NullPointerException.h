/*
 * NullPointerException.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef NULLPOINTEREXCEPTION_H_
#define NULLPOINTEREXCEPTION_H_

#include "Exception.h"

namespace cpp {
namespace lang {

class NullPointerException: public Exception {
public:
	NullPointerException();
	NullPointerException(Exception *cause);
	virtual ~NullPointerException();

	virtual string getClass() const;
};

}
}

#endif /* NULLPOINTEREXCEPTION_H_ */
