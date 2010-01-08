/*
 * Base64Exception.h
 *
 *  Created on: 22/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef BASE64EXCEPTION_H_
#define BASE64EXCEPTION_H_

#include <cpp/lang/Exception.h>

using namespace std;

namespace cpp {
namespace lang {

	class Base64Exception: public Exception {
	public:
		Base64Exception();
		Base64Exception(const string &message);
		Base64Exception(const string &message, Exception *cause);
		~Base64Exception();
	};

}
}

#endif /* BASE64EXCEPTION_H_ */
