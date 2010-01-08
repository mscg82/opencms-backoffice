/*
 * IndexOutOfBoundException.h
 *
 *  Created on: 16/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef INDEXOUTOFBOUNDEXCEPTION_H_
#define INDEXOUTOFBOUNDEXCEPTION_H_

#include "Exception.h"

namespace cpp{
namespace lang{

class IndexOutOfBoundException : public Exception{
public:
	IndexOutOfBoundException();
	IndexOutOfBoundException(const string &message);
	IndexOutOfBoundException(const string &message, Exception *cause);
	~IndexOutOfBoundException();
};

}
}

#endif /* INDEXOUTOFBOUNDEXCEPTION_H_ */
