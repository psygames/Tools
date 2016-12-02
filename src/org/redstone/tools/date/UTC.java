package org.redstone.tools.date;


public class UTC implements Comparable<UTC>
{
    private static int[] monthDaysTotalInterval = null;
    public static final UTC TIMESTAMP_ZERO = new UTC(1970, 1, 1, 0, 0, 0, 0);
    public static final int[] normalMonthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int[] leapMonthDays = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int millisecond;

    /**
     * 懒加载
     * 由于月份的天数是以400*12个月形成一个循环
     * 这里使用400*12的每月累加天数形成的数组(第一月为0)
     *
     * @return 累加数组
     */
    public static final int[] getMonthDaysTotalInterval()
    {
        if (monthDaysTotalInterval != null)
            return monthDaysTotalInterval;
        int increase = 0;
        monthDaysTotalInterval = new int[400 * 12];
        for (int i = 0; i < 400; i++)
        {
            boolean isLeap = i % 4 == 0 && i % 100 != 0 || i % 400 == 0;
            int[] monDays = isLeap ? leapMonthDays : normalMonthDays;
            for (int j = 0; j < 12; j++)
            {
                monthDaysTotalInterval[i * 12 + j] = increase;
                increase += monDays[j];
            }
        }
        return monthDaysTotalInterval;
    }

    /**
     * 用时间戳初始化 UTC 时间
     *
     * @param timestamp 时间戳
     */
    public UTC(long timestamp)
    {
        this.copyFrom(TIMESTAMP_ZERO);
        this.add(timestamp);
    }

    /**
     * 使用时间初始化 UTC 时间
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        时
     * @param minute      分
     * @param second      秒
     * @param millisecond 毫秒
     */
    public UTC(int year, int month, int day, int hour, int minute, int second, int millisecond)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    /**
     * UTC 时间相减
     *
     * @param a 被减时间,常理上应该要小于this时间
     * @return 时间间隔
     */
    public UTCSpan subtract(UTC a)
    {
        return new UTCSpan(a, this);
    }

    public void add(long milliseconds)
    {
        long adds = milliseconds;
        int carry = 0;

        // millisecond
        millisecond += adds % 1000;
        carry = millisecond < 0 ? -1 : millisecond / 1000;
        millisecond = (millisecond + 1000) % 1000;
        adds /= 1000L;

        // second
        second += adds % 60 + carry;
        carry = second < 0 ? -1 : second / 60;
        second = (second + 60) % 60;
        adds /= 60L;

        // minute
        minute += adds % 60 + carry;
        carry = minute < 0 ? -1 : minute / 60;
        minute = (minute + 60) % 60;
        adds /= 60L;

        // hour
        hour += adds % 24 + carry;
        carry = hour < 0 ? -1 : hour / 24;
        hour = (hour + 24) % 24;
        adds /= 24L;

        // 以下是核心代码
        adds += carry;											// 天数进位
        int[] mdInterval = getMonthDaysTotalInterval();
        int mstart = (year % 400) * 12 + month - 1;				// 计算出当前年月份在 400 年月份循环中的位置
        adds += mdInterval[mstart] + day - 1;					// 天数加上当前年的 400年循环天数 余数
        int m400Incr = year / 400 + (int) (adds / 146097);		// 计算出400年循环的次数，146097为400年天数总和，这里没考虑负数天数的影响，下边处理哦
        adds %= 146097;											// 400年中的天数
        if(adds < 0)											// 计算天数为负数时候，需要借位，
        {
        	adds += 146097;
        	m400Incr -= 1;
        }
        int mindex = divFind(mdInterval, (int) adds, 0, mdInterval.length - 1); // 找到天数在 400 年月天数中的所处位置

        day = (int) adds - mdInterval[mindex] + 1;				// 计算天数
        month = mindex % 12 + 1;								// 月份
        year = 400 * m400Incr + mindex / 12;					// 年份	
    }

    private int divFind(int[] array, int value, int from, int to)
    {
        if (from + 1 >= to)
            return from;

        int mid = (from + to) / 2;
        if (array[mid] > value)
            return divFind(array, value, from, mid);
        else
            return divFind(array, value, mid, to);
    }

    /**
     * 当前月的天数(考虑闰年)
     *
     * @return 天数
     */
    public int monthDays()
    {
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
            return leapMonthDays[month - 1];
        return normalMonthDays[month - 1];
    }

    /**
     * UTC 当前时间(使用System.currentTimeMillis())
     *
     * @return UTC 当前时间
     */
    public static UTC now()
    {
        return new UTC(System.currentTimeMillis());
    }

    private void copyFrom(UTC from)
    {
        this.year = from.year;
        this.month = from.month;
        this.day = from.day;
        this.hour = from.hour;
        this.minute = from.minute;
        this.second = from.second;
        this.millisecond = from.millisecond;
    }

    /**
     * 时间大小比较
     *
     * @param a 被减参数
     * @return 减 结果
     */
    public int compareTo(UTC a)
    {
        int v = 0;
        if ((v = this.getYear() - a.getYear()) != 0)
            return v;
        else if ((v = this.getMonth() - a.getMonth()) != 0)
            return v;
        else if ((v = this.getDay() - a.getDay()) != 0)
            return v;
        else if ((v = this.getHour() - a.getHour()) != 0)
            return v;
        else if ((v = this.getMinute() - a.getMinute()) != 0)
            return v;
        else if ((v = this.getSecond() - a.getSecond()) != 0)
            return v;
        else if ((v = this.getMillisecond() - a.getMillisecond()) != 0)
            return v;
        return v;
    }

    @Override
    public String toString()
    {
        return String.format("%04d/%02d/%02d %02d:%02d:%02d %03d"
                , year, month, day, hour, minute, second, millisecond);
    }

    @Override
    public UTC clone()
    {
        return new UTC(year, month, day, hour, minute, second, millisecond);
    }

    public int getYear()
    {
        return year;
    }

    public int getMonth()
    {
        return month;
    }

    public int getDay()
    {
        return day;
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public int getSecond()
    {
        return second;
    }

    public int getMillisecond()
    {
        return millisecond;
    }
}

