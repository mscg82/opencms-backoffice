/*
 * StreamCopy.cpp
 *
 *  Created on: 19/ago/2009
 *      Author: Giuseppe Miscione
 */

#include "cpp/util/StreamCopy.h"

namespace cpp{
namespace util{

	StreamCopy::StreamCopy() {

	}

	StreamCopy::~StreamCopy() {

	}

	ifstream* StreamCopy::getBinaryFile(const char *fileName, bool openAtEndOfFile){
		ios::openmode mode = ios::in|ios::binary;
		if(openAtEndOfFile)
			mode = mode | ios::ate;
		ifstream *ret = new ifstream(fileName, mode);
		(*ret) >> noskipws;
		return ret;
	}

	vector<char>* StreamCopy::toBuffer(basic_istream<char> &inputStream){
		//inputStream.seekg(ios::end);
		int length = inputStream.tellg();
		inputStream.seekg(ios::beg);
		return toBuffer(istream_iterator<char>(inputStream), istream_iterator<char>(), length);
	}

	vector<char>* StreamCopy::toBuffer(istream_iterator<char> begin, istream_iterator<char> end, int inputLength){
		vector<char>* buffer = new vector<char>();
		buffer->reserve(inputLength);
		copy(begin, end, std::back_inserter(*buffer));
		return buffer;
	}

	basic_ostream<char>& StreamCopy::writeBuffer(const vector<char> *buffer, basic_ostream<char> &outputStream){
		copy(buffer->begin(), buffer->end(), ostream_iterator<char>(outputStream));
		outputStream.flush();
		return outputStream;
	}

	void StreamCopy::copyStream(basic_istream<char> &inputStream, basic_ostream<char> &outputStream){
		copyStream(istream_iterator<char>(inputStream), istream_iterator<char>(), outputStream);
	}

	void StreamCopy::copyStream(istream_iterator<char> begin, istream_iterator<char> end, basic_ostream<char> &outputStream){
		copy(begin, end, ostream_iterator<char>(outputStream));
	}

}
}
