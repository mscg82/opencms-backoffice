/*
 * StreamCopy.h
 *
 *  Created on: 19/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef STREAMCOPY_H_
#define STREAMCOPY_H_

#include <algorithm>
#include <iostream>
#include <fstream>
#include <iterator>
#include <vector>

using namespace std;

namespace cpp{
namespace util{

	class StreamCopy {
	public:
		virtual ~StreamCopy();

		/**
		 * Opens an ifstream in binary mode for the file whose name is provided.
		 * The returned pointer can be used with the <code>toBuffer</code> function
		 * to copy the file content into a memory buffer contained in a <code>vector&lt;char&gt;</code>.
		 * Note that you are responsible to delete the pointer that is returned from this method.
		 * @param fileName the name of the file that will be opened in binary mode.
		 * @param openAtEndOfFile Optional switch to position the file pointer at the end
		 * of the stream when opening the file. Leave it to its default value (<code>true</code>) to
		 * allow <code>toBuffer</code> to correctly calculate file size when allocationg the memory buffer.
		 * @returns An ifstream opened in binary mode for the file whose name is provided.
		 */
		static ifstream* getBinaryFile(const char *fileName, bool openAtEndOfFile = true);

		/**
		 * Copies the whole data contained in the provided input stream to a memory buffer
		 * contained in a <code>vector&lt;char&gt;</code>. This function tries to calculate
		 * the size of the stream by using <code>inputStream.tellg()</code> to get the initial
		 * position in the stream. To allow this to work properly, the position pointer of the
		 * stream should be placed at the end of the stream when calling this method. If not,
		 * the size of the stream cannot be calculated properly and the capacity of the returned
		 * buffer may exceed its real size.
		 * Note that you are responsible to delete the pointer that is returned from this method.
		 * @param inputStream The input stream that contains the data that will be copied to the buffer.
		 * @returns A <code>vector&lt;char&gt;</code> with the data contained in the stream.
		 */
		static vector<char>* toBuffer(basic_istream<char> &inputStream);

		/**
		 * Copies the data contained between the two provided iterators to a memory buffer
		 * contained in a <code>vector&lt;char&gt;</code>. You can optionally provide the
		 * size of the data contained between the two iterators. This size will be used to
		 * size the capacity of the buffer.
		 * Note that you are responsible to delete the pointer that is returned from this method.
		 * @param begin The iterator pointing to the begin of the data to be copied.
		 * @param end The iterator pointing to the end of the data to be copied.
		 * @param inputLength An optional parameter to specify the size of the data.
		 * @returns A <code>vector&lt;char&gt;</code> with the data contained between the
		 * two provided iterators.
		 */
		static vector<char>* toBuffer(istream_iterator<char> begin, istream_iterator<char> end, int inputLength = 0);

		/**
		 * Writes the data in the provided buffer to the supplied output stream.
		 * @param buffer The buffer containing the data to be written.
		 * @param outputStream The output stream in which data will be written.
		 * @returns A reference to outputStream.
		 */
		static basic_ostream<char>& writeBuffer(const vector<char> *buffer, basic_ostream<char> &outputStream);

		/**
		 * Copies the whole input stream in the supplied output stream.
		 * @param inputStream The source input stream.
		 * @param outputStream The destination output stream.
		 */
		static void copyStream(basic_istream<char> &inputStream, basic_ostream<char> &outputStream);

		/**
		 * Copies the data contained between the two provided iterators to the
		 * supplied output stream.
		 * @param begin The iterator pointing to the begin of the data to be copied.
		 * @param end The iterator pointing to the end of the data to be copied.
		 * @param outputStream The destination output stream.
		 */
		static void copyStream(istream_iterator<char> begin, istream_iterator<char> end, basic_ostream<char> &outputStream);
	private:
		StreamCopy();
	};

}
}

#endif /* STREAMCOPY_H_ */
