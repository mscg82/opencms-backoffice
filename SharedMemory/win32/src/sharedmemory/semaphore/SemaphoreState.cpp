/*
 * SemaphoreState.cpp
 *
 *  Created on: 08/gen/2010
 *      Author: Giuseppe Miscione
 */

#include <sharedmemory/semaphore/SemaphoreState.h>

namespace sharedmemory {
namespace semaphore {

	SemaphoreState::SemaphoreState(DWORD status) {
		this->status = status;
	}

	SemaphoreState::SemaphoreState(const SemaphoreState& orig) {
		this->status = orig.status;
	}

	SemaphoreState::~SemaphoreState() {

	}

	SemaphoreState& SemaphoreState::operator=(const SemaphoreState& orig) {
		this->status = orig.status;
		return *this;
	}

	bool SemaphoreState::isSignaled() const {
		return status == WAIT_OBJECT_0;
	}

	bool SemaphoreState::isTimedOut() const {
		return status == WAIT_TIMEOUT;
	}

	bool SemaphoreState::isErrorState() const {
		return status == WAIT_FAILED;
	}

	bool SemaphoreState::isAbandoned() const {
		return status == WAIT_ABANDONED;
	}

	DWORD SemaphoreState::getStatus() const {
		return status;
	}

}
}
