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
package com.forrestguice.suntimeswidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import android.util.AttributeSet;
import android.util.Log;

import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetData;
import com.forrestguice.suntimeswidget.calculator.SuntimesRiseSetDataset;

import java.util.Calendar;

/**
 * LightMapView .. a stacked bar graph over the duration of a day showing relative duration of
 * night, day, and twilight times.
 */
public class LightMapView extends android.support.v7.widget.AppCompatImageView
{
    private static final double MINUTES_IN_DAY = 24 * 60;

    public static final int DEFAULT_MAX_UPDATE_RATE = 15 * 1000;  // ms value; once every 15s
    public static final int DEFAULT_POINT_RADIUS = 3;
    public static final int DEFAULT_STROKE_WIDTH = 2;

    private LightMapTask drawTask;

    private int maxUpdateRate = DEFAULT_MAX_UPDATE_RATE;
    private int pointRadius = DEFAULT_POINT_RADIUS;
    private int pointStrokeWidth = DEFAULT_STROKE_WIDTH;
    private int colorNight, colorAstro, colorNautical, colorCivil, colorDay, colorPointFill, colorPointStroke;

    private SuntimesRiseSetDataset data = null;
    private long lastUpdate = 0;
    private boolean resizable = true;


    public LightMapView(Context context)
    {
        super(context);
        init(context);
    }

    public LightMapView(Context context, AttributeSet attribs)
    {
        super(context, attribs);
        init(context);
    }

    /**
     * @param context a context used to access resources
     */
    @SuppressWarnings("ResourceType")
    private void init(Context context)
    {
        int[] colorAttrs = { R.attr.graphColor_night,   // 0
                R.attr.graphColor_astronomical,         // 1
                R.attr.graphColor_nautical,             // 2
                R.attr.graphColor_civil,                // 3
                R.attr.graphColor_day,                  // 4
                R.attr.graphColor_pointFill,            // 5
                R.attr.graphColor_pointStroke };        // 6
        TypedArray typedArray = context.obtainStyledAttributes(colorAttrs);
        int def = R.color.transparent;

        colorNight = ContextCompat.getColor(context, typedArray.getResourceId(0, def));
        colorAstro = ContextCompat.getColor(context, typedArray.getResourceId(1, def));
        colorNautical = ContextCompat.getColor(context, typedArray.getResourceId(2, def));
        colorCivil = ContextCompat.getColor(context, typedArray.getResourceId(3, def));
        colorDay = ContextCompat.getColor(context, typedArray.getResourceId(4, def));
        colorPointFill = ContextCompat.getColor(context, typedArray.getResourceId(5, def));
        colorPointStroke = ContextCompat.getColor(context, typedArray.getResourceId(6, def));

        typedArray.recycle();
    }

    public int getMaxUpdateRate()
    {
        return maxUpdateRate;
    }

    public void setResizable( boolean value )
    {
        resizable = value;
    }

    /**
     *
     */
    public void onResume()
    {
        Log.d("DEBUG", "LightMapView onResume");
    }

