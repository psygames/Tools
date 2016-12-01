package org.redstone.tools.date;


public class UTCSpan
{
    private UTC from;
    private UTC to;

    private int centuries;
    private int years;
    private int months;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private int milliseconds;

    private boolean isNegative = false;

    public UTCSpan(UTC from, UTC to)
    {
        this.from = from.clone();
        this.to = to.clone();
        Init();
    }

    public UTCSpan(long timeStampFrom, long timestampTo)
    {
        this(new UTC(timeStampFrom), new UTC(timestampTo));
    }

    private void Init()
    {
        isNegative = false;
        int cmp = to.compareTo(from);
        if (cmp > 0)
        {
            subtractInit(to, from);
        }
        else if (cmp < 0)
        {
            subtractInit(from, to);
            makeNegative();
        }
        else
        {
            //do nothing
        }

    }

    private void makeNegative()
    {
        isNegative = true;
        centuries = -centuries;
        years = -years;
        months = -months;
        days = -days;
        hours = -hours;
        minutes = -minutes;
        seconds = -seconds;
        milliseconds = -milliseconds;
    }

    // a - b (a > b)
    private void subtractInit(UTC a, UTC b)
    {
        int carry = 0;
        milliseconds = a.getMillisecond() - b.getMillisecond();
        if (milliseconds < 0)
        {
            carry = -1;
            milliseconds += 1000;
        }

        seconds = a.getSecond() - b.getSecond() + carry;
        if (seconds < 0)
        {
            carry = -1;
            seconds += 60;
        }
        else
            carry = 0;

        minutes = a.getMinute() - b.getMinute() + carry;
        if (minutes < 0)
        {
            carry = -1;
            minutes += 60;
        }
        else
            carry = 0;

        hours = a.getHour() - b.getHour() + carry;
        if (hours < 0)
        {
            carry = -1;
            hours += 24;
        }
        else
            carry = 0;

        days = a.getDay() - b.getDay() + carry;
        if (days < 0)
        {
            carry = -1;
            days += b.monthDays();
        }
        else
            carry = 0;

        months = a.getMonth() - b.getMonth() + carry;
        if (months < 0)
        {
            carry = -1;
            months += 12;
        }
        else
            carry = 0;

        years = a.getYear() - b.getYear() + carry;

        centuries = years / 100;

        years = years % 100;

    }

    @Override
    public String toString()
    {
        return String.format("%d %04d/%02d/%02d %02d:%02d:%02d %03d"
                , centuries, years, months, days, hours, minutes, seconds, milliseconds);
    }


    public String toSpecialString()
    {
        String[] specialSuffix1 = {"刚刚", "秒前", "分钟前", "小时前", "天前", "个月前", "年前", "个世纪前"};
        String[] specialSuffix2 = {"马上", "秒后", "分钟后", "小时后", "天后", "个月后", "年后", "个世纪后"};
        String[] specialSuffix = isNegative ? specialSuffix2 : specialSuffix1;
        int[] tm1 = {seconds, seconds, minutes, hours, days, months, years, centuries};
        int[] tm2 = {-seconds, -seconds, -minutes, -hours, -days, -months, -years, -centuries};
        int[] tm = isNegative ? tm2 : tm1;

        for (int i = tm.length - 1; i > 0; i--)
        {
            if (i > 1 && tm[i] > 0 || i == 1 && tm[i] > 20)
                return tm[i] + specialSuffix[i];
        }
        return specialSuffix[0];
    }

    public boolean isNegative()
    {
        return isNegative;
    }

    public int getCenturies()
    {
        return centuries;
    }

    public int getYears()
    {
        return years;
    }

    public int getMonths()
    {
        return months;
    }

    public void setDays(int days)
    {
        this.days = days;
    }

    public int getHours()
    {
        return hours;
    }

    public void setMinutes(int minutes)
    {
        this.minutes = minutes;
    }

    public int getSeconds()
    {
        return seconds;
    }

    public int getMilliseconds()
    {
        return milliseconds;
    }

    public UTC getUTCFrom()
    {
        return from;
    }

    public UTC getUTCTo()
    {
        return to;
    }
}