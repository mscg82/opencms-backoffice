/*
 * Base64Encrypt.cpp
 *
 *  Created on: 22/ago/2009
 *      Author: Giuseppe Miscione
 */

#include <cpp/crypt/Base64Encoder.h>

namespace cpp {
namespace crypt {

	const std::string Base64Encoder::encodeChars =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	const char Base64Encoder::paddingChar =
		'=';

	Base64Encoder::Base64Encoder() {
		bytesToEncode.reserve(3);
	}

	Base64Encoder::~Base64Encoder() {

	}

	void Base64Encoder::append(const char *bytes, const int &length){
		for(int i = 0; i < length; i++){
			bytesToEncode.push_back(bytes[i]);
			if(bytesToEncode.size() >= 3){
				encodeInt(encodedStr, bytesToEncode);
				bytesToEncode.clear();
			}
		}
	}

	std::string Base64Encoder::toString(){
		if(bytesToEncode.size() != 0){
			encodeInt(encodedStr, bytesToEncode);
			bytesToEncode.clear();
		}
		return encodedStr.str();
	}

	void Base64Encoder::append(const std::vector<char> &bytes){
		append(&(*(bytes.begin())), bytes.size());
	}

	std::string Base64Encoder::encode(const char *bytes, const int &length){
		std::stringbuf ret;
		encodeInt(ret, bytes, length);
		return ret.str();
	}

	std::string Base64Encoder::encode(const std::vector<char> &bytes){
		std::stringbuf ret;
		encodeInt(ret, bytes);
		return ret.str();
	}

	void Base64Encoder::encodeInt(std::stringbuf &buffer, const char *bytes, const int &length){
		char tmp[3] = {(char)0, (char)0, (char)0};
		int i = 0;
		while(i < length){
			tmp[0] = bytes[i];
			tmp[1] = (++i < length ? bytes[i] : (char)0);
			tmp[2] = (++i < length ? bytes[i] : (char)0);
			i++;

			unsigned char index = (unsigned char)0;
			index = tmp[0] / 4;
			buffer.sputc(Base64Encoder::encodeChars[index]);

			index = (unsigned char)0;
			index = (tmp[0] % 4) * 16 + tmp[1] / 16;
			buffer.sputc(Base64Encoder::encodeChars[index]);

			if((i - length) == 2){
				buffer.sputc(Base64Encoder::paddingChar);
			}
			else{
				index = (unsigned char)0;
				index = (tmp[1] % 16) * 4 + tmp[2] / 64;
				buffer.sputc(Base64Encoder::encodeChars[index]);
			}

			if((i - length) >= 1){
				buffer.sputc(Base64Encoder::paddingChar);
			}
			else{
				index = (unsigned char)0;
				index = tmp[2] % 64;
				buffer.sputc(Base64Encoder::encodeChars[index]);
			}
		}
	}

	void Base64Encoder::encodeInt(std::stringbuf &buffer, const std::vector<char> &bytes){
		encodeInt(buffer, &(*(bytes.begin())), bytes.size());
	}

}
}
