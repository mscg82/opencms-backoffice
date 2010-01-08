/*
 * Base64BadBufferLength.h
 *
 *  Created on: 22/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef BASE64BADBUFFERLENGTH_H_
#define BASE64BADBUFFERLENGTH_H_

#include <cpp/lang/Base64Exception.h>

using namespace std;

namespace cpp {
namespace lang {

	class Base64BadStringLengthException: public Base64Exception {
	public:
		Base64BadStringLengthException();
		Base64BadStringLengthException(Exception *cause);
		~Base64BadStringLengthException();

		virtual string getClass() const;
	private:
		static const string exceptionMessage;
	};

}
}

#endif /* BASE64BADBUFFERLENGTH_H_ */
