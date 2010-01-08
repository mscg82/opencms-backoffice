/*
 * ArrayIndexOutOfBoundException.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef ARRAYINDEXOUTOFBOUNDEXCEPTION_H_
#define ARRAYINDEXOUTOFBOUNDEXCEPTION_H_

#include "cpp/lang/IndexOutOfBoundException.h"

namespace cpp {
namespace lang {

class ArrayIndexOutOfBoundException: public IndexOutOfBoundException {
public:
	ArrayIndexOutOfBoundException(int index);
	ArrayIndexOutOfBoundException(int index, Exception *cause);
	~ArrayIndexOutOfBoundException();

private:
	void init(int index, Exception *cause);
};

}
}

#endif /* ARRAYINDEXOUTOFBOUNDEXCEPTION_H_ */
