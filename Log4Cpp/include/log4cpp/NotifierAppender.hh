/*
 * NotifierAppender.hh
 *
 *  Created on: 18/ago/2009
 *  Author: Giuseppe Miscione
 */

#ifndef NOTIFIERAPPENDER_HH_
#define NOTIFIERAPPENDER_HH_

#include <log4cpp/LayoutAppender.hh>
#include <log4cpp/LogEventListener.hh>
#include <list>

namespace log4cpp {

	/**
	 * This class propagates the LoggingEvent to all
	 * registered LogEventListener, so that another
	 * portion of code (i.e. the code that controls a
	 * text area in a GUI) can be aware of the logging
	 * action.
	 */
	class NotifierAppender: public log4cpp::LayoutAppender {

	public:
		NotifierAppender(const std::string& name, bool deleteListenersOnExit = false);
		virtual ~NotifierAppender();

		virtual bool reopen();

		/**
		 * Closes this appender and unregisters all the
		 * register listeners, without deleting them.
		 */
		virtual void close();

		/**
		 * Registers a listener to this NotifierAppender, so that
		 * all LoggingEvents captured by the appender will be
		 * propagated to the listener.
		 * @param listener A pointer to the listener that will be
		 * registered.
		 */
		virtual void registerListener(LogEventListener *listener);

		/**
		 * Unregisters the provided listener from this NotifierAppender,
		 * if it was previously registered. Optionally, it deletes the
		 * pointed listener.
		 * @param listener The listener that will be unregistered.
		 * @param deleteListener A boolean switch to choose if the listener
		 * must be deleted after it's unregistered. If the listener was not
		 * registered to this appender, it won't be deleted.
		 * @returns <code>true</code> if the listener was unregistered,
		 * <code>false</code> otherwise.
		 */
		virtual bool unregisterListener(LogEventListener *listener, bool deleteListener = false);

		/**
		 * Appends the LoggingEvent to the queue.
		 * @param event the LoggingEvent to layout and append to the queue.
		 **/
		virtual void _append(const LoggingEvent& event);

	protected:
		std::list<LogEventListener*> _listeners;
		bool _deleteListenersOnExit;
	};

}

#endif /* NOTIFIERAPPENDER_HH_ */
