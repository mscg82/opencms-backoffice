/*
 * LogEventListener.hh
 *
 *  Created on: 18/ago/2009
 *      Author: Giuseppe Miscione
 */

#ifndef LOGEVENTLISTENER_HH_
#define LOGEVENTLISTENER_HH_

#include <log4cpp/LoggingEvent.hh>

namespace log4cpp {

	class LogEventListener {
	public:
		LogEventListener();
		virtual ~LogEventListener();

		/**
		 * This event function is called when a LoggingEvent occurs in the
		 * NotifierAppender to which this listener is registered.
		 * @param event The LogginEvent that must be logged.
		 * @param message The formatted message associated to the event.
		 */
		virtual void onLogMessage(const LoggingEvent &event, const std::string &message) = 0;

	};

}

#endif /* LOGEVENTLISTENER_HH_ */
