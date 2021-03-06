/**
    Copyright (C) 2014 Forrest Guice
    This file is part of SuntimesWidget.

    SuntimesWidget is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SuntimesWidget is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SuntimesWidget.  If not, see <http://www.gnu.org/licenses/>.
*/ 

package com.forrestguice.suntimeswidget.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.forrestguice.suntimeswidget.R;
import com.forrestguice.suntimeswidget.calculator.SuntimesCalculatorDescriptor;
import com.forrestguice.suntimeswidget.layouts.SuntimesLayout;
import com.forrestguice.suntimeswidget.layouts.SuntimesLayout_1x1_0;
import com.forrestguice.suntimeswidget.layouts.SuntimesLayout_1x1_1;
import com.forrestguice.suntimeswidget.layouts.SuntimesLayout_1x1_2;
import com.forrestguice.suntimeswidget.themes.DarkTheme;
import com.forrestguice.suntimeswidget.themes.SuntimesTheme;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Shared preferences used by individual widgets; uses getSharedPreferences (stored in com.forrestguice.suntimeswidget.xml).
 * Each pref takes an appWidgetId; the app uses these prefs by supplying 0 (AppWidgetManager.INVALID_APPWIDGET_ID).
 */
public class WidgetSettings
{
    public static final String PREFS_WIDGET = "com.forrestguice.suntimeswidget";

    public static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String PREF_PREFIX_KEY_APPEARANCE = "_appearance_";
    public static final String PREF_PREFIX_KEY_GENERAL = "_general_";
    public static final String PREF_PREFIX_KEY_LOCATION = "_location_";
    public static final String PREF_PREFIX_KEY_TIMEZONE = "_timezone_";
    public static final String PREF_PREFIX_KEY_DATE = "_date_";
    public static final String PREF_PREFIX_KEY_ACTION = "_action_";

    public static final String PREF_KEY_GENERAL_CALCULATOR = "calculator";
    public static final String PREF_DEF_GENERAL_CALCULATOR = "any";

    public static final String PREF_KEY_APPEARANCE_THEME = "theme";
    public static final String PREF_DEF_APPEARANCE_THEME = DarkTheme.THEMEDEF_NAME;

    public static final String PREF_KEY_APPEARANCE_SHOWTITLE = "showtitle";
    public static final boolean PREF_DEF_APPEARANCE_SHOWTITLE = false;

    public static final String PREF_KEY_APPEARANCE_TITLETEXT = "titletext";
    public static final String PREF_DEF_APPEARANCE_TITLETEXT = "";

    public static final String PREF_KEY_APPEARANCE_WIDGETMODE_1x1 = "widgetmode_1x1";
    public static final WidgetMode1x1 PREF_DEF_APPEARANCE_WIDGETMODE_1x1 = WidgetMode1x1.WIDGETMODE1x1_BOTH_1;

    public static final String PREF_KEY_APPEARANCE_ALLOWRESIZE = "allowresize";
    public static final boolean PREF_DEF_APPEARANCE_ALLOWRESIZE = true;

    public static final String PREF_KEY_APPEARANCE_TIMEFORMATMODE = "timeformatmode";
    public static final TimeFormatMode PREF_DEF_APPEARANCE_TIMEFORMATMODE = TimeFormatMode.MODE_SYSTEM;

    public static final String PREF_KEY_GENERAL_TIMEMODE = "timemode";
    public static final TimeMode PREF_DEF_GENERAL_TIMEMODE = TimeMode.OFFICIAL;

    public static final String PREF_KEY_GENERAL_TIMENOTE_RISE = "timenoterise";
    public static final SolarEvents PREF_DEF_GENERAL_TIMENOTE_RISE = SolarEvents.SUNRISE;

    public static final String PREF_KEY_GENERAL_TIMENOTE_SET = "timenoteset";
    public static final SolarEvents PREF_DEF_GENERAL_TIMENOTE_SET = SolarEvents.SUNSET;

    public static final String PREF_KEY_GENERAL_COMPAREMODE = "comparemode";
    public static final CompareMode PREF_DEF_GENERAL_COMPAREMODE = CompareMode.TOMORROW;

    public static final String PREF_KEY_ACTION_MODE = "action";
    public static final ActionMode PREF_DEF_ACTION_MODE = ActionMode.ONTAP_LAUNCH_CONFIG;

    public static final String PREF_KEY_ACTION_LAUNCH = "launch";
    public static final String PREF_DEF_ACTION_LAUNCH = "com.forrestguice.suntimeswidget.SuntimesActivity";

    public static final String PREF_KEY_LOCATION_MODE = "locationMode";
    public static final LocationMode PREF_DEF_LOCATION_MODE = LocationMode.CUSTOM_LOCATION;

