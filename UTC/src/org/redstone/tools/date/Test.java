package org.redstone.tools.date;

public class Test
{
    public static void main(String[] args) throws InterruptedException
    {
        /*------- TEST UTC -------*/

        /*-------- UTC --------*/
        System.out.println(new UTC(0, 0, 0, 0, 0, 0, 0));
        System.out.println(new UTC(1991, 12, 13, 0, 0, 0, 0));
        System.out.println(new UTC(-1991, 12, 13, 0, 0, 0, 0)); //公元前.  注意:除年份外,其他时间单位不能为负数

        /*-------- Timestamp --------*/
        System.out.println(new UTC(0L));
        System.out.println(new UTC(999999999999L));
        System.out.println(new UTC(-1));

        /*-------- Others -------*/
        System.out.println(UTC.TIMESTAMP_ZERO);
        System.out.println(UTC.now());





        /*-------- TEST UTCSpan -------*/

        UTC from;
        UTC to;
        UTCSpan span;

        /*-------- UTC --------*/

        //20161023152959999
        //20151023153000000
        from = new UTC(2016, 10, 23, 15, 29, 59, 999);
        to = new UTC(2015, 10, 23, 15, 30, 0, 0);
        span = to.subtract(from);
        printSpan(span);

        //20151023153000000
        //20161023152000000
        from = new UTC(2015, 10, 23, 15, 30, 0, 0);
        to = new UTC(2016, 10, 23, 15, 30, 0, 0);
        span = to.subtract(from);
        printSpan(span);

        //00001023153000000
        //20161023152959999
        from = new UTC(0000, 10, 23, 15, 30, 0, 0);
        to = new UTC(-2016, 10, 23, 15, 29, 59, 999);
        span = to.subtract(from);
        printSpan(span);

        /*------ TimeStamp -------*/
        //100
        //1
        from = new UTC(100);
        to = new UTC(1);
        span = to.subtract(from);
        printSpan(span);

        //0
        //1000000000000L
        from = new UTC(0);
        to = new UTC(1000000000000L);
        span = to.subtract(from);
        printSpan(span);

        /*------ Others ------*/
        from = UTC.now();
        while (true)
        {
            Thread.sleep(1000);
            span = UTC.now().subtract(from);
            printSpan(span);
        }
    }

    private static void printSpan(UTCSpan span)
    {
        System.out.println(span.getUTCTo() + "  -  " + span.getUTCFrom() + "  =  " + span + "  -->  " + span.toSpecialString());
    }
}
