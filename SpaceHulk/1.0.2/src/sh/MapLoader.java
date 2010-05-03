// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
//import java.util.Stack;
import java.util.Vector;
import util.Point2I;

class MapLoader
{
    private Map map_;

    MapLoader(Map map, InputStream is)
        throws java.io.IOException
    {
        map_ = map;

        try
        {
            //util.Debug.message("+MapLoader::MapLoader");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            MapHandler mh = new MapHandler();
            saxParser.parse(is, mh);
            //util.Debug.message("-MapLoader::MapLoader");
        }
        catch (javax.xml.parsers.ParserConfigurationException e)
        {
            e.printStackTrace();
            throw new java.io.IOException("ParserConfigurationException");
        }
        catch (org.xml.sax.SAXException e)
        {
            e.printStackTrace();
            throw new java.io.IOException("SAXException");
        }
    }

    class BaseHandler extends DefaultHandler
    {
        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if (attributes != null && attributes.getLength() > 0)
            {
                for (int j = 0; j < attributes.getLength(); j++)
                {
                    String name = attributes.getQName(j);
                    String value = attributes.getValue(j);
                    setAttribute(name, value);
                }
            }
        }

        void setAttribute(String name, String value)
        {
            util.Debug.error("BaseHandler: Unknown attribute: " + name);
        }
    }

    class TileHandler extends BaseHandler
    {
        private int y_ = 0;
        private int x_ = 0;
        private Vector entry_ = new Vector();
        private Vector exit_ = new Vector();

        void done()
        {
            for (int i = 0; i < entry_.size(); ++i)
            {
                Point2I p = (Point2I) entry_.elementAt(i);
                map_.setTile(p.x, p.y, TileType.ENTRY);
            }
            for (int i = 0; i < exit_.size(); ++i)
            {
                Point2I p = (Point2I) exit_.elementAt(i);
                map_.setTile(p.x, p.y, TileType.EXIT);
            }
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            util.Debug.assert2(qName.equalsIgnoreCase("tile"), "TileHandler: start element " + qName);
            super.startElement(uri, localName, qName, attributes);
            x_ = 0;
        }

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if (length > 0)
            {
                //String text = new String(ch, start, length);
                //util.Debug.message("TileHandler: " + x_ + " " + y_ + " \"" + text + "\"");
                for (int i = start; i < (start + length); ++i)
                {
                    char c = ch[i];
                    //util.Debug.message("TileHandler: " + x_ + " " + y_ + " " + c);
                    if (c >= 'a' && c <= 'z')
                    {
                        map_.setTile(x_, y_, TileType.FLOOR | (c - 'a'));
                    }
                    else if (c >= 'A' && c <= 'Z')
                    {
                        map_.setTile(x_, y_, TileType.WALL | (c - 'A'));
                    }
                    else if (c == '!')
                    {
                        //map_.setTile(x_, y_, TileType.ENTRY);
                        entry_.addElement(new Point2I(x_, y_));
                    }
                    else if (c == '$')
                    {
                        //map_.setTile(x_, y_, TileType.EXIT);
                        exit_.addElement(new Point2I(x_, y_));
                    }
                    else if (c != '~')
                        util.Debug.error("TileHandler: Unknown tile " + x_ + " " + y_ + " " + c);
                    ++x_;
                }
            }
        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            ++y_;
        }
    }

    class PointHandler extends BaseHandler
    {
        private int x_ = 0;
        private int y_ = 0;

        void setAttribute(String name, String value)
        {
            if (name.equalsIgnoreCase("x"))
                x_ = Integer.parseInt(value);
            else if (name.equalsIgnoreCase("y"))
                y_ = Integer.parseInt(value);
            else
                super.setAttribute(name, value);
        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            setObject(x_, y_);
        }

        void setObject(int x, int y)
        {
            //map_.setObject(x, y, 0);
        }
    }

    class DoorHandler extends PointHandler
    {
        void setObject(int x, int y)
        {
            map_.setObject(x, y, DoorState.getTile(TileType.DOOR, DoorState.CLOSED));
        }
    }

    class BulkHeadHandler extends PointHandler
    {
        void setObject(int x, int y)
        {
            map_.setObject(x, y, DoorState.getTile(TileType.BULKHEAD, DoorState.OPEN));
        }
    }

    class StartPosHandler extends PointHandler
    {
        private int team_;
        private String name_;
        private Face face_ = Face.NONE;

        StartPosHandler(int team)
        {
            team_ = team;
        }

        void setAttribute(String name, String value)
        {
            if (name.equalsIgnoreCase("face"))
            {
                if (value.equalsIgnoreCase("north"))
                    face_ = Face.NORTH;
                else if (value.equalsIgnoreCase("east"))
                    face_ = Face.EAST;
                else if (value.equalsIgnoreCase("south"))
                    face_ = Face.SOUTH;
                else if (value.equalsIgnoreCase("west"))
                    face_ = Face.WEST;
                else
                    util.Debug.error("StartPosHandler: Unknown face: " + value);
            }
            else
                super.setAttribute(name, value);
        }

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if (length > 0)
            {
                String text = new String(ch, start, length);
                if (name_ != null)
                    name_ = name_ + text;
                else
                    name_ = text;
            }
        }

        void setObject(int x, int y)
        {
            map_.add(team_, new Map.StartPos(name_, x, y, face_));
        }
    }

    class ObjectHandler extends PointHandler
    {
        private String object_ = null;

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if (length > 0)
            {
                String text = new String(ch, start, length);
                if (object_ != null)
                    object_ = object_ + text;
                else
                    object_ = text;
            }
        }

        int getObject()
        {
            int obj = MapLoader.getObject(object_);
            if (obj == 0)
            {
                util.Debug.error("ObjectHandler: Unknown object " + object_);
                return TileType.OBJECT;
            }
            else
                return obj;
        }

        void setObject(int x, int y)
        {
            int object = getObject();
            if (object == TileType.OBJECT_ARCHIVE || object == TileType.OBJECT_CAT)
                map_.setItem(x, y, object);
            else
                map_.setObject(x, y, object);
        }
    }

    class BlipHandler extends PointHandler
    {
        void setObject(int x, int y)
        {
            Blip blip = new Blip();
            blip.setPos(x, y);
            map_.placePiece(blip);
        }
    }

    class ValueHandler extends BaseHandler
    {
        private String value_ = null;

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if (length > 0)
            {
                String text = new String(ch, start, length);
                if (value_ == null)
                    value_ = text;
                else
                    value_ = value_ + text;
            }
        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            //util.Debug.message("ValueHandler:endElement " + value_);
            setValue(value_);
        }

        void setValue(String value)
        {
        }
    }

    class BlipsPerTurnHandler extends ValueHandler
    {
        void setValue(String value)
        {
            map_.setBlipsPerTurn(Integer.parseInt(value));
        }
    }

    class BlipsPerEntryHandler extends ValueHandler
    {
        void setValue(String value)
        {
            map_.setBlipsPerEntry(Integer.parseInt(value));
        }
    }

    class InitialMoveHandler extends ValueHandler
    {
        void setValue(String value)
        {
            map_.setInitialMoveStealers(value.equalsIgnoreCase("stealers"));
        }
    }

    class InitialBlipsHandler extends ValueHandler
    {
        void setValue(String value)
        {
            map_.setInitialBlips(Integer.parseInt(value));
        }
    }

    class MarineStartHandler extends ValueHandler
    {
        private int team_;
        private int type_ = Marine.STANDARD;
        private int object_ = 0;

        MarineStartHandler(int team)
        {
            team_ = team;
        }

        void setAttribute(String name, String value)
        {
            if (name.equalsIgnoreCase("type"))
            {
                if (value.equalsIgnoreCase("sergeant"))
                    type_ = Marine.SERGEANT;
                else if (value.equalsIgnoreCase("flamer"))
                    type_ = Marine.FLAMER;
                else
                    util.Debug.error("MarineStartHandler: Unknown type: " + value);
            }
            else if (name.equalsIgnoreCase("object"))
            {
                object_ = getObject(value);
                if (object_ == 0)
                    util.Debug.error("MarineStartHandler: Unknown object " + value);
            }
            else
                super.setAttribute(name, value);
        }

        void setValue(String value)
        {
            if (object_ == 0)
                map_.add(team_, new Map.MarineStart(value, type_));
            else
                map_.add(team_, new Map.MarineStart(value, type_, object_));
        }
    }

    class TeamHandler extends BaseHandler
    {
        private int team_ = 0;
        private DefaultHandler topHandler = null;
        //private TileHandler tileHandler = new TileHandler();

        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            topHandler = null;
            if (qName.equalsIgnoreCase("team"))
                super.startElement(uri, localName, qName, attributes);
            else if (qName.equalsIgnoreCase("start"))
                topHandler = new StartPosHandler(team_);
            else if (qName.equalsIgnoreCase("marine"))
                topHandler = new MarineStartHandler(team_);
            else
                util.Debug.error("TeamHandler: Unknown start element " + qName);
            if (topHandler != null)
                topHandler.startElement(uri, localName, qName, attributes);
        }

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if (topHandler != null)
                topHandler.characters(ch, start, length);
        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            if (topHandler != null)
                topHandler.endElement(uri, localName, qName);
            else if (qName.equalsIgnoreCase("team"))
                team_++;
            topHandler = null;
        }
    }

    class MapHandler extends BaseHandler
    {
        private String topHandlerName = null;
        private DefaultHandler topHandler = null;
        private TileHandler tileHandler = new TileHandler();
        private TeamHandler teamHandler = new TeamHandler();
        private int width_ = 0;
        private int height_ = 0;

        public void startDocument()
            throws SAXException
        {
            topHandler = null;
            topHandlerName = null;
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException
        {
            if (topHandler == null)
            {
                if (qName.equalsIgnoreCase("map"))
                {
                    super.startElement(uri, localName, qName, attributes);
                    //util.Debug.message("MapHandler: width: " + width_ + " height: " + height_);
                    map_.create(width_, height_);
                }
                else if (qName.equalsIgnoreCase("tile"))
                    topHandler = tileHandler;
                else if (qName.equalsIgnoreCase("door"))
                    topHandler = new DoorHandler();
                else if (qName.equalsIgnoreCase("bulkhead"))
                    topHandler = new BulkHeadHandler();
                else if (qName.equalsIgnoreCase("object"))
                    topHandler = new ObjectHandler();
                else if (qName.equalsIgnoreCase("blip"))
                    topHandler = new BlipHandler();
                else if (qName.equalsIgnoreCase("blipsperturn"))
                    topHandler = new BlipsPerTurnHandler();
                else if (qName.equalsIgnoreCase("blipsperentry"))
                    topHandler = new BlipsPerEntryHandler();
                else if (qName.equalsIgnoreCase("initialmove"))
                    topHandler = new InitialMoveHandler();
                else if (qName.equalsIgnoreCase("initialblips"))
                    topHandler = new InitialBlipsHandler();
                else if (qName.equalsIgnoreCase("team"))
                    topHandler = teamHandler;
                else if (qName.equalsIgnoreCase("start"))
                    topHandler = teamHandler;
                else if (qName.equalsIgnoreCase("marine"))
                    topHandler = teamHandler;
                else
                    util.Debug.error("MapHandler: Unknown start element " + qName);

                if (topHandler != null)
                {
                    topHandlerName = qName;
                    //util.Debug.message("MapHandler: start element " + topHandlerName);
                }
            }
            if (topHandler != null)
                topHandler.startElement(uri, localName, qName, attributes);
        }

        void setAttribute(String name, String value)
        {
            if (name.equalsIgnoreCase("width"))
                width_ = Integer.parseInt(value);
            else if (name.equalsIgnoreCase("height"))
                height_ = Integer.parseInt(value);
            else
                super.setAttribute(name, value);
        }

        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            if (topHandler != null)
                topHandler.characters(ch, start, length);
        }

        public void endElement(String uri, String localName, String qName)
            throws SAXException
        {
            //util.Debug.message("MapHandler:endElement " + qName);
            //util.Debug.message("MapHandler:endElement topHandlerName " + topHandlerName);
            if (topHandler != null)
            {
                topHandler.endElement(uri, localName, qName);
                if (topHandlerName.equalsIgnoreCase(qName))
                {
                    topHandler = null;
                    topHandlerName = null;
                }
            }
            else if (qName.equalsIgnoreCase("map"))
                tileHandler.done();
        }

        public void endDocument()
            throws SAXException
        {
        }
    }

    static int getObject(String object)
    {
        if (object.equalsIgnoreCase("archive"))
            return TileType.OBJECT_ARCHIVE;
        else if (object.equalsIgnoreCase("toxin"))
            return TileType.OBJECT_TOXIN;
        else if (object.equalsIgnoreCase("cargo"))
            return TileType.OBJECT_CARGO;
        else if (object.equalsIgnoreCase("damping"))
            return TileType.OBJECT_DAMPING;
        else if (object.equalsIgnoreCase("controls1"))
            return TileType.OBJECT_CONTROLS1;
        else if (object.equalsIgnoreCase("controls2"))
            return TileType.OBJECT_CONTROLS2;
        else if (object.equalsIgnoreCase("controls3"))
            return TileType.OBJECT_CONTROLS3;
        else if (object.equalsIgnoreCase("airpump1"))
            return TileType.OBJECT_AIRPUMP1;
        else if (object.equalsIgnoreCase("airpump2"))
            return TileType.OBJECT_AIRPUMP2;
        else if (object.equalsIgnoreCase("airpump3"))
            return TileType.OBJECT_AIRPUMP3;
        else if (object.equalsIgnoreCase("airpump4"))
            return TileType.OBJECT_AIRPUMP4;
        else if (object.equalsIgnoreCase("cat"))
            return TileType.OBJECT_CAT;
        else if (object.equalsIgnoreCase("cryonw"))
            return TileType.OBJECT_CRYO_NW_1;
        else if (object.equalsIgnoreCase("cryone"))
            return TileType.OBJECT_CRYO_NE_2;
        else if (object.equalsIgnoreCase("cryosw"))
            return TileType.OBJECT_CRYO_SW_2;
        else if (object.equalsIgnoreCase("cryose"))
            return TileType.OBJECT_CRYO_SE_2;
        else
            return 0;
    }
}