    public static final String PREF_KEY_LOCATION_LONGITUDE = "longitude";
    public static String PREF_DEF_LOCATION_LONGITUDE = "-112.4691";      // reassigned later by initDefaults

    public static final String PREF_KEY_LOCATION_LATITUDE = "latitude";
    public static String PREF_DEF_LOCATION_LATITUDE = "34.5409";         // reassigned later by initDefaults

    public static final String PREF_KEY_LOCATION_ALTITUDE = "altitude";
    public static String PREF_DEF_LOCATION_ALTITUDE = "";

    public static final String PREF_KEY_LOCATION_LABEL = "label";
    public static String PREF_DEF_LOCATION_LABEL = "Prescott, AZ";       // reassigned later by initDefaults

    public static final String PREF_KEY_TIMEZONE_MODE = "timezoneMode";
    public static final TimezoneMode PREF_DEF_TIMEZONE_MODE = TimezoneMode.CURRENT_TIMEZONE;

    public static final String PREF_KEY_TIMEZONE_CUSTOM = "timezone";
    public static final String PREF_DEF_TIMEZONE_CUSTOM = "US/Arizona";

    public static final String PREF_KEY_TIMEZONE_SOLARMODE = "solarmode";
    public static final SolarTimeMode PREF_DEF_TIMEZONE_SOLARMODE = SolarTimeMode.LOCAL_MEAN_TIME;

    public static final String PREF_KEY_DATE_MODE = "dateMode";
    public static final DateMode PREF_DEF_DATE_MODE = DateMode.CURRENT_DATE;

    public static final String PREF_KEY_DATE_YEAR = "dateYear";
    public static final int PREF_DEF_DATE_YEAR = -1;

    public static final String PREF_KEY_DATE_MONTH = "dateMonth";
    public static final int PREF_DEF_DATE_MONTH = -1;

    public static final String PREF_KEY_DATE_DAY = "dateDay";
    public static final int PREF_DEF_DATE_DAY = -1;

    /**
     * WidgetOnTap
     */
    public static enum ActionMode
    {
        ONTAP_DONOTHING("Ignore"),
        ONTAP_LAUNCH_CONFIG("Reconfigure Widget"),
        ONTAP_LAUNCH_ACTIVITY("Launch Activity"),
        ONTAP_FLIPTO_NEXTITEM("Flip Views");

        private String displayString;

        private ActionMode(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            ONTAP_DONOTHING.setDisplayString(context.getString(R.string.actionMode_doNothing));
            ONTAP_LAUNCH_CONFIG.setDisplayString(context.getString(R.string.actionMode_config));
            ONTAP_LAUNCH_ACTIVITY.setDisplayString(context.getString(R.string.actionMode_launchActivity));
            ONTAP_FLIPTO_NEXTITEM.setDisplayString(context.getString(R.string.actionMode_flipToNextItem));
        }

