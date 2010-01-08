/*
 * Base64Encrypt.h
 *
 *  Created on: 22/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef BASE64ENCRYPT_H_
#define BASE64ENCRYPT_H_

#include <stdlib.h>
#include <string>
#include <sstream>
#include <vector>

namespace cpp {
namespace crypt {

	class Base64Encoder {
	public:
		Base64Encoder();
		virtual ~Base64Encoder();

		void append(const char *bytes, const int &length);
		void append(const std::vector<char> &bytes);
		std::string toString();

		static std::string encode(const char *bytes, const int &length);
		static std::string encode(const std::vector<char> &bytes);
	protected:
		std::stringbuf encodedStr;
		std::vector<char> bytesToEncode;

		static const std::string encodeChars;
		static const char paddingChar;

		static void encodeInt(std::stringbuf &buffer, const char *bytes, const int &length);
		static void encodeInt(std::stringbuf &buffer,const std::vector<char> &bytes);
	private:
	};

}
}

#endif /* BASE64ENCRYPT_H_ */
