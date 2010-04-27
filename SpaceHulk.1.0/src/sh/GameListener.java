// SpaceHulkME  Copyright (C) 2008  Adam Gates
// This program comes with ABSOLUTELY NO WARRANTY; for license see COPYING.TXT.

package sh;

public interface GameListener
{
    void beginTurn(Piece p);
    void pieceMoving(Piece p);
    void pieceMoved(Piece p);
    void pieceShoots(Piece p);
    void pieceShootsMiss(Piece p);
    void pieceShootsMissDoor(Piece p);
    void pieceJams(Piece p);
    void pieceCloseCombat(Piece p);
    void pieceCloseCombatHit(Piece p);
    void pieceCloseCombatMiss(Piece p);
    void pieceCloseCombatObject(Piece p, int x, int y, int object);
    void pieceDied(Piece p);
    void doorStateChanging(int x, int y, int type, DoorState oldds, DoorState newds);
    void doorStateChanged(int x, int y, int type, DoorState ds);
    void objectDamaged(int x, int y, int object);
    void objectDestroyed(int x, int y, int object);
    void objectUsed(int x, int y, int object, String msg);
    void objectDropped(Marine m, int object);
}