        public int ordinal( ActionMode[] array )
        {
            for (int i=0; i<array.length; i++)
            {
                if (array[i].name().equals(this.name()))
                {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * WidgetMode1x1
     */
    public static enum WidgetMode1x1
    {
        WIDGETMODE1x1_SUNRISE("Sunrise only", R.layout.layout_widget_1x1_1),
        WIDGETMODE1x1_SUNSET("Sunset only", R.layout.layout_widget_1x1_2),
        WIDGETMODE1x1_BOTH_1("Sunrise & Sunset (1)", R.layout.layout_widget_1x1_0),
        WIDGETMODE1x1_BOTH_2("Sunrise & Sunset (2)", R.layout.layout_widget_1x1_3);

        private final int layoutID;
        private String displayString;

        private WidgetMode1x1(String displayString, int layoutID)
        {
            this.displayString = displayString;
            this.layoutID = layoutID;
        }

        public int getLayoutID()
        {
            return layoutID;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            WIDGETMODE1x1_SUNRISE.setDisplayString(context.getString(R.string.widgetMode1x1_sunrise));
            WIDGETMODE1x1_SUNSET.setDisplayString(context.getString(R.string.widgetMode1x1_sunset));
            WIDGETMODE1x1_BOTH_1.setDisplayString(context.getString(R.string.widgetMode1x1_both_1));
            WIDGETMODE1x1_BOTH_2.setDisplayString(context.getString(R.string.widgetMode1x1_both_2));
        }
    }


    /**
     * DateMode
     */
    public static enum DateMode
    {
        CURRENT_DATE("Today"),
        CUSTOM_DATE("User Defined");

        private String displayString;

        private DateMode(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            CURRENT_DATE.setDisplayString(context.getString(R.string.dateMode_current));
            CUSTOM_DATE.setDisplayString(context.getString(R.string.dateMode_custom));
        }
    }

    /**
     * DateInfo
     */
    public static class DateInfo
    {
        private int year = -1, month = -1, day = -1;

        public DateInfo(Calendar date)
        {
            this(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        }
        public DateInfo( int year, int month, int day )
        {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        public int getYear() { return year; }
        public int getMonth() { return month; }
        public int getDay() { return day; }

        public boolean isSet()
        {
            return (year != -1 && month != -1 && day != -1);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof DateInfo))
            {
                return false;
            } else {
                DateInfo that = (DateInfo)obj;
                return (this.getYear() == that.getYear()) && (this.getMonth() == that.getMonth()) && (this.getDay() == that.getDay());
            }
        }

        @Override
        public int hashCode()
        {
            int hash = Integer.valueOf(year).hashCode();
            hash = hash * 37 + (Integer.valueOf(month).hashCode());
            hash = hash * 37 + (Integer.valueOf(day).hashCode());
            return hash;
        }
    }

    /**
     * TimezoneMode
     */
    public static enum TimezoneMode
    {
        SOLAR_TIME("Solar"),
        CURRENT_TIMEZONE("Current"),
        CUSTOM_TIMEZONE("Custom");

        private String displayString;

        private TimezoneMode(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            SOLAR_TIME.setDisplayString(context.getString(R.string.timezoneMode_solar));
            CURRENT_TIMEZONE.setDisplayString(context.getString(R.string.timezoneMode_current));
            CUSTOM_TIMEZONE.setDisplayString(context.getString(R.string.timezoneMode_custom));
        }
    }

    /**
     * SolarTimeMode
     */
    public static enum SolarTimeMode
    {
        APPARENT_SOLAR_TIME("Current"),
        LOCAL_MEAN_TIME("Solar");

        private String displayString;

        private SolarTimeMode(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            LOCAL_MEAN_TIME.setDisplayString(context.getString(R.string.solartime_localMean));
            APPARENT_SOLAR_TIME.setDisplayString(context.getString(R.string.solartime_apparent));
        }
    }

    /**
     * TimeFormatMode
     */
    public static enum TimeFormatMode
    {
        MODE_SYSTEM("System"),
        MODE_12HR("12 hr"),
        MODE_24HR("24 hr");

        private String displayString;

        private TimeFormatMode( String displayString )
        {
            this.displayString = displayString;
        }

        public String getDisplayString()
        {
            return displayString;

        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            MODE_SYSTEM.setDisplayString(context.getString(R.string.timeFormatMode_system));
            MODE_12HR.setDisplayString(context.getString(R.string.timeFormatMode_12hr));
            MODE_24HR.setDisplayString(context.getString(R.string.timeFormatMode_24hr));

        }
    }

    /**
     * LocationMode
     */
    public static enum LocationMode
    {
        CURRENT_LOCATION("Current Location"),
        CUSTOM_LOCATION("Custom Location");

        private String displayString;

        private LocationMode(String displayString)
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            CURRENT_LOCATION.setDisplayString(context.getString(R.string.locationMode_current));
            CUSTOM_LOCATION.setDisplayString(context.getString(R.string.locationMode_custom));
        }
    }

    /**
     * Location
     */
    public static class Location
    {
        public static String pattern_latLon = "#.#####";

        private String label;
        private String latitude;
        private String longitude;
        private String altitude;   // meters

        /**
         * @param latitude decimal degrees (DD) string
         * @param longitude decimal degrees (DD) string
         */
        public Location( String latitude, String longitude )
        {
            this(null, latitude, longitude, null);
        }

        /**
         * @param label display name
         * @param latitude decimal degrees (DD) string
         * @param longitude decimal degrees (DD) string
         */
        public Location( String label, String latitude, String longitude )
        {
            this(label, latitude, longitude, null);
        }

        /**
         * @param label display name
         * @param latitude decimal degrees (DD) string
         * @param longitude decimal degrees (DD) string
         * @param altitude a placeholder for altitude & not currently used anywhere. The number format is ambiguous / should be fixed if used.
         */
        public Location( String label, String latitude, String longitude, String altitude )
        {
            this.label = (label == null) ? "" : label;
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = (altitude == null) ? "" : altitude;
        }

        /**
         * @param label display name
         * @param location an android.location.Location object (that might be obtained via GPS or otherwise)
         */
        public Location( String label, @NonNull android.location.Location location )
        {
            double rawLatitude = location.getLatitude();
            double rawLongitude = location.getLongitude();
            double rawAltitude = location.getAltitude();

            DecimalFormat formatter = decimalDegreesFormatter();

            this.label = label;
            this.latitude = formatter.format(rawLatitude);
            this.longitude = formatter.format(rawLongitude);
            this.altitude = rawAltitude + "";
        }

        /**
         * @return a user-defined display label / location name
         */
        public String getLabel()
        {
            return label;
        }

        /**
         * @return latitude in decimal degrees (DD)
         */
        public String getLatitude()
        {
            return latitude;
        }

        public Double getLatitudeAsDouble()
        {
            return Double.parseDouble(latitude);
        }

        /**
         * @return longitude in decimal degrees (DD)
         */
        public String getLongitude()
        {
            return longitude;
        }

        public Double getLongitudeAsDouble()
        {
            return Double.parseDouble(longitude);
        }

        public String getAltitude() { return altitude; }

        /**
         * @return a "geo" URI describing this Location
         */
        public Uri getUri()
        {
            String uriString = "geo:" + latitude + "," + longitude;
            if (!altitude.isEmpty())
            {
                uriString += "," + altitude;
            }
            return Uri.parse(uriString);
        }

        /**
         * @return a decimal degrees string "latitude, longitude" describing this location
         */
        public String toString()
        {
            return latitude + ", " + longitude;
        }

        /**
         * @param obj another Location object
         * @return true the locations are the same (label, lat, lon, and alt), false they are different somehow
         */
        @Override
        public boolean equals(Object obj)
        {
            if (!(obj instanceof Location))
            {
                return false;
            } else {
                Location that = (Location)obj;
                return (this.getLabel().equals(that.getLabel()))
                        && (this.getLatitude().equals(that.getLatitude()))
                        && (this.getLongitude().equals(that.getLongitude()))
                        && (this.getAltitude().equals(that.getAltitude()));
            }
        }

        public static DecimalFormat decimalDegreesFormatter()
        {
            DecimalFormat formatter = (DecimalFormat)(NumberFormat.getNumberInstance(Locale.US));
            formatter.applyLocalizedPattern(pattern_latLon);
            return formatter;
        }
    }

    /**
     * CompareMode
     */
    public static enum CompareMode
    {
        YESTERDAY("Yesterday"),
        TOMORROW("Tomorrow");

        private String displayString;

        private CompareMode( String displayString )
        {
            this.displayString = displayString;
        }

        public String getDisplayString()
        {
            return displayString;
        }

        public void setDisplayString( String displayString )
        {
            this.displayString = displayString;
        }

        public String toString()
        {
            return displayString;
        }

        public static void initDisplayStrings( Context context )
        {
            YESTERDAY.setDisplayString( context.getString(R.string.compareMode_yesterday) );
            TOMORROW.setDisplayString( context.getString(R.string.compareMode_tomorrow) );
        }
    }

    /**
     * TimeMode
     */
    public static enum TimeMode
    {
        OFFICIAL("Actual", "Actual Time"),
        CIVIL("Civil", "Civil Twilight"),
        NAUTICAL("Nautical", "Nautical Twilight"),
        ASTRONOMICAL("Astronomical", "Astronomical Twilight"),
        NOON("Noon", "Solar Noon");

        public static boolean shortDisplayStrings = false;
        private String longDisplayString;
        private String shortDisplayString;

        private TimeMode(String shortDisplayString, String longDisplayString)
        {
            this.shortDisplayString = shortDisplayString;
            this.longDisplayString = longDisplayString;

        }

        public String toString()
        {
            if (shortDisplayStrings)
            {
                return shortDisplayString;

            } else {
                return longDisplayString;
            }
        }

        public String getShortDisplayString()
        {
            return shortDisplayString;
        }

        public String getLongDisplayString()
        {
            return longDisplayString;
        }

        public void setDisplayStrings(String shortDisplayString, String longDisplayString)
        {
            this.shortDisplayString = shortDisplayString;
            this.longDisplayString = longDisplayString;
        }

        public static void initDisplayStrings( Context context )
        {
            OFFICIAL.setDisplayStrings( context.getString(R.string.timeMode_official_short),
                    context.getString(R.string.timeMode_official) );

            NAUTICAL.setDisplayStrings( context.getString(R.string.timeMode_nautical_short),
                    context.getString(R.string.timeMode_nautical));

            CIVIL.setDisplayStrings( context.getString(R.string.timeMode_civil_short),
                    context.getString(R.string.timeMode_civil) );

            ASTRONOMICAL.setDisplayStrings( context.getString(R.string.timeMode_astronomical_short),
                    context.getString(R.string.timeMode_astronomical) );

            NOON.setDisplayStrings( context.getString(R.string.timeMode_noon_short),
                    context.getString(R.string.timeMode_noon) );
        }
    }

    public static void saveAllowResizePref(Context context, int appWidgetId, boolean allowResize)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.putBoolean(prefs_prefix + PREF_KEY_APPEARANCE_ALLOWRESIZE, allowResize);
        prefs.apply();
    }
    public static boolean loadAllowResizePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        return prefs.getBoolean(prefs_prefix + PREF_KEY_APPEARANCE_ALLOWRESIZE, PREF_DEF_APPEARANCE_ALLOWRESIZE);
    }
    public static void deleteAllowResizePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.remove(prefs_prefix + PREF_KEY_APPEARANCE_ALLOWRESIZE);
        prefs.apply();
    }

