package com.example.ivan.owlcalendarwear;

//DONE localized calendar displayed +
//DONE capitalized month name +
//DONE print snow for christmas +
//DONE minor enhancements +

//TODO иконка в приложении +
//TODO статистика использования и ошибок

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.os.ConfigurationCompat;
import android.support.wearable.activity.WearableActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.util.DisplayMetrics;

//import android.view.ViewGroup;
//import android.widget.Button;
//import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class MainActivity extends WearableActivity {
    private TextView mTextView;
    private int localWeekOffset = 2;
    private int activeMonth = 0;
    private int activeYear = 0;
    private Locale locale = null;
    private int heightPixels = (new DisplayMetrics()).heightPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //-- init app
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        //-- init design
        /*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightScale = (new Double(new Double(displayMetrics.heightPixels) / new Double(320))).intValue();
        int widthScale = (new Double(new Double(displayMetrics.widthPixels) / new Double(320))).intValue();
        Button mButton1 = (Button) findViewById(R.id.button1);
        if (mButton1 != null) {
            mButton1.setWidth(120 * widthScale);
            mButton1.setHeight(60 * heightScale);
            ViewGroup.LayoutParams lp = mButton1.getLayoutParams();
            if( lp instanceof ViewGroup.MarginLayoutParams)
            {
                ((ViewGroup.MarginLayoutParams) lp).topMargin = 260 * heightScale;
                ((ViewGroup.MarginLayoutParams) lp).setMarginStart(23 * widthScale);
            }
        }
        Button mButton2 = (Button) findViewById(R.id.button2);
        if (mButton2 != null) {
            mButton2.setWidth(120 * widthScale);
            mButton2.setHeight(60 * heightScale);
            ViewGroup.LayoutParams lp = mButton2.getLayoutParams();
            if( lp instanceof ViewGroup.MarginLayoutParams)
            {
                ((ViewGroup.MarginLayoutParams) lp).topMargin = 260 * heightScale;
                ((ViewGroup.MarginLayoutParams) lp).setMarginEnd(23 * widthScale);
            }
        }
        if (mTextView != null) {
            //mTextView.setTextSize(COMPLEX_UNIT_PX,20 * heightScale);
            //mTextView.setTextSize(20 * heightScale);
        }
        //*/
        //-- init locale
        //set local
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
        //if (heightPixels < 300) {
        //    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        //}
        mTextView.setText(Html.fromHtml(drawCalendar(activeMonth, activeYear)
                , Html.FROM_HTML_MODE_COMPACT));
        //mTextView.setText(Html.fromHtml("<font color=red>Hello world!</font>", Html.FROM_HTML_MODE_COMPACT)); //debug
        //-- enable Always On
        setAmbientEnabled();
    }

    public void OnButton1Click(View v) {
        //back button - show previous month
        //-- init month
        if (activeMonth == 0 && activeYear > 1) {
            activeMonth = 11;
            activeYear--;
        }
        else {
            activeMonth--;
        }
        //-- draw calendar
        mTextView = (TextView) findViewById(R.id.text);
        if (mTextView != null) {
            mTextView.setText(Html.fromHtml(drawCalendar(activeMonth, activeYear)
                    , Html.FROM_HTML_MODE_COMPACT));
        }
    }

    public void OnButton2Click(View v) {
        //forward button - show next month
        //-- init month
        if (activeMonth == 11 && activeYear < 999999999) {
            activeMonth = 0;
            activeYear++;
        }
        else {
            activeMonth++;
        }
        //-- draw calendar
        mTextView = (TextView) findViewById(R.id.text);
        if (mTextView != null) {
            mTextView.setText(Html.fromHtml(drawCalendar(activeMonth, activeYear)
                    , Html.FROM_HTML_MODE_COMPACT));
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
            //current date
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
            sb.append("<font color=red><b>");
            String[] arrayMonths = DateFormatSymbols.getInstance(locale).getMonths();
            if (locale.getLanguage().toLowerCase().equals("ru")) {
                arrayMonths = new String[] {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь"
                        , "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
            }
            //print capitalized month name
            sb.append(arrayMonths[drawMonth].substring(0, 1).toUpperCase() + arrayMonths[drawMonth].substring(1));
            sb.append(" ");
            sb.append(drawYear);
            sb.append("</b></font>");
            //if (heightPixels > 300) {
                sb.append("<br>\n<font size=3>&nbsp;</font>");
            //}
            //-- print weekdays
            String weekdays[] = DateFormatSymbols.getInstance(locale).getShortWeekdays();
            sb.append("<br>\n");
            sb.append("<b>");
            int n;
            for (int i = 1; i <= 8; i++) {
                //init
                n = (i + localWeekOffset - 1) % 8;
                if (n == 0) continue;
                if (n > weekdays.length - 1) n = weekdays.length - 1;
                String sWeekDayName = weekdays[n].toUpperCase();
                if (sWeekDayName.length() > 2) {
                    sWeekDayName = sWeekDayName.substring(0, 2);
                }
                else if (sWeekDayName.length() < 2) {
                    //add spacer for short week day name
                    if (locale.getLanguage().toLowerCase().equals("ja")) {
                        //custom spacer for jp locale
                        //sWeekDayName = "&nbsp;" + sWeekDayName;
                    }
                    else if (locale.getLanguage().toLowerCase().equals("ko")) {
                        //custom spacer for kr locale
                        sWeekDayName = " " + sWeekDayName;
                    }
                    else {
                        sWeekDayName = "&nbsp;" + sWeekDayName;
                    }
                }
                //locale.getLanguage().toLowerCase().equals("ru")
                //print
                if (i > 1 && locale.getLanguage().toLowerCase().equals("zh")) {
                    //custom spacer for china locale
                }
                else if (i > 1) {
                    sb.append(" ");
                }
                if (i == 6) sb.append("<font color=navy>");
                sb.append(sWeekDayName);
                if (i == 7) sb.append("</font>");
            }
            sb.append("</b>");
            //-- print month days
            //sb.append("<br>\n<b>пн вт ср чт пт <font color=navy>сб вс</font></b>"); //debug
            int rowCount = 0;
            int i = 0;
            int calDay = calendar.get(Calendar.DATE);
            int calMonth = calendar.get(Calendar.MONTH);
            int calYear = calendar.get(Calendar.YEAR);
            while (!(i == 0 && rowCount >= 6)) {
                //check exit condition
                if (i == 0) {
                    if (rowCount > 3 && calMonth > drawMonth) break;
                    if (rowCount > 3 && drawMonth == 11 && calMonth == 0) break;
                }
                //render line break
                if (i == 0) {
                    sb.append("<br>\n");
                    rowCount++;
                }
                //render spacer
                if (i > 0) sb.append(" ");
                //render font color
                if (calMonth < drawMonth) sb.append("<font color=lightgray>"); //month before
                else if (calMonth > drawMonth) sb.append("<font color=lightgray>"); //month after
                else if (calYear == currentYear && calMonth == currentMonth && calDay == currentDay) sb.append("<font color=red>"); //current date
                else if ((i == 5 || i == 6) && calMonth == drawMonth) sb.append("<font color=navy>"); //weekend
                else sb.append("<font>"); //normal day
                //render date number
                if (calDay < 10) sb.append("&nbsp;");
                if (locale.getLanguage().toLowerCase().equals("ru") && calMonth == 0 && calDay == 7) {
                    sb.append("*"); //print snow for russian christmas
                }
                else if (!locale.getLanguage().toLowerCase().equals("ru") && calMonth == 11 && calDay == 25) {
                    sb.append("**"); //print snow for english christmas
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
                i++;
                if (i > 6) i = 0;
            }
            //sb.append("<br>\ndateStart [" + (new SimpleDateFormat("dd.MM.yyyy")).format(dateStart) + "]"); //debug
            //sb.append("<br>\ndateFirstInMonth [" + (new SimpleDateFormat("dd.MM.yyyy")).format(dateFirstInMonth) + "]"); //debug
            //sb.append("<br>dateLastInMonth [" + (new SimpleDateFormat("dd.MM.yyyy")).format(dateLastInMonth) + "]"); //debug
            //sb.append("<br>\ncal day of week [" + cal.get(Calendar.DAY_OF_WEEK) + "]"); //debug
            /* //debug
            sb.append("<br>\n");
            sb.append("<font color=lightgray>26 27 28 29 30 31</font> <font color=navy>01</font>");
            sb.append("<br>\n");
            sb.append("<font color=lightgray>26 27 28 29 30 31</font> <font color=navy>01</font>");
            sb.append("<br>\n");
            sb.append("02 03 04 05 06 <font color=navy>07 08</font>");
            sb.append("<br>\n");
            sb.append("09 10 11 12 <font color=red>13</font> <font color=navy>14 15</font>");
            sb.append("<br>\n");
            sb.append("16 17 18 19 20 <font color=navy>21 22</font>");
            sb.append("<br>\n");
            sb.append("23 24 25 26 27 <font color=navy>28 29</font>");
            sb.append("<br>\n");
            sb.append("30 31 <font color=lightgray>01 02 03 04 05</font>");
            //*/
            //-- print footer
            while (rowCount < 6) {
                //if (heightPixels > 300) {
                    sb.append("<br>\n&nbsp;");
                //}
                rowCount++;
            }
            //if (heightPixels > 300) {
                sb.append("<br>\n&nbsp;");
                sb.append("<br>\n&nbsp;");
            //}
            //throw new Exception("Ooops..."); //debug
        }
        catch (Exception e) {
            sb = new StringBuilder();
            sb.append("<font color=red><b>We Are Sorry</b></font><br>\n");
            sb.append(e.toString());
        }
        return sb.toString();
    }
}