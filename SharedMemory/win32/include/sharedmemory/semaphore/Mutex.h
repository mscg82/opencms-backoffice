/*
 * Mutex.h
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#ifndef MUTEX_H_
#define MUTEX_H_

#include <sharedmemory/semaphore/Semaphore.h>

namespace sharedmemory {
namespace semaphore {

	class Mutex: public Semaphore {
	public:
		Mutex(string semaphoreName);
		virtual ~Mutex();
	};

}
}

#endif /* MUTEX_H_ */
