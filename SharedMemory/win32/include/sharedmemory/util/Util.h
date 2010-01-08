/*
 * Util.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef UTIL_H_
#define UTIL_H_

#include <windows.h>
#include <string>

using namespace std;

namespace sharedmemory {

	namespace util {

		class Util {

		private:
			Util();
			virtual ~Util();

		public:
			static string getLastErrorMessage();
		};

	}

}

#endif /* UTIL_H_ */
