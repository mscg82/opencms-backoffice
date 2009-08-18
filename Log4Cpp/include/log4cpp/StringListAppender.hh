/*
 * StringQueueAppender.hh
 *
 * Copyright 2000, LifeLine Networks BV (www.lifeline.nl). All rights reserved.
 * Copyright 2000, Bastiaan Bakker. All rights reserved.
 *
 * See the COPYING file for the terms of usage and distribution.
 */

#ifndef _LOG4CPP_STRINGLISTAPPENDER_HH
#define _LOG4CPP_STRINGLISTAPPENDER_HH

#include <log4cpp/Portability.hh>
#include <string>
#include <list>
#include <map>
#include <log4cpp/LayoutAppender.hh>

namespace log4cpp {

    /**
     * This class puts log messages in an in-memory list. Its primary use
     * is in test cases, but it may be useful elsewhere as well.
     *
     * @since 0.2.4
     **/
    class LOG4CPP_EXPORT StringListAppender : public LayoutAppender {
        public:

    	StringListAppender(const std::string& name, int maxLines = 0);
        virtual ~StringListAppender();
        
        virtual bool reopen();
        virtual void close();

        /**
         * Return the current size of the message list.
         * Shorthand for getList().size().
         * @returns the list size
         **/
        virtual size_t listSize() const;

        /**
         * Return the list to which the Appends adds messages.
         * @returns the message list
         **/
        virtual std::list<std::string>& getList();

        /**
		 * Return the list to which the Appends adds messages of the provided priority.
		 * @returns the message list
		 **/
		virtual std::list<std::string>& getListForLevel(const Priority::PriorityLevel &level);

        /**
         * Return the list to which the Appends adds messages.
         * @returns the message list
         **/
        virtual const std::list<std::string>& getList() const;

		/**
		 * Return the list to which the Appends adds messages of the provided priority.
		 * @returns the message list
		 **/
		virtual const std::list<std::string>& getListForLevel(const Priority::PriorityLevel &level) const;

        protected:
        
        /**
         * Appends the LoggingEvent to the queue.
         * @param event the LoggingEvent to layout and append to the queue.
         **/
        virtual void _append(const LoggingEvent& event);

        std::list<std::string> _list;
        std::map<Priority::Value, std::list<std::string>* > _listForValues;
        int _maxLines;
    };
}

#endif // _LOG4CPP_STRINGLISTAPPENDER_HH
