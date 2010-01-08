/*
 * Mutex.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/semaphore/Mutex.h>

namespace sharedmemory {
namespace semaphore {

	Mutex::Mutex(string semaphoreName): Semaphore(semaphoreName, 1, 1) {

	}

	Mutex::~Mutex() {

	}

}
}
