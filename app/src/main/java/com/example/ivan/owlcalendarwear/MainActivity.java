package com.example.ivan.owlcalendarwear;

//-- 2019 Dec tasks
//DONE localized calendar displayed
//DONE capitalized month name
//DONE print snow for christmas
//DONE minor enhancements

//-- 2020 Nov tasks
//DONE app icon
//DONE app name and localized app name
//DONE square screen fix
//DONE system color theme (partially)
//DONE localized weekdays
//DONE minor code review
//UNDONE rotary and physical buttons and gestures (bugs found, disabled)
//UNDONE tile mode - tile API still closed to developers on Nov 2021
//UNDONE swipes with viewPager - next time may be...

import android.os.Bundle;
import android.support.v4.os.ConfigurationCompat;
import android.support.wearable.activity.WearableActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends WearableActivity {
    private int localWeekOffset = 2;
    private int activeMonth = 0;
    private int activeYear = 0;
    private Locale locale = null;

    private final String COLOR_HEADER = "#01579B"; //navy, 000080, 01579B
    private final String COLOR_DATE = "#D50000"; //red
    private final String COLOR_DISABLED = "#4D4D4D"; //lightgray, F5F5F6, E1E2E1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-- init app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-- enable Always On
        setAmbientEnabled();
        //-- init calendar
        InitCalendar();
    }

    /*
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (event.getRepeatCount() == 0) {
            if (keyCode == KeyEvent.KEYCODE_STEM_3
                    || keyCode ==  KeyEvent.KEYCODE_NAVIGATE_PREVIOUS
                    || keyCode ==  KeyEvent.KEYCODE_NAVIGATE_IN) {
                OnButtonLeftClick(null);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_STEM_2) {
                InitCalendar();
                return true;
            } else {
                OnButtonRightClick(null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    //*/

    public void OnButtonLeftClick(View v) {
        //back button - show previous month
        //-- init month
        activeMonth--;
        if (activeMonth < 0) {
            activeMonth = 11;
            activeYear--;
            if (activeYear < 1) {
                activeYear = 1;
                activeMonth = 0;
            }
        }
        //-- draw calendar
        TextView mTextView = (TextView) findViewById(R.id.text);
        if (mTextView != null) {
            mTextView.setText(Html.fromHtml(drawCalendar(activeMonth, activeYear)
                    , Html.FROM_HTML_MODE_COMPACT));
        }
        //mTextView.requestFocus(); //return focus ta catch button events
    }

    public void OnButtonRightClick(View v) {
        //forward button - show next month
        //-- init month
        activeMonth++;
        if (activeMonth > 11) {
            activeMonth = 0;
            activeYear++;
            if (activeYear > 999999) {
                activeYear = 999999;
                activeMonth = 11;
            }
        }
        //-- draw calendar
        TextView mTextView = (TextView) findViewById(R.id.text);
        if (mTextView != null) {
            mTextView.setText(Html.fromHtml(drawCalendar(activeMonth, activeYear)
                    , Html.FROM_HTML_MODE_COMPACT));
        }
        //mTextView.requestFocus(); //return focus ta catch button events
    }

    private void InitCalendar() {
        //-- init color scheme
        //mTextView.setTextColor(getResources().getColor(android.R.color.background_light, R.style.Theme_Wearable));
        //mTextView.getTextColors().getDefaultColor();
        //?android:textColorPrimary
        //android:textColor="?android:textColorPrimary"
        //?android:background
        //android:textColor="@color/black"
        //android:background="@color/white"
        //-- init locale
        //set locale
        try {
            locale = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0);
        }
        catch (Exception e) {
            locale = Locale.getDefault();
        }
        //locale = Locale.forLanguageTag("RU"); //debug locale
        //locale = Locale.ENGLISH; //debug locale
        //locale = Locale.FRANCE; //debug locale
        //locale = Locale.JAPAN; //debug locale
        //locale = Locale.CHINA; //debug locale
        //locale = Locale.GERMANY; //debug locale
        //locale = Locale.ITALY; //debug locale
        //locale = Locale.KOREA; //debug locale
        //locale = Locale.TAIWAN; //debug locale
        //locale = Locale.US; //debug locale
        Calendar calendar = Calendar.getInstance(locale);
        localWeekOffset = calendar.getFirstDayOfWeek();
        //-- init current month
        calendar.setTime(new Date());
        //calendar.set(2018, 2, 15); //debug
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        activeMonth = calendar.get(Calendar.MONTH);
        activeYear = calendar.get(Calendar.YEAR);
        //-- draw calendar
        TextView mTextView = (TextView) findViewById(R.id.text);
        if (mTextView != null) {
            //-- draw calendar
            mTextView.setText(Html.fromHtml(drawCalendar(activeMonth, activeYear)
                    , Html.FROM_HTML_MODE_COMPACT));
            //-- enable to catch button events
            /*
            mTextView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return onKeyDown(keyCode, event);
                }
            });
            mTextView.requestFocus();
            //*/
        }
    }

    private String drawCalendar(int drawMonth, int drawYear) {
        StringBuilder sb = new StringBuilder();
        try {
            //-- init
            int currentDay;
            int currentMonth;
            int currentYear;
            //-- init calendar
            String localLanguage = locale.getLanguage().toLowerCase();
            String localCountry = locale.getCountry().toLowerCase();
            //-- init local weekdays
            //sun, mon, tue, wed, thu, fri, sat
            //sat, sun - classic
            boolean[] arrayWeekends = new boolean[] {true, false, false, false, false, false, true};
            //thu, fri
            if (localCountry.equals("af")) {
                arrayWeekends = new boolean[] {false, false, false, false, true, true, false};
            }
            //fri, sat
            if (localCountry.equals("dz") || localCountry.equals("bh") || localCountry.equals("bd")
                    || localCountry.equals("eg") || localCountry.equals("il")
                    || localCountry.equals("jo") || localCountry.equals("kw")
                    || localCountry.equals("ly") || localCountry.equals("mv")
                    || localCountry.equals("om") || localCountry.equals("ps")
                    || localCountry.equals("qa") || localCountry.equals("sa")
                    || localCountry.equals("so") || localCountry.equals("sd")
                    || localCountry.equals("sy") || localCountry.equals("ae")
                    || localCountry.equals("ye")) {
                arrayWeekends = new boolean[] {false, false, false, false, false, true, true};
            }
            //fri, sun
            if (localCountry.equals("bn")) {
                arrayWeekends = new boolean[] {true, false, false, false, false, true, false};
            }
            //fri
            if (localCountry.equals("dj") || localCountry.equals("ir") || localCountry.equals("iq")) {
                arrayWeekends = new boolean[] {false, false, false, false, false, true, false};
            }
            //sun
            if (localCountry.equals("gq") || localCountry.equals("hk") || localCountry.equals("kp")
                    || localCountry.equals("ug")) {
                arrayWeekends = new boolean[] {true, false, false, false, false, false, false};
            }
            //--current date
            Date dateCurrent = new Date();
            Calendar calendar = Calendar.getInstance(locale);
            calendar.setTime(dateCurrent);
            currentDay = calendar.get(Calendar.DATE);
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            //month to draw
            calendar = Calendar.getInstance(locale);
            calendar.set(activeYear, activeMonth, 1);
            if (calendar.get(Calendar.DAY_OF_WEEK) != localWeekOffset) {
                while (calendar.get(Calendar.DAY_OF_WEEK) != localWeekOffset) {
                    calendar.add(Calendar.DATE, -1);
                }
            }
            //-- print header
            sb.append("<font color=" + COLOR_DATE + "><b>");
            String[] arrayMonths = DateFormatSymbols.getInstance(locale).getMonths();
            if (localLanguage.equals("ru")) {
                arrayMonths = new String[] {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь"
                        , "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
            }
            //print capitalized month name
            sb.append(arrayMonths[drawMonth].substring(0, 1).toUpperCase() + arrayMonths[drawMonth].substring(1));
            sb.append(" ");
            sb.append(drawYear);
            sb.append("</b></font>");
            sb.append("<br>\n<font size=3>&nbsp;</font>");
            //-- print weekdays
            String weekdays[] = DateFormatSymbols.getInstance(locale).getShortWeekdays();
            sb.append("<br>\n");
            sb.append("<b>");
            for (int offsetDrawDay = 1; offsetDrawDay <= 8; offsetDrawDay++) {
                //init
                int offsetWeekDay = offsetWeekDay = (offsetDrawDay + localWeekOffset - 1) % 8; //(!) base of 8
                if (offsetWeekDay == 0) continue;
                if (offsetWeekDay > weekdays.length - 1) offsetWeekDay = weekdays.length - 1;
                String sWeekDayName = weekdays[offsetWeekDay].toUpperCase();
                if (sWeekDayName.length() > 2) {
                    sWeekDayName = sWeekDayName.substring(0, 2);
                }
                else if (sWeekDayName.length() < 2) {
                    //add spacer for short week day name
                    if (localLanguage.equals("ko")) {
                        //custom spacer for korean locale
                        sWeekDayName = " " + sWeekDayName;
                    }
                    else {
                        sWeekDayName = "&nbsp;" + sWeekDayName;
                    }
                }
                //localLanguage.equals("ru") //debug
                //print
                if (offsetDrawDay > 1) sb.append(" ");
                if (arrayWeekends[offsetWeekDay - 1]) sb.append("<font color=" + COLOR_HEADER + ">");
                sb.append(sWeekDayName);
                if (arrayWeekends[offsetWeekDay - 1]) sb.append("</font>");
            }
            sb.append("</b>");
            //-- print month days
            //sb.append("<br>\n<b>пн вт ср чт пт <font color=" + COLOR_HEADER + ">сб вс</font></b>"); //debug
            int rowCount = 0;
            int offsetDrawDay = 0;
            int calDay = calendar.get(Calendar.DATE);
            int calMonth = calendar.get(Calendar.MONTH);
            int calYear = calendar.get(Calendar.YEAR);
            while (!(offsetDrawDay == 0 && rowCount >= 6)) {
                //check exit condition
                if (offsetDrawDay == 0) {
                    if (rowCount > 3 && calMonth > drawMonth) break;
                    if (rowCount > 3 && drawMonth == 11 && calMonth == 0) break;
                }
                //init
                int offsetWeekDay = offsetWeekDay = (offsetDrawDay + localWeekOffset - 1) % 7; //(!) base of 7
                //render line break
                if (offsetDrawDay == 0) {
                    sb.append("<br>\n");
                    rowCount++;
                }
                //render spacer
                if (offsetDrawDay > 0) sb.append(" ");
                //render font color
                if (calMonth < drawMonth) sb.append("<font color=" + COLOR_DISABLED + ">"); //month before
                else if (calMonth > drawMonth) sb.append("<font color=" + COLOR_DISABLED + ">"); //month after
                else if (calYear == currentYear && calMonth == currentMonth && calDay == currentDay) sb.append("<font color=" + COLOR_DATE + ">"); //current date
                else if (arrayWeekends[offsetWeekDay] && calMonth == drawMonth) sb.append("<font color=" + COLOR_HEADER + ">"); //weekend
                else sb.append("<font>"); //normal day
                //render date number
                if (calDay < 10) sb.append("&nbsp;");
                if (localLanguage.equals("ru") && calMonth == 0 && calDay == 7) {
                    sb.append("*"); //print snow for russian christmas
                }
                else if (!localLanguage.equals("ru") && calMonth == 11 && calDay == 25) {
                    sb.append("**"); //print snow for western christmas
                }
                else {
                    sb.append(calDay); //print day number
                }
                //sb.append(String.format(locale, "%02d", calendar.get(Calendar.DATE))); //print date with leading zero
                //render after
                sb.append("</font>");
                //iterate next day
                /*
                calendar.add(Calendar.DATE, 1);
                calDay = calendar.get(Calendar.DATE);
                calMonth = calendar.get(Calendar.MONTH);
                calYear = calendar.get(Calendar.YEAR);
                //*/
                //*
                if (calDay >= 28 && calMonth == 1) {
                    calendar.set(calYear, calMonth, calDay);
                    calendar.add(Calendar.DATE, 1);
                    calDay = calendar.get(Calendar.DATE);
                    calMonth = calendar.get(Calendar.MONTH);
                    calYear = calendar.get(Calendar.YEAR);
                }
                else {
                    calDay++;
                    if (calDay > 30 && (calMonth == 3 || calMonth == 5 || calMonth == 8
                            || calMonth == 10)) {
                        calDay = 1;
                        calMonth++;
                    }
                    else if (calDay > 31 && (calMonth == 0 || calMonth == 2 || calMonth == 4
                            || calMonth == 6 || calMonth == 7 || calMonth == 9 || calMonth == 11)) {
                        calDay = 1;
                        calMonth++;
                    }
                    if (calMonth > 11) {
                        calMonth = 0;
                        calYear++;
                    }
                }
                //*/
                offsetDrawDay++;
                if (offsetDrawDay > 6) offsetDrawDay = 0;
            }
            /* //debug
            sb.append("<br>\n");
            sb.append("<font color=" + COLOR_DISABLED + ">26 27 28 29 30 31</font> <font color=" + COLOR_HEADER + ">01</font>");
            sb.append("<br>\n");
            sb.append("<font color=" + COLOR_DISABLED + ">26 27 28 29 30 31</font> <font color=" + COLOR_HEADER + ">01</font>");
            sb.append("<br>\n");
            sb.append("02 03 04 05 06 <font color=" + COLOR_HEADER + ">07 08</font>");
            sb.append("<br>\n");
            sb.append("09 10 11 12 <font color=" + COLOR_DATE + ">13</font> <font color=" + COLOR_HEADER + ">14 15</font>");
            sb.append("<br>\n");
            sb.append("16 17 18 19 20 <font color=" + COLOR_HEADER + ">21 22</font>");
            sb.append("<br>\n");
            sb.append("23 24 25 26 27 <font color=" + COLOR_HEADER + ">28 29</font>");
            sb.append("<br>\n");
            sb.append("30 31 <font color=" + COLOR_DISABLED + ">01 02 03 04 05</font>");
            //*/
            //-- print footer
            while (rowCount < 6) {
                sb.append("<br>\n&nbsp;");
                rowCount++;
            }
            sb.append("<br>\n&nbsp;");
            sb.append("<br>\n&nbsp;");
        }
        catch (Exception e) {
            sb = new StringBuilder();
            sb.append("<font color=" + COLOR_DATE + "><b>We Are Sorry</b></font><br>\n");
            sb.append(e.toString());
        }
        return sb.toString();
    }
}