    /**
     * @param w the changed width
     * @param h the changed height
     * @param oldw the previous width
     * @param oldh the previous height
     */
    @Override
    public void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (resizable)
        {
            updateViews(true);
        }
    }

    /**
     * throttled update method
     */
    public void updateViews( boolean forceUpdate )
    {
        long timeSinceLastUpdate = (System.currentTimeMillis() - lastUpdate);
        if (forceUpdate || timeSinceLastUpdate >= maxUpdateRate)
        {
            updateViews(data);
        }
    }

    /**
     * @param data an instance of SuntimesRiseSetDataset
     */
    public void updateViews(SuntimesRiseSetDataset data)
    {
        this.data = data;

        if (drawTask != null && drawTask.getStatus() == AsyncTask.Status.RUNNING)
        {
            drawTask.cancel(true);
        }
        drawTask = new LightMapTask();
        drawTask.execute(data, getWidth(), getHeight());
    }

    /**
     * @param context a context used to access shared prefs
     */
    protected void loadSettings(Context context)
    {
        //Log.d("DEBUG", "LightMapView loadSettings (prefs)");
        if (isInEditMode())
        {
            //noinspection UnnecessaryReturnStatement
            return;
        }
    }

    /**
     * @param context a context used to access resources
     * @param bundle a Bundle used to load state
     */
    protected void loadSettings(Context context, Bundle bundle )
    {
        //Log.d("DEBUG", "LightMapView loadSettings (bundle)");
    }


    /**
     * @param context a context used to access shared prefs
     * @return true settings were saved
     */
    protected boolean saveSettings(Context context)
    {
        //Log.d("DEBUG", "LightMap loadSettings (prefs)");
        return true;
    }

    /**
     * @param bundle a Bundle used to save state
     * @return true settings were saved
     */
    protected boolean saveSettings(Bundle bundle)
    {
        //Log.d("DEBUG", "LightMapView saveSettings (bundle)");
        return true;
    }

    /**
     * LightMapTask
     */
    private class LightMapTask extends AsyncTask<Object, Void, Bitmap>
    {
        /**
         * @param params 0: SuntimesRiseSetDataset,
         *               1: Integer (width),
         *               2: Integer (height)
         * @return a bitmap, or null params are invalid
         */
        @Override
        protected Bitmap doInBackground(Object... params)
        {
            int w, h;
            SuntimesRiseSetDataset data;
            try {
                data = (SuntimesRiseSetDataset)params[0];
                w = (Integer)params[1];
                h = (Integer)params[2];

            } catch (ClassCastException e) {
                Log.w("LightmapTask", "Invalid params; using [null, 0, 0]");
                return null;
            }

            if (w <= 0 || h <= 0)
            {
                return null;
            }

            Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
            Canvas c = new Canvas(b);
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

            boolean layer_astro, layer_nautical, layer_civil;

            // draw background (night)
            p.setColor(colorNight);
            drawRect(c, p);

            if (data != null)
            {
                // draw astro twilight
                p.setColor(colorAstro);
                if (!(layer_astro = drawRect(data.dataAstro, c, p)))
                {
                    if (data.dataNautical.hasSunriseTimeToday() || data.dataNautical.hasSunsetTimeToday())
                    {
                        drawRect(c, p);
                    }
                }

                // draw nautical twilight
                p.setColor(colorNautical);
                if (!(layer_nautical = drawRect(data.dataNautical, c, p)))
                {
                    if (data.dataCivil.hasSunriseTimeToday() || data.dataCivil.hasSunsetTimeToday())
                    {
                        drawRect(c, p);
                    }
                }

                // draw civil twilight
                p.setColor(colorCivil);
                if (!(layer_civil = drawRect(data.dataCivil, c, p)))
                {
                    if (data.dataActual.hasSunriseTimeToday() || data.dataActual.hasSunsetTimeToday())
                    {
                        drawRect(c, p);
                    }
                }

                // draw foreground (day)
                p.setColor(colorDay);
                if (!drawRect(data.dataActual, c, p))
                {
                    boolean noLayers = !layer_astro && !layer_nautical && !layer_civil;
                    if (noLayers && data.isDay())
                    {
                        drawRect(c, p);
                    }
                }

                // draw now marker
                drawPoint(data.now(), pointRadius, c, p);
            }

            return b;
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onProgressUpdate( Void... progress )
        {
        }

        @Override
        protected void onPostExecute( Bitmap result )
        {
            if (isCancelled())
            {
                result = null;
            }
            setImageBitmap(result);
        }

        /////////////////////////////////////////////

        protected void drawRect(Canvas c, Paint p)
        {
            int w = c.getWidth();
            int h = c.getHeight();
            c.drawRect(0, 0, w, h, p);
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        protected boolean drawRect( SuntimesRiseSetData data, Canvas c, Paint p )
        {
            Calendar riseTime = data.sunriseCalendarToday();
            Calendar setTime = data.sunsetCalendarToday();
            if (riseTime == null && setTime == null)
            {
                return false;
            }

            int w = c.getWidth();
            int h = c.getHeight();

            int left = 0;
            if (riseTime != null)
            {
                double riseMinute = riseTime.get(Calendar.HOUR_OF_DAY) * 60 + riseTime.get(Calendar.MINUTE);
                double riseR = riseMinute / MINUTES_IN_DAY;
                left = (int) Math.round(riseR * w);
            }

            int right = w;
            if (setTime != null)
            {
                double setMinute = setTime.get(Calendar.HOUR_OF_DAY) * 60 + setTime.get(Calendar.MINUTE);
                double setR = setMinute / MINUTES_IN_DAY;
                right = (int) Math.round(setR * w);
            }

            boolean setTimeBeforeRiseTime = (riseTime != null && setTime != null && setTime.getTime().before(riseTime.getTime()));
            if (setTimeBeforeRiseTime)
            {
                c.drawRect(0, 0, right, h, p);
                c.drawRect(left, 0, w, h, p);

            } else {
                c.drawRect(left, 0, right, h, p);
            }
            return true;
        }

        protected void drawPoint( Calendar calendar, int radius, Canvas c, Paint p )
        {
            if (calendar != null)
            {
                int w = c.getWidth();
                int h = c.getHeight();

                double minute = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
                int x = (int) Math.round((minute / MINUTES_IN_DAY) * w);
                int y = h / 2;

                p.setStyle(Paint.Style.FILL);
                p.setColor(colorPointFill);
                c.drawCircle(x, y, radius, p);

                p.setStyle(Paint.Style.STROKE);
                p.setStrokeWidth(pointStrokeWidth);
                p.setColor(colorPointStroke);
                c.drawCircle(x, y, radius, p);
            }
        }
    }

}