    public static void save1x1ModePref(Context context, int appWidgetId, WidgetSettings.WidgetMode1x1 mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.putString(prefs_prefix + PREF_KEY_APPEARANCE_WIDGETMODE_1x1, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.WidgetMode1x1 load1x1ModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_APPEARANCE_WIDGETMODE_1x1, PREF_DEF_APPEARANCE_WIDGETMODE_1x1.name());

        WidgetMode1x1 widgetMode;
        try
        {
            widgetMode = WidgetSettings.WidgetMode1x1.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            widgetMode = PREF_DEF_APPEARANCE_WIDGETMODE_1x1;
            Log.w("load1x1ModePref", "Failed to load value '" + modeString + "'; using default '" + PREF_DEF_APPEARANCE_WIDGETMODE_1x1.name() + "'.");
        }
        return widgetMode;
    }
    public static SuntimesLayout load1x1ModePref_asLayout(Context context, int appWidgetId)
    {
        SuntimesLayout layout;
        WidgetSettings.WidgetMode1x1 mode = load1x1ModePref(context, appWidgetId);
        switch (mode.getLayoutID())
        {
            case R.layout.layout_widget_1x1_1:
                layout = new SuntimesLayout_1x1_1();
                break;

            case R.layout.layout_widget_1x1_2:
                layout = new SuntimesLayout_1x1_2();
                break;

            case R.layout.layout_widget_1x1_0:
            default:
                layout = new SuntimesLayout_1x1_0(mode.getLayoutID());
                break;
        }
        return layout;
    }
    public static void delete1x1ModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.remove(prefs_prefix + PREF_KEY_APPEARANCE_WIDGETMODE_1x1);
        prefs.apply();
    }




    public static void saveThemePref(Context context, int appWidgetId, String themeName)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.putString(prefs_prefix + PREF_KEY_APPEARANCE_THEME, themeName);
        prefs.apply();
    }
    public static SuntimesTheme loadThemePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        String themeName = prefs.getString(prefs_prefix + PREF_KEY_APPEARANCE_THEME, PREF_DEF_APPEARANCE_THEME);

        //noinspection UnnecessaryLocalVariable
        SuntimesTheme theme = WidgetThemes.loadTheme(context, themeName);
        //Log.d("loadThemePref", "theme is " + theme.themeName());
        return theme;
    }
    public static void deleteThemePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.remove(prefs_prefix + PREF_KEY_APPEARANCE_THEME);
        prefs.apply();
    }


    public static void saveCalculatorModePref(Context context, int appWidgetId, SuntimesCalculatorDescriptor mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.putString(prefs_prefix + PREF_KEY_GENERAL_CALCULATOR, mode.name());
        prefs.apply();
    }
    public static SuntimesCalculatorDescriptor loadCalculatorModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_GENERAL_CALCULATOR, PREF_DEF_GENERAL_CALCULATOR);

        SuntimesCalculatorDescriptor calculatorMode = null;
        try
        {
            calculatorMode = SuntimesCalculatorDescriptor.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            Log.e("loadCalculatorModePref", e.toString() + " ... It looks like " + modeString + " isn't in our list of calculators.");
            // TODO: handle this better. right now it allows this function to return a null, which triggers NullPointerExceptions later
            // ... what is the right course of action? either instantiate a default (that couples us to that third party code) or ...? our widget doesn't currently have an error display state
        }
        return calculatorMode;
    }
    public static void deleteCalculatorModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.remove(prefs_prefix + PREF_KEY_GENERAL_CALCULATOR);
        prefs.apply();
    }


    public static void saveShowTitlePref(Context context, int appWidgetId, boolean showTitle)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.putBoolean(prefs_prefix + PREF_KEY_APPEARANCE_SHOWTITLE, showTitle);
        prefs.apply();
    }
    public static boolean loadShowTitlePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        return prefs.getBoolean(prefs_prefix + PREF_KEY_APPEARANCE_SHOWTITLE, PREF_DEF_APPEARANCE_SHOWTITLE);
    }
    public static void deleteShowTitlePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.remove(prefs_prefix + PREF_KEY_APPEARANCE_SHOWTITLE);
        prefs.apply();
    }


    public static void saveTitleTextPref(Context context, int appWidgetId, String titleText)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.putString(prefs_prefix + PREF_KEY_APPEARANCE_TITLETEXT, titleText);
        prefs.apply();
    }
   public static String loadTitleTextPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        return prefs.getString(prefs_prefix + PREF_KEY_APPEARANCE_TITLETEXT, PREF_DEF_APPEARANCE_TITLETEXT);
    }
    public static void deleteTitleTextPref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.remove(prefs_prefix + PREF_KEY_APPEARANCE_TITLETEXT);
        prefs.apply();
    }


    public static void saveTimeModePref(Context context, int appWidgetId, WidgetSettings.TimeMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.putString(prefs_prefix + PREF_KEY_GENERAL_TIMEMODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.TimeMode loadTimeModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_GENERAL_TIMEMODE, PREF_DEF_GENERAL_TIMEMODE.name());

        TimeMode timeMode;
        try
        {
            timeMode = WidgetSettings.TimeMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            timeMode = PREF_DEF_GENERAL_TIMEMODE;
        }
        return timeMode;
    }
    public static void deleteTimeModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.remove(prefs_prefix + PREF_KEY_GENERAL_TIMEMODE);
        prefs.apply();
    }

    public static void saveSolarTimeModePref(Context context, int appWidgetId, WidgetSettings.SolarTimeMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.putString(prefs_prefix + PREF_KEY_TIMEZONE_SOLARMODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.SolarTimeMode loadSolarTimeModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_TIMEZONE_SOLARMODE, PREF_DEF_TIMEZONE_SOLARMODE.name());

        SolarTimeMode timeMode;
        try
        {
            timeMode = WidgetSettings.SolarTimeMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            timeMode = PREF_DEF_TIMEZONE_SOLARMODE;
        }
        return timeMode;
    }
    public static void deleteSolarTimeModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.remove(prefs_prefix + PREF_KEY_TIMEZONE_SOLARMODE);
        prefs.apply();
    }


    public static void saveTimeFormatModePref(Context context, int appWidgetId, WidgetSettings.TimeFormatMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.putString(prefs_prefix + PREF_KEY_APPEARANCE_TIMEFORMATMODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.TimeFormatMode loadTimeFormatModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_APPEARANCE_TIMEFORMATMODE, PREF_DEF_APPEARANCE_TIMEFORMATMODE.name());

        TimeFormatMode formatMode;
        try
        {
            formatMode = WidgetSettings.TimeFormatMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            formatMode = PREF_DEF_APPEARANCE_TIMEFORMATMODE;
        }
        return formatMode;
    }
    public static void deleteTimeFormatModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_APPEARANCE;
        prefs.remove(prefs_prefix + PREF_KEY_APPEARANCE_TIMEFORMATMODE);
        prefs.apply();
    }


    public static void saveActionModePref(Context context, int appWidgetId, WidgetSettings.ActionMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_ACTION;
        prefs.putString(prefs_prefix + PREF_KEY_ACTION_MODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.ActionMode loadActionModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_ACTION;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_ACTION_MODE, PREF_DEF_ACTION_MODE.name());

        ActionMode actionMode;
        try
        {
            actionMode = WidgetSettings.ActionMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            actionMode = PREF_DEF_ACTION_MODE;
        }
        return actionMode;
    }
    public static void deleteActionModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_ACTION;
        prefs.remove(prefs_prefix + PREF_KEY_ACTION_MODE);
        prefs.apply();
    }


    public static void saveActionLaunchPref(Context context, int appWidgetId, String launchString)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_ACTION;
        prefs.putString(prefs_prefix + PREF_KEY_ACTION_LAUNCH, launchString);
        prefs.apply();
    }
    public static String loadActionLaunchPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_ACTION;
        //noinspection UnnecessaryLocalVariable
        String launchString = prefs.getString(prefs_prefix + PREF_KEY_ACTION_LAUNCH, PREF_DEF_ACTION_LAUNCH);
        return launchString;

    }
    public static void deleteActionLaunchPref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_ACTION;
        prefs.remove(prefs_prefix + PREF_KEY_ACTION_LAUNCH);
        prefs.apply();
    }




    public static void saveLocationModePref(Context context, int appWidgetId, WidgetSettings.LocationMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_LOCATION;
        prefs.putString(prefs_prefix + PREF_KEY_LOCATION_MODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.LocationMode loadLocationModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_LOCATION;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_LOCATION_MODE, PREF_DEF_LOCATION_MODE.name());

        LocationMode locationMode;
        try
        {
            locationMode = WidgetSettings.LocationMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            locationMode = PREF_DEF_LOCATION_MODE;
        }
        return locationMode;
    }
    public static void deleteLocationModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_LOCATION;
        prefs.remove(prefs_prefix + PREF_KEY_LOCATION_MODE);
        prefs.apply();
    }


    public static void saveDateModePref(Context context, int appWidgetId, WidgetSettings.DateMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_DATE;
        prefs.putString(prefs_prefix + PREF_KEY_DATE_MODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.DateMode loadDateModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_DATE;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_DATE_MODE, PREF_DEF_DATE_MODE.name());

        DateMode dateMode;
        try
        {
            dateMode = WidgetSettings.DateMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            dateMode = PREF_DEF_DATE_MODE;
        }
        return dateMode;
    }
    public static void deleteDateModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_DATE;
        prefs.remove(prefs_prefix + PREF_KEY_DATE_MODE);
        prefs.apply();
    }

    public static void saveDatePref(Context context, int appWidgetId, DateInfo info )
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_DATE;
        prefs.putInt(prefs_prefix + PREF_KEY_DATE_YEAR, info.getYear());
        prefs.putInt(prefs_prefix + PREF_KEY_DATE_MONTH, info.getMonth());
        prefs.putInt(prefs_prefix + PREF_KEY_DATE_DAY, info.getDay());
        prefs.apply();
    }
    public static WidgetSettings.DateInfo loadDatePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_DATE;
        int year = prefs.getInt(prefs_prefix + PREF_KEY_DATE_YEAR, PREF_DEF_DATE_YEAR);
        int month = prefs.getInt(prefs_prefix + PREF_KEY_DATE_MONTH, PREF_DEF_DATE_MONTH);
        int day = prefs.getInt(prefs_prefix + PREF_KEY_DATE_DAY, PREF_DEF_DATE_DAY);
        return new DateInfo(year, month, day);
    }
    public static void deleteDatePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_DATE;
        prefs.remove(prefs_prefix + PREF_KEY_DATE_YEAR);
        prefs.remove(prefs_prefix + PREF_KEY_DATE_MONTH);
        prefs.remove(prefs_prefix + PREF_KEY_DATE_DAY);
        prefs.apply();
    }


    public static void saveTimezoneModePref(Context context, int appWidgetId, WidgetSettings.TimezoneMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_TIMEZONE;
        prefs.putString(prefs_prefix + PREF_KEY_TIMEZONE_MODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.TimezoneMode loadTimezoneModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_TIMEZONE;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_TIMEZONE_MODE, PREF_DEF_TIMEZONE_MODE.name());

        TimezoneMode timezoneMode;
        try
        {
            timezoneMode = WidgetSettings.TimezoneMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            timezoneMode = PREF_DEF_TIMEZONE_MODE;
        }
        return timezoneMode;
    }
    public static void deleteTimezoneModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_TIMEZONE;
        prefs.remove(prefs_prefix + PREF_KEY_TIMEZONE_MODE);
        prefs.apply();
    }


    public static void saveLocationPref(Context context, int appWidgetId, Location location)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_LOCATION;
        prefs.putString(prefs_prefix + PREF_KEY_LOCATION_ALTITUDE, location.getAltitude());
        prefs.putString(prefs_prefix + PREF_KEY_LOCATION_LONGITUDE, location.getLongitude());
        prefs.putString(prefs_prefix + PREF_KEY_LOCATION_LATITUDE, location.getLatitude());
        prefs.putString(prefs_prefix + PREF_KEY_LOCATION_LABEL, location.getLabel());
        prefs.apply();
    }
    public static Location loadLocationPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_LOCATION;
        //String altString = prefs.getString(prefs_prefix + PREF_KEY_LOCATION_ALTITUDE, PREF_DEF_LOCATION_ALTITUDE);
        String lonString = prefs.getString(prefs_prefix + PREF_KEY_LOCATION_LONGITUDE, PREF_DEF_LOCATION_LONGITUDE);
        String latString = prefs.getString(prefs_prefix + PREF_KEY_LOCATION_LATITUDE, PREF_DEF_LOCATION_LATITUDE);
        String nameString = prefs.getString(prefs_prefix + PREF_KEY_LOCATION_LABEL, PREF_DEF_LOCATION_LABEL);
        return new Location(nameString, latString, lonString);

    }
    public static void deleteLocationPref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_LOCATION;
        prefs.remove(prefs_prefix + PREF_KEY_LOCATION_ALTITUDE);
        prefs.remove(prefs_prefix + PREF_KEY_LOCATION_LONGITUDE);
        prefs.remove(prefs_prefix + PREF_KEY_LOCATION_LATITUDE);
        prefs.remove(prefs_prefix + PREF_KEY_LOCATION_LABEL);
        prefs.apply();
    }


    public static void saveTimezonePref(Context context, int appWidgetId, String timezone)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_TIMEZONE;
        prefs.putString(prefs_prefix + PREF_KEY_TIMEZONE_CUSTOM, timezone);
        prefs.apply();
    }
    public static String loadTimezonePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_TIMEZONE;
        return prefs.getString(prefs_prefix + PREF_KEY_TIMEZONE_CUSTOM, PREF_DEF_TIMEZONE_CUSTOM);
    }
    public static void deleteTimezonePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_TIMEZONE;
        prefs.remove(prefs_prefix + PREF_KEY_TIMEZONE_CUSTOM);
        prefs.apply();
    }


    public static void saveCompareModePref(Context context, int appWidgetId, WidgetSettings.CompareMode mode)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.putString(prefs_prefix + PREF_KEY_GENERAL_COMPAREMODE, mode.name());
        prefs.apply();
    }
    public static WidgetSettings.CompareMode loadCompareModePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_GENERAL_COMPAREMODE, PREF_DEF_GENERAL_COMPAREMODE.name());

        CompareMode compareMode;
        try
        {
            compareMode = WidgetSettings.CompareMode.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            compareMode = PREF_DEF_GENERAL_COMPAREMODE;
        }
        return compareMode;
    }
    public static void deleteCompareModePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.remove(prefs_prefix + PREF_KEY_GENERAL_COMPAREMODE);
        prefs.apply();
    }



    public static void saveTimeNoteRisePref(Context context, int appWidgetId, SolarEvents riseChoice)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.putString(prefs_prefix + PREF_KEY_GENERAL_TIMENOTE_RISE, riseChoice.name());
        prefs.apply();
    }
    public static SolarEvents loadTimeNoteRisePref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_GENERAL_TIMENOTE_RISE, PREF_DEF_GENERAL_TIMENOTE_RISE.name());

        SolarEvents riseMode;
        try {
            riseMode = SolarEvents.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            riseMode = PREF_DEF_GENERAL_TIMENOTE_RISE;
        }
        return riseMode;
    }
    public static void deleteTimeNoteRisePref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.remove(prefs_prefix + PREF_KEY_GENERAL_TIMENOTE_RISE);
        prefs.apply();
    }



    public static void saveTimeNoteSetPref(Context context, int appWidgetId, SolarEvents setChoice)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.putString(prefs_prefix + PREF_KEY_GENERAL_TIMENOTE_SET, setChoice.name());
        prefs.apply();
    }
    public static SolarEvents loadTimeNoteSetPref(Context context, int appWidgetId)
    {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_WIDGET, 0);
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        String modeString = prefs.getString(prefs_prefix + PREF_KEY_GENERAL_TIMENOTE_SET, PREF_DEF_GENERAL_TIMENOTE_SET.name());

        SolarEvents setMode;
        try {
            setMode = SolarEvents.valueOf(modeString);

        } catch (IllegalArgumentException e) {
            setMode = PREF_DEF_GENERAL_TIMENOTE_SET;
        }
        return setMode;
    }
    public static void deleteTimeNoteSetPref(Context context, int appWidgetId)
    {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_WIDGET, 0).edit();
        String prefs_prefix = PREF_PREFIX_KEY + appWidgetId + PREF_PREFIX_KEY_GENERAL;
        prefs.remove(prefs_prefix + PREF_KEY_GENERAL_TIMENOTE_SET);
        prefs.apply();
    }

    public static void deletePrefs(Context context, int appWidgetId)
    {
        deleteActionModePref(context, appWidgetId);
        deleteActionLaunchPref(context, appWidgetId);

        delete1x1ModePref(context, appWidgetId);
        deleteAllowResizePref(context, appWidgetId);

        deleteThemePref(context, appWidgetId);
        deleteShowTitlePref(context, appWidgetId);
        deleteTitleTextPref(context, appWidgetId);
        deleteTimeFormatModePref(context, appWidgetId);

        deleteCalculatorModePref(context, appWidgetId);
        deleteTimeModePref(context, appWidgetId);
        deleteCompareModePref(context, appWidgetId);

        deleteLocationModePref(context, appWidgetId);
        deleteLocationPref(context, appWidgetId);

        deleteTimezoneModePref(context, appWidgetId);
        deleteSolarTimeModePref(context, appWidgetId);
        deleteTimezonePref(context, appWidgetId);

        deleteDateModePref(context, appWidgetId);
        deleteDatePref(context, appWidgetId);

        deleteTimeNoteRisePref(context, appWidgetId);
        deleteTimeNoteSetPref(context, appWidgetId);
    }

    public static void initDefaults( Context context )
    {
        PREF_DEF_LOCATION_LABEL = context.getString(R.string.default_location_label);
        PREF_DEF_LOCATION_LATITUDE = context.getString(R.string.default_location_latitude);
        PREF_DEF_LOCATION_LONGITUDE = context.getString(R.string.default_location_longitude);
    }

    public static void initDisplayStrings( Context context )
    {
        ActionMode.initDisplayStrings(context);
        WidgetMode1x1.initDisplayStrings(context);
        CompareMode.initDisplayStrings(context);
        TimeMode.initDisplayStrings(context);
        LocationMode.initDisplayStrings(context);
        TimezoneMode.initDisplayStrings(context);
        SolarTimeMode.initDisplayStrings(context);
        DateMode.initDisplayStrings(context);
        TimeFormatMode.initDisplayStrings(context);
    }
}
