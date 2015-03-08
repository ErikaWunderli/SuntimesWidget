package com.forrestguice.suntimeswidget;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SuntimesUtils
{
    /**
     * TimeDisplayText : class
     */
    public static class TimeDisplayText
    {
        private String value;
        private String units;
        private String suffix;

        public TimeDisplayText(String value, String units, String suffix)
        {
            this.value = value;
            this.units = units;
            this.suffix = suffix;
        }

        public String getValue()
        {
            return value;
        }

        public String getUnits()
        {
            return units;
        }

        public String getSuffix()
        {
            return suffix;
        }

        public String toString()
        {
            StringBuilder s = new StringBuilder();
            s.append(value);
            s.append(" ");
            s.append(units);
            s.append(" ");
            s.append(suffix);
            return s.toString();
        }
    }

    /**
     * @param specifiedSize
     * @return
     */
    public static int getCellsForSize( int specifiedSize )
    {
        int numCells = 1;
        while (getSizeForCells(numCells) < specifiedSize)
        {
            numCells++;
        }
        return numCells-1;
    }

    /**
     * @param numCells
     * @return
     */
    public static int getSizeForCells( int numCells )
    {
        return (70 * numCells) - 30;
    }

    /**
     * @param context
     * @param cal
     * @return
     */
    public static TimeDisplayText calendarTimeShortDisplayString(Context context, Calendar cal)
    {
        Date time = cal.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm");
        SimpleDateFormat suffixFormat = new SimpleDateFormat("a");

        return new TimeDisplayText( timeFormat.format(time), "", suffixFormat.format(time) );
        //return DateUtils.formatDateTime(context, cal.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_A);
    }

    /**
     * @param c1
     * @param c2
     * @return
     */
    public static String calendarDeltaShortDisplayString(Calendar c1, Calendar c2)
    {
        return "";  // TODO
    }

    /**
     * @param timeSpan1 first event
     * @param timeSpan2 second event
     * @return a TimeDisplayText object that describes difference between the two spans
     */
    public static TimeDisplayText timeDeltaLongDisplayString(long timeSpan1, long timeSpan2)
    {
        String value = "";
        String units = "";
        String suffix = "";

        long timeSpan = timeSpan2 - timeSpan1;
        GregorianCalendar d = new GregorianCalendar();
        d.setTimeInMillis(timeSpan);
        long timeInMillis = d.getTimeInMillis();

        long numberOfSeconds = timeInMillis / 1000;
        suffix += ((numberOfSeconds > 0) ? "longer" : "shorter");
        numberOfSeconds = Math.abs(numberOfSeconds);

        long numberOfMinutes = numberOfSeconds / 60;
        long remainingSeconds = numberOfSeconds % 60;

        value += ((numberOfMinutes < 1) ? "" : numberOfMinutes + "m")
                + " " +
                ((remainingSeconds < 1) ? "" : remainingSeconds + "s");

        return new TimeDisplayText(value, units, suffix);
    }

    /**
     * Creates a title string from a given "title pattern".
     *
     * The following substitutions are supported:
     *   %% .. the % character
     *   %m .. the time mode (e.g. nautical twilight / civil twilight / actual time) -> timeMode.getLongDisplayString()
     *   %M .. the time mode (short version) -> timeMode.getShortDisplayString()
     *
     * @param titlePattern a pattern string (simple substitutions)
     * @return a display string suitable for display as a widget title
     */
    public static String displayStringForTitlePattern(String titlePattern, Context context, int appWidgetId)
    {
        SuntimesWidgetSettings.TimeMode timeMode = SuntimesWidgetSettings.loadTimeModePref(context, appWidgetId);

        String displayString = titlePattern;
        String modePattern = "%M";
        String modePatternShort = "%m";
        String percentPattern = "%%";

        displayString = displayString.replaceAll(modePatternShort, timeMode.getShortDisplayString());
        displayString = displayString.replaceAll(modePattern, timeMode.getLongDisplayString());
        displayString = displayString.replaceAll(percentPattern, "%");
        return displayString;
    }
}