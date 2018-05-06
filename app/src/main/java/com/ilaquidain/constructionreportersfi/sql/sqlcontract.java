package com.ilaquidain.constructionreportersfi.sql;

import android.provider.BaseColumns;

/**
 * Created by ilaquidain on 08/03/2018.
 */

public final class sqlcontract {

    private sqlcontract(){}

    public static class QCEntry implements BaseColumns{
        public static final String TABLE_NAME = "QC_Reports";
        public static final String COLUMN_REPORT_DATE = "Report_Date";
        public static final String COLUMN_REPORT_TIME_IN = "Report_Time_In";
        public static final String COLUMN_REPORT_TIME_OUT = "Report_Time_Out";
    }
}
