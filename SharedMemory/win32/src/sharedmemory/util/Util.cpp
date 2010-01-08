/*
 * Util.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/util/Util.h>

namespace sharedmemory {

	namespace util {

		Util::Util() {

		}

		Util::~Util() {

		}

		string Util::getLastErrorMessage() {
			LPVOID lpMsgBuf;
			FormatMessage(
					FORMAT_MESSAGE_ALLOCATE_BUFFER | FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
					NULL, GetLastError(),
					MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT), (LPTSTR) &lpMsgBuf,
					0, NULL);
			string msg = (LPTSTR) lpMsgBuf;
			LocalFree(lpMsgBuf);
			return msg;
		}

	}

}
