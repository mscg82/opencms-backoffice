package com.mscg.virgilio.database;

import java.util.Date;

import android.database.Cursor;
import android.database.SQLException;

import com.mscg.virgilio.programs.Programs;

public interface ProgramsManagement {

    public boolean containsProgramsForDay(Date day) throws SQLException;

    public Cursor getPrograms() throws SQLException;

    public long getProgramsCount() throws SQLException;

    public Programs getProgramsForDay(Date day) throws SQLException;

    public void removeAllPrograms() throws SQLException;

    public void removeOlderPrograms(int numDays) throws SQLException;

    public void removeProgramById(long id) throws SQLException;

    public Programs savePrograms(Programs programs) throws SQLException;

